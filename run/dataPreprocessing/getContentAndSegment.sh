#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

curDir=/home/weblog/portalUserAnalysis/data/article/$yesterday

if [ ! -d $curDir ];then
	mkdir -p $curDir
fi

${HADOOP} fs -text /ntes_weblog/commonData/articleIncr/$yesterday/* | awk -F'\t' '{print $1}' > $curDir"/docids"

sh $baseDir/getArticleContent.sh $yesterday
sh $baseDir/articleContentSegment.sh $yesterday





 








