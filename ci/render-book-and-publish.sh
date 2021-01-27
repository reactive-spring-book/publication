#!/usr/bin/env bash

START_DIR=$(pwd)
echo "starting in ${START_DIR}."
INIT_DATE=`date`

OUT_ROOT=${HOME}/book-ci-root
BOOK_CHECKOUT=${OUT_ROOT}/docs
CODE_CHECKOUT=${OUT_ROOT}/code
RSB_URI=https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git

rm -rf $BOOK_CHECKOUT

## GET THE REPOSITORIES
if [ ! -d "$BOOK_CHECKOUT" ]; then
    ## get the Asciidoc book
    mkdir -p ${BOOK_CHECKOUT}
    cd $BOOK_CHECKOUT
    git clone ${RSB_URI} $BOOK_CHECKOUT || echo "couldn't clone the book from ${RSB_URI}."
fi

if [ ! -d "$CODE_CHECKOUT" ]; then
    ## get the supporting code
    mkdir -p ${CODE_CHECKOUT}
    cd $CODE_CHECKOUT
    cat $START_DIR/ci/repositories.txt | while read l ; do
     echo "---------------------------------------"
     echo "cloning ${l}"
     git clone ${l}
    done
fi

## PRODUCE THE BOOK ARTIFACTS
cd $START_DIR
export KINDLEGEN=${HOME}/bin/kindlegen/kindlegen

KINDLEGEN_DIRNAME=$(dirname $KINDLEGEN)

#echo "going to reset $KINDLEGEN_DIRNAME"
rm -rf $KINDLEGEN_DIRNAME
mkdir -p $KINDLEGEN_DIRNAME
export PATH=$KINDLEGEN_DIRNAME:$PATH

export PUBLICATION_ROOT=${BOOK_CHECKOUT}/src/docs/asciidoc
export PUBLICATION_TARGET=${START_DIR}/target/book-output
export PUBLICATION_CODE=${CODE_CHECKOUT}

java -jar ${START_DIR}/target/production-0.0.1-SNAPSHOT.jar

if [ -d "$PUBLICATION_TARGET" ]; then
    cd $BOOK_CHECKOUT
    ARTIFACT_TAG=output-artifacts
    git remote set-url origin $RSB_URI
    git checkout $ARTIFACT_TAG

    OUTPUT=${BOOK_CHECKOUT}/output
    mkdir -p $OUTPUT
    cp -r $PUBLICATION_TARGET/* $OUTPUT
    git add $OUTPUT
    git commit -am "adding built artifacts $(date)..."
    git push origin $ARTIFACT_TAG
fi
