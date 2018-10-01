#!/bin/bash 

START_DIR=$( cd `dirname $0`  && pwd )

echo "starting in ${START_DIR}..."

D=${TMPDIR:-${TRAVIS_TMPDIR:-/tmp}}/book 
echo "the book clone will be at ${D}"

URI=https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git 

if [ ! -d $D ] ; then 
	msg="cloned the http://github.com/joshlong/reactive-spring-book into ${D}.."
	mkdir -p $(dirname $D)
	git clone $URI $D && echo $msg || echo "couldn't clone $URI .."
fi 

cd $D 
git pull 
./bin/build-pdf.sh screen 

mkdir -p $START_DIR/output
cp index.pdf $START_DIR/output/book-screen.pdf 
cd $START_DIR
cat ${START_DIR}/.git/config 
git add $START_DIR/output/book-screen.pdf  
git remote set-url origin $URI
git checkout -b output-artifacts
git commit -am "updated artifacts"
git push  origin output-artifacts

