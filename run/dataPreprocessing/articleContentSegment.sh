#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

sh $baseDir/../runCommand.sh com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.ArticleSegmentor /home/weblog/portalUserAnalysis/data/article/$yesterday ${WORDSEGMENT_LIB_DIR}





 








