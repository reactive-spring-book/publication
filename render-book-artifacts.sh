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
echo "inside the book checkout directory ${BOOK_CHECKOUT}." 
pwd 

git pull 


## 
## Now we need to make sure the required code 
## is in place so that our code snippets don't fail
## 
echo "we need to see what the tree shows us"

mkdir -p $BOOK_CHECKOUT/../code && cd $BOOK_CHECKOUT/../code 


cat $START_DIR/repositories.txt | while read l ; do git clone $l   ; done 


cd $BOOK_CHECKOUT


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

./bin/epub.sh 

ls -la $BUILD_SCREEN
ls -la $BUILD_PREPRESS

## lets commit the results to our repo 

cd $BOOK_CHECKOUT

echo "book checkout is : `pwd` " 
mkdir -p $BOOK_CHECKOUT

ARTIFACT_TAG=output-artifacts


git remote set-url origin $URI


BOOK_CHECKOUT_OUTPUT=$BOOK_CHECKOUT/output


git checkout $ARTIFACT_TAG

mkdir -p $BOOK_CHECKOUT_OUTPUT


git add $BOOK_CHECKOUT_OUTPUT
cp $BUILD_PREPRESS $BOOK_CHECKOUT_OUTPUT/${BUILD_PREPRESS_FN}
cp $BUILD_SCREEN $BOOK_CHECKOUT_OUTPUT/${BUILD_SCREEN_FN}

git add $BOOK_CHECKOUT_OUTPUT/* 
git commit -am "adding built artifacts"
git push origin $ARTIFACT_TAG
echo "just ran: git push origin ${ARTIFACT_TAG}.."

