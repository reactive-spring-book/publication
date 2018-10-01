#!/bin/bash 

export 
D=${TMPDIR:-${TRAVIS_TMPDIR:-/tmp}}/book 
echo "the book clone will be at ${D}"

if [ ! -d $D ] ; then 
	msg="cloned the http://github.com/joshlong/reactive-spring-book into ${D}. \n\nNow, going to attempt a book build..."
	mkdir -p $(dirname $D)
	git clone https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git $D &&  echo  $msg \
		||  echo "couldn't clone https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git .."
fi 

cd $D 
git pull 




