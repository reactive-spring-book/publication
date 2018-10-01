#!/bin/bash 

export 
D=${TMPDIR:-${TRAVIS_TMPDIR:-/tmp}}/book 
echo "the book clone will be at ${D}"

if [ ! -d $D ] ; then 
	git clone https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git $D 
	echo "cloned the http://github.com/joshlong/reactive-spring-book into ${D}."
	echo "Now, going to attempt a book build..."
fi 

cd $D 
git pull 




