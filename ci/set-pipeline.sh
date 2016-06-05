#!/bin/sh
echo y | fly -t azr sp -p blog-feed -c pipeline.yml -l ../../credentials.yml
