#!/bin/bash

# RVM ..  
sudo apt-get install software-properties-common
sudo apt-add-repository -y ppa:rael-gc/rvm
sudo apt-get update
sudo apt-get install rvm
rvm install "ruby-2.3.1"
gem install bundler -v '< 2'


