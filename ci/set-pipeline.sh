#!/bin/sh
echo y | fly -t home sp -p blog-feed -c pipeline.yml -l ../../credentials.yml
