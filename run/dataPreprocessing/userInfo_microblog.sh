#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.userProfile.dataPreprocessing.MicroblogUserInfoCleaningMR -d $yesterday





 








