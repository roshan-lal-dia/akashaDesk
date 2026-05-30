#!/bin/sh
set -eu

wget -q -O - "http://127.0.0.1:${PORT:-8080}/healthz" | grep -q '"status":"ok"'

