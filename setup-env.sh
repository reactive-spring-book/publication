#!/bin/bash

# Ghostscript 
#sudo -E apt-get -yq --no-install-suggests --no-install-recommends install imagemagick ghostscript rvm 
sudo apt-add-repository -y ppa:rael-gc/rvm
sudo add-apt-repository -y ppa:moti-p/cc
sudo apt-get update

sudo apt-get -y --reinstall install ghostscript
sudo apt-get -y --reinstall install rvm
sudo apt-get -y --reinstall install software-properties-common


rvm install "ruby-2.3.1"
gem install bundler -v '< 2'



