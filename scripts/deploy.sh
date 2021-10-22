#!/bin/bash

set -e

# set the DEBUG env variable to turn on debugging
[[ -n "$DEBUG" ]] && set -x

# export each of the variables so that each is accessible from the environment
export INSTANCE
export INSTANCE_NAME
export APP_NAME
export NAMESPACE
export USERNAME
export AVAIL_ZONE
export IMAGE_NAME
export IMAGE_TAG
export LIFECYCLE
export REPLICAS

NAMESPACE="${DEPLOY_NS:?missing required input \'DEPLOY_NS\'}"
VERSION="${VERSION:?missing required input \'VERSION\'}"
LIFECYCLE="${LIFECYCLE:-nprd}"

PROJECT_FILE="${PROJECT_FILE:-project.yml}"

APP_NAME=$(yaml read "${PROJECT_FILE}" metadata.name)
IMAGE_NAME=$(yaml read "${PROJECT_FILE}" metadata.image)
REPLICAS=$(yaml read "${PROJECT_FILE}" metadata.instances.${LIFECYCLE})
if [[ "${REPLICAS}" == 'null' ]]; then
  REPLICAS=1
fi

IMAGE_TAG="${VERSION}"
# remove any quotes (single or double) from the PRERELEASE string
PRERELEASE="${PRERELEASE//\"}"
PRERELEASE="${PRERELEASE//\'}"
if [[ ${PRERELEASE} != "" ]]; then
    IMAGE_TAG=$(git describe --tags --always)
fi

CONFIG_TMPL="templates/config.tmpl"
DEPLOY_TMPL="templates/deploy.tmpl"
ROUTE_TMPL="templates/route.tmpl"
SERVICE_TMPL="templates/service.tmpl"
CONFIGMAP_TMPL="templates/configmap.tmpl"
SECRETS_TMPL="templates/secrets.tmpl"

CONFIG_SPEC="/tmp/config.yaml"
DEPLOY_SPEC="/tmp/deploy.yaml"
ROUTE_SPEC="/tmp/route.yaml"
SERVICE_SPEC="/tmp/service.yaml"
CONFIGMAP_SPEC="/tmp/configmap.yaml"
SECRETS_SPEC="/tmp/secrets.yaml"


make_registry_secret() {
  local username="$1"
  local token="$2"

  oc create secret docker-registry image-pull-secret \
    --docker-server="containers.xxxx.com" \
    --docker-username="$username" \
    --docker-password="$token" \
    --docker-email="unused" || echo "image pull secret already exists"

  oc secrets link default image-pull-secret --for=pull || echo "link to secret already exists"
}

create_registry_secret() {
  username=""
  token="${BOT_PWD_MASK:?missing required input \'BOT_PWD_MASK\'}"

  make_registry_secret "$username" "$token"
}

create_local_registry_secret() {
  username=$(git config --get user.name)
  token="${REGISTRY_TOKEN:?missing registry token for user $username}"

  make_registry_secret "$username" "$token"
}

check_az() {
  AVAIL_ZONE="${DEPLOY_AZ:?missing required input \'DEPLOY_AZ\'}"
  # make sure that avail zone is a real value
  case "${AVAIL_ZONE}" in
    np-alln|prd-alln|np-rcdn|prd-rcdn|np-rtp|prd-rtp)
      USERNAME="${DEPLOY_USER:?missing required input \'DEPLOY_USER\'}"
      PASSWD="${DEPLOY_PWD:?missing required input \'DEPLOY_PWD\'}"
      INSTANCE="https://cae-${AVAIL_ZONE}.com"
      INSTANCE_NAME=""
      ;;
    local)
      INSTANCE="${LOCAL_CLUSTER:-https://10.0.75.2:8443}"
      prefix="https://"
      val="${INSTANCE#$prefix}"
      INSTANCE_NAME="${val//./-}"

      USERNAME="developer"
      PASSWD="developer"
      IMAGE_NAME="172.30.1.1:5000/${NAMESPACE}/${APP_NAME}"
      IMAGE_TAG="latest"
      ;;
    *)
      echo "ERROR: Unknown availability zone: ${AVAIL_ZONE}"
      exit 1
      ;;
  esac
}

dogen() {
  local template="$1"
  local spec="$2"

  if [ -f "${template}" ]; then

    dinamo gen -e --template "$template" --file "$spec"

    echo "==========="
    echo "Spec Location: $spec"
    echo
    cat "$spec"
    echo "==========="
    echo
  else
    echo "no ${template} found; skipping..."
  fi

}

generate() {
  dogen "${DEPLOY_TMPL}" "${DEPLOY_SPEC}"
  dogen "${SERVICE_TMPL}" "${SERVICE_SPEC}"
  dogen "${ROUTE_TMPL}" "${ROUTE_SPEC}"
  dogen "${CONFIGMAP_TMPL}" "${CONFIGMAP_SPEC}"
  dogen "${SECRETS_TMPL}" "${SECRETS_SPEC}"
}

login() {
  dogen "${CONFIG_TMPL}" "${CONFIG_SPEC}"

  # set where Kubernetes configuration file lives for the clusters
  export KUBECONFIG="${CONFIG_SPEC}"

  # login into the avail zone
  auth_res=$(oc login --insecure-skip-tls-verify --username="${USERNAME}" --password="${PASSWD}" "${INSTANCE}")
  echo "$auth_res"
  if [[ $auth_res != "Login successful"* ]]; then
    # login failed so exit immediately
    exit 1
  fi

  prj_res=$(oc project "${NAMESPACE}")
  echo "$prj_res"
  if [[ $prj_res != "Now using project"* ]] && [[ $prj_res != "Already on project"* ]]; then
    # user does not have access to project or project does not exist; exit immediately
    exit 1
  fi
}

release() {
  # create or update service (api)
  if [ -f "${SERVICE_SPEC}" ]; then
    if ! service=$(oc get -f "${SERVICE_SPEC}" 2>&1); then
      oc create -f "${SERVICE_SPEC}"
    else
      echo "service already exist"
      echo -e "previous service:\\n$service\\n\\n"
      oc apply -f "${SERVICE_SPEC}"
    fi
  fi

  # create route (if needed)
  if [ -f "${ROUTE_SPEC}" ]; then
    if ! route=$(oc get -f "${ROUTE_SPEC}" 2>&1); then
      oc create -f "${ROUTE_SPEC}"
    else
      echo "Route already exists"
      echo -e "$route\\n\\n"
    fi
  fi

  # create or update configmap
  if [ -f "${CONFIGMAP_SPEC}" ]; then
    if ! configmap=$(oc get -f "${CONFIGMAP_SPEC}" 2>&1); then
      oc create -f "${CONFIGMAP_SPEC}"
    else
      echo "Configmap already exist"
      echo -e "previous configmap:\\n$configmap\\n\\n"
      oc replace -f "${CONFIGMAP_SPEC}"
    fi
  fi

  # create or update secrets
  if [ -f "${SECRETS_SPEC}" ]; then
    if ! secrets=$(oc get -f "${SECRETS_SPEC}" 2>&1); then
      oc create -f "${SECRETS_SPEC}"
    else
      echo "Secrets already exist"
      echo -e "previous secrets:\\n$secrets\\n\\n"
      oc replace -f "${SECRETS_SPEC}"
    fi
  fi

  # create or update deploy configuration (api)
  if [ -f "${DEPLOY_SPEC}" ]; then
    if ! dc=$(oc get -f "${DEPLOY_SPEC}" 2>&1); then
      oc create -f "${DEPLOY_SPEC}"
    else
      echo "api deployment config already exist"
      echo -e "previous dc:\\n$dc\\n\\n"
      oc replace -f "${DEPLOY_SPEC}"
    fi

    # execute the deployment; this could likely show failure
    # after the configuration was either created or updated as
    # that can trigger a deploy as well. But to be safe, try it
    # and ignore the error.
    oc rollout latest "dc/${APP_NAME}" || echo "deployment error ignored; investigate if message not '* is already in progress (Pending).'"
  fi

}

### Main
GIT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
case "${GIT_BRANCH}" in
  develop|master)
    check_az
    login
    create_registry_secret
    ;;
  *)
    DEPLOY_AZ='local'

    check_az
    login
    create_local_registry_secret
    ;;
esac

# generate files
generate

# perform release
release
