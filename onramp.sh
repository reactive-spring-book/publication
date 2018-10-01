#!/bin/bash 

START_DIR=$( cd `dirname $0`  && pwd )

echo "starting in ${START_DIR}..."

D=${TMPDIR:-${TRAVIS_TMPDIR:-/tmp}}/book 
echo "the book clone will be at ${D}"

if [ ! -d $D ] ; then 
	msg="cloned the http://github.com/joshlong/reactive-spring-book into ${D}.."
	mkdir -p $(dirname $D)
	URI=https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git 
	git clone $URI $D && echo $msg || echo "couldn't clone $URI .."
fi 

cd $D 
git pull 
./bin/build-pdf.sh screen 

pdf=${START_DIR}/build/asciidoc/pdf-screen/index.pdf
ls -la $pdf 


