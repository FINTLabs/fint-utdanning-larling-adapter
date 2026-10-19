#!/usr/bin/env bash
set -euo pipefail

source "$(dirname "$0")/paths.sh"

base="${1:-}"
head="${2:-HEAD}"

if [ -z "$base" ] || ! git cat-file -e "$base^{commit}" 2>/dev/null; then
  echo "app=true"
  exit 0
fi

if git diff --quiet "$base" "$head" -- $APP_PATHS .github/workflows/CI.yaml; then
  echo "app=false"
else
  echo "app=true"
fi
