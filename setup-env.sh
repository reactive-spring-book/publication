#!/bin/bash

# Ghostscript 

sudo add-apt-repository -y ppa:moti-p/cc
sudo apt-get update
sudo apt-get -y --reinstall install ghostscript
sudo apt-get -y --reinstall install gs-esp
# sudo apt-get -y --reinstall install imagemagick
gs 

# RVM ..  
echo "exiting!!!!!!!!!"
exit 
sudo apt-get install software-properties-common
sudo apt-add-repository -y ppa:rael-gc/rvm
sudo apt-get update
sudo apt-get install rvm
rvm install "ruby-2.3.1"
gem install bundler -v '< 2'



