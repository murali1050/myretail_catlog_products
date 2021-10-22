MAKEFLAGS += -r --warn-undefined-variables
SHELL := /bin/bash
.SHELLFLAGS := -o pipefail -euc
.DEFAULT_GOAL := build

include Makefile.variables
include Makefile.local

.PHONY: help clean veryclean tag-build build build-image format check test cover generate todo adhoc deploy next-dev start-rc finish-rc promote start-release finish-release pub-image

## display this help message
help:
	@echo 'Management commands for catalog:'
	@echo
	@echo 'Usage:'
	@echo '  ## Build Commands'
	@echo '    build           Compile the project.'
	@echo '    tag-build       Add git tag for latest build.'
	@echo '    build-image     Build deployable docker image.'
	@echo
	@echo '  ## Develop / Test Commands'
	@echo '    format          Run code formatter.'
	@echo '    check           Run static code analysis (lint).'
	@echo '    test            Run tests on project.'
	@echo '    cover           Run tests and capture code coverage metrics on project.'
	@echo '    generate        Run code generator for project.'
	@echo '    todo            Generate a TODO list for project.'
	@echo '    clean           Clean the directory tree of produced artifacts.'
	@echo '    veryclean       Same as clean but also removes cached dependencies.'
	@echo
	@echo '  ## Release Commands'
	@echo '    pub-image       Push tagged docker images to registry.'
	@echo '    deploy          Deploy project to local Openshift or Openshift (CAE) for specified lifecycle.'
	@echo '    next-dev        Prepare project for development of next version.'
	@echo '    start-rc        Start creation of release candidate version of project.'
	@echo '    finish-rc       Finish creation of release candidate version of project.'
	@echo '    promote         Promote release candidate version to production version of project.'
	@echo '    start-release   Start release of version of project.'
	@echo '    finish-release  Finish release of version of project.'
	@echo
	@echo '  ## Local Commands'
	@echo '    setup           Configures Minishfit/Docker directory mounts.'
	@echo '    drma            Removes all stopped containers.'
	@echo '    drmia           Removes all unlabelled images.'
	@echo '    drmvu           Removes all unused container volumes.'
	@echo

.ci-clean:
ifeq ($(CI_ENABLED),1)
	@rm -f tmp/dev_image_id || :
endif

## Clean the directory tree of produced artifacts.
clean: .ci-clean prepare
	@${DOCKERRUN} bash -c 'rm -rf target/*'

## Same as clean but also removes cached dependencies.
veryclean: clean
	@${DOCKERRUN} bash -c 'rm -rf tmp mvn-repo'

## builds the dev container
prepare: tmp/dev_image_id
tmp/dev_image_id: Dockerfile.dev
	@mkdir -p tmp
	@docker rmi -f ${DEV_IMAGE} > /dev/null 2>&1 || true
	@echo "## Building dev container"
	@docker build --quiet -t ${DEV_IMAGE} --build-arg DEVELOPER="${DEVELOPER}" -f Dockerfile.dev .
	@docker inspect -f "{{ .ID }}" ${DEV_IMAGE} > tmp/dev_image_id

# ----------------------------------------------
# build

## Compile the project.
build: build/dev

build/dev: format
	${DOCKERRUN} bash ./scripts/build.sh

## Add git tag for latest build.
tag-build: prepare
	${DOCKERNOVENDOR} bash ./scripts/version.sh build

# ----------------------------------------------
# develop and test

## print environment info about this dev environment
debug:
	@echo ROOT="$(ROOT)"
	@echo VERSION="$(VERSION)"
	@echo PRERELEASE="$(PRERELEASE)"
	@echo RELEASE_TYPE="$(RELEASE_TYPE)"
	@echo TAG_TYPE="$(TAG_TYPE)"
	@echo IMAGE_NAME="$(IMAGE_NAME)"
	@echo
	@echo docker commands run as:
	@echo "$(DOCKERRUN)"

## Run code formatter.
format: prepare
	${DOCKERRUN} bash ./scripts/format.sh
ifeq ($(CI_ENABLED),1)
	@if ! git diff-index --quiet HEAD; then echo "java-formater modified code; requires attention!"; exit 1; fi
else
	@if ! git diff-index --quiet HEAD; then echo "java-formater modified code; requires attention!"; fi
endif

## Run static code analysis (lint).
check: format
	${DOCKERRUN} bash ./scripts/check.sh

## Run tests on project.
test: format
	${DOCKERRUN} bash ./scripts/test.sh

## Run tests and capture code coverage metrics on project.
cover: format
ifeq ($(CI_ENABLED),1)
	${DOCKERRUN} bash ./scripts/cover.sh --jenkins
else
	${DOCKERRUN} bash ./scripts/cover.sh
endif

## Build deployable docker image.
build-image:
	@bash ./scripts/dist.sh

## Push tagged docker images to registry.
pub-image:
	@bash ./scripts/dist.sh --publish

## Run code generator for project.
generate:
	@docker pull swaggerapi/swagger-codegen-cli-v3
	docker run --rm \
		-v "${ROOT}":/mnt \
		-w /mnt \
		swaggerapi/swagger-codegen-cli-v3 generate \
		-i "${API_FILE}" \
		--lang spring \
		-c config/codegen_config.json
	@find src \( -path ./mvn-repo -o -path ./.git \) -prune -o -type d -exec chmod 755 {} \; || :
	@find src \( -path ./mvn-repo -o -path ./.git \) -prune -o -name "*.java" -exec chmod 644 {} \; || :

# generate a TODO.md file with a list of TODO and FIXME items sorted by file
# the string is case insensitive and is removed from the output. So the final output
# should provide the file, line number, username that added it, and message about what
# needs to be done.
# Excludes the Makefile from consideration. Only files that are being tracked in git are
# included by default, therefore external dependencies or anything that is part of gitignore
# is automatically excluded.
## Generate a TODO list for project.
todo: prepare
	${DOCKERNOVENDOR} bash ./scripts/todo.sh -e Makefile -e scripts/todo.sh -t '(FIXME|TODO)'

# usage: make adhoc RUNTHIS='command to run inside of dev container'
# example: make adhoc RUNTHIS='which glide'
adhoc: prepare
	@${DOCKERRUN} ${RUNTHIS}

# ----------------------------------------------
# release

## Deploy project to local Openshift or Openshift (CAE) for specified lifecycle.
deploy: prepare
	@echo "## Deploying to ${LIFECYCLE}"
	@${DOCKERNOVENDOR} bash ./scripts/deploy.sh

## Prepare project for development of next version.
next-dev: prepare
	@echo "## Incrementing version: ${VERSION}"
	${DOCKERNOVENDOR} bash ./scripts/version.sh dev

## Start creation of release candidate version of project.
start-rc: .pre-rel prepare
	${DOCKERNOVENDOR} bash ./scripts/version.sh rc

## Finish creation of release candidate version of project.
finish-rc: .pre-rel
	@BRANCH=$$(git rev-parse --abbrev-ref HEAD) ; RC_VERSION=$${BRANCH#release/} ; git flow release finish -p -m "Release $${RC_VERSION}" $${RC_VERSION}
	${DOCKERNOVENDOR} bash ./scripts/version.sh rctodev

## Promote release candidate version to production version of project.
promote: prepare
	@PREV_VERSION=$$(git describe --tags --always); ${DOCKERNOVENDOR} bash ./scripts/version.sh promote ; bash ./scripts/dist.sh --promote "$${PREV_VERSION}"

## Start release of version of project.
start-release: .pre-rel prepare
	@echo "## Starting release for version: ${VERSION}"
	@git flow release start ${VERSION}
	${DOCKERNOVENDOR} bash ./scripts/version.sh rel

## Finish release of version of project.
finish-release: .pre-rel
	@echo "## Releasing version: ${VERSION}"
	@git flow release finish -p -m "Release ${VERSION}" ${VERSION}

.pre-rel:
ifndef HAS_GITFLOW
	$(error "You must install git-flow")
endif
