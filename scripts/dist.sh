#!/bin/bash

IMAGE_NAME="${IMAGE_NAME:?missing required input \'IMAGE_NAME\'}"
VERSION="${VERSION:?missing required input \'VERSION\'}"

IMAGE_VERSION="${VERSION}"
# remove any quotes (single or double) from the PRERELEASE string
PRERELEASE="${PRERELEASE//\"}"
PRERELEASE="${PRERELEASE//\'}"
if [[ ${PRERELEASE} != "" ]]; then
    IMAGE_VERSION=$(git describe --tags --always)
fi

GIT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

configs=()

build_image() {
  docker build -t "${IMAGE_NAME}" --build-arg VERSION="${VERSION}" .
  docker tag "${IMAGE_NAME}" "${IMAGE_NAME}:${IMAGE_VERSION}"
}

promote_image() {
  local prev_ver="$1"
  docker "${configs[@]}" pull "${IMAGE_NAME}:${prev_ver}"
  docker tag "${IMAGE_NAME}:${prev_ver}" "${IMAGE_NAME}:${IMAGE_VERSION}"
  docker "${configs[@]}" push "${IMAGE_NAME}:${IMAGE_VERSION}"
}

publish_image() {
  # push the specific tag version
  docker "${configs[@]}" push "${IMAGE_NAME}:${IMAGE_VERSION}"
  # push the latest tag
  docker "${configs[@]}" push "${IMAGE_NAME}:latest"
}

login() {

  :

}

PUB='false'
PROMOTE='false'
PREV_VERSION=""
case "$1" in
  --publish)
      PUB='true'
      ;;
  --promote)
      PROMOTE='true'
      PREV_VERSION="$2"
      ;;
  *)
      ;;
esac

echo "IMAGE_NAME = ${IMAGE_NAME}"
echo "IMAGE_VERSION = ${IMAGE_VERSION}"

case "${GIT_BRANCH}" in
  develop)
    if [[ ${PRERELEASE} != "" ]]; then
      build_image
      if [[ "${PUB}" == *"true"* ]]; then
        login
        publish_image
      fi
    else
      echo "ERROR: PRERELEASE not set on develop branch"
      exit 1
    fi
    ;;
  master)
    if [[ "${PROMOTE}" == *"true"* ]]; then
      if [[ ${PREV_VERSION} == "" ]]; then
        echo "ERROR: previous version not provided"
        exit 1
      fi
      login
      promote_image "${PREV_VERSION}"
    else
      if [[ ${PRERELEASE} == "" ]] || [[ ${PRERELEASE} == "rc" ]]; then
        build_image
        if [[ "${PUB}" == *"true"* ]]; then
          login
          publish_image
        fi
      else
        echo "ERROR: unexpected PRERELEASE value ${PRERELEASE}"
        exit 1
      fi
    fi
    ;;
  *)
    build_image
    if [[ "${IMAGE_NAME}" != "containers.xxx.com"* ]]; then
      publish_image
    fi
    ;;
esac
