#!/usr/bin/env bash

set -e

current="$(date +'%d-%m-%H:%M:%S')"
~/.local/bin/aws s3 sync doc s3://my-travis-ci-bucket/nim/${current}/${TRAVIS_COMMIT}/javadoc --region=eu-central-1 --delete
~/.local/bin/aws s3 sync report s3://my-travis-ci-bucket/nim/${current}/${TRAVIS_COMMIT}/tests --region=eu-central-1 --delete
