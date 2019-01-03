#!/usr/bin/env bash 



start=$( cd `dirname $0` && pwd	 )
echo "initializing from ${start} "

cat ${start}/repositories.txt  | while read l ; do
 #do git clone $l ; 
 d=$( echo $l |  cut -f5 -d\/ | cut -f1 -d. ) 
 dir_to_create=${start}/../$d
 if [[ -e  $dir_to_create ]] ; then 
 	echo "WARN: ${dir_to_create} aleady exists." # if you want this to be freshly initialized then check in your work and delete the directory 
 else 
 	
 	echo "initializing ${dir_to_create}"  
 	git clone ${l} ${dir_to_create}
 	
 fi
 
done 