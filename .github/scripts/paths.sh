#!/usr/bin/env bash

# Everything that ends up in the Docker image. Commits outside these paths
# (kustomize, workflows, docs) reuse the image already in the registry.
APP_PATHS="src build.gradle settings.gradle gradle.properties gradle gradlew gradlew.bat Dockerfile"
