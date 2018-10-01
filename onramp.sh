#!/bin/bash 


D=`pwd`/book 

if [ ! -d $D ] ; then 
	git clone https://${RSB_GITHUB_TOKEN}@github.com/joshlong/reactive-spring-book.git $D 
	cd $D  
fi 


cd book 
git pull 



