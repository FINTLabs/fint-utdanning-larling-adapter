#!/usr/bin/env bash
set -euo pipefail

source "$(dirname "$0")/paths.sh"

# The last commit that touched the app. A merge from develop to main that brings
# no new app changes resolves to the same commit, so main deploys the image beta
# already runs instead of building a new one.
git log -1 --abbrev=7 --format=%h -- $APP_PATHS
