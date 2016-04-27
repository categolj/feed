#!/bin/sh
echo y | fly -t do sp -p blog-feed -c pipeline.yml -l ../../credentials.yml
