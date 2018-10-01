#!/bin/bash 

START_DIR=$( cd `dirname $0`  && pwd )

echo "starting in ${START_DIR}..."

D=${TMPDIR:-${TRAVIS_TMPDIR:-/tmp}}/book 

if [ -d $D ]; then 
	rm -rf $D 
fi 

echo "the book clone will be at ${D}"

URI=https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git 

if [ ! -d $D ] ; then 
	msg="cloned the http://github.com/joshlong/reactive-spring-book into ${D}.."
	mkdir -p $(dirname $D)
	git clone $URI $D && echo $msg || echo "couldn't clone $URI .."
fi 

cd $D 
git pull 


## 
OUTPUT_DIR=$HOME/output

BUILD_SCREEN_FN=book-screen.pdf 
BUILD_SCREEN=${OUTPUT_DIR}/${BUILD_SCREEN_FN} 

BUILD_PREPRESS_FN=book-prepress.pdf 
BUILD_PREPRESS=${OUTPUT_DIR}/${BUILD_PREPRESS_FN}

mkdir -p $OUTPUT_DIR 

export BUILD_PDF_OUTPUT_FILE=$BUILD_SCREEN
./bin/build-pdf.sh screen 

export BUILD_PDF_OUTPUT_FILE=$BUILD_PREPRESS
./bin/build-pdf.sh 



## lets commit the results to our repo 

cd $D
ARTIFACT_TAG=output-artifacts


git remote set-url origin $URI
git checkout -b $ARTIFACT_TAG

if [ -d  $D/output ]; then 
	mkdir -p $D/output
	git add $D/output
fi 

cp $BUILD_PREPRESS $D/output/${BUILD_PREPRESS_FN}
cp $BUILD_SCREEN $D/output/${BUILD_SCREEN_FN}

git commit -am "updated artifacts"
git push --force origin $ARTIFACT_TAG
