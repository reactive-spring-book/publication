#!/bin/bash 

START_DIR=$( cd `dirname $0`  && pwd )

echo "starting in ${START_DIR}..."

BOOK_CHECKOUT=${TMPDIR:-${TRAVIS_TMPDIR:-/tmp}}/book 

if [ -d $BOOK_CHECKOUT ]; then 
	rm -rf $BOOK_CHECKOUT 
fi 

echo "the book clone will be at ${BOOK_CHECKOUT}"

URI=https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git 

if [ ! -d $BOOK_CHECKOUT ] ; then 
	msg="cloned the http://github.com/joshlong/reactive-spring-book into ${BOOK_CHECKOUT}.."
	mkdir -p $(dirname $BOOK_CHECKOUT)
	git clone $URI $BOOK_CHECKOUT && echo $msg || echo "couldn't clone $URI .."
fi 

cd $BOOK_CHECKOUT 
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

ls -la $BUILD_SCREEN
ls -la $BUILD_PREPRESS

exit 
## lets commit the results to our repo 

cd $BOOK_CHECKOUT
ARTIFACT_TAG=output-artifacts


git remote set-url origin $URI
git checkout -b $ARTIFACT_TAG

git pull origin $ARTIFACT_TAG --allow-unrelated-histories

if [ -d  $BOOK_CHECKOUT/output ]; then 
	mkdir -p $BOOK_CHECKOUT/output
	git add $BOOK_CHECKOUT/output
fi 

cp $BUILD_PREPRESS $BOOK_CHECKOUT/output/${BUILD_PREPRESS_FN}
cp $BUILD_SCREEN $BOOK_CHECKOUT/output/${BUILD_SCREEN_FN}

git commit -am "updated artifacts"
git push --force origin $ARTIFACT_TAG
