#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

beforeRunMRMoudle userProfile_Gentie
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.statistics.userProfile.GenTiePerUserMR -d $yesterday
afterRunMRMoudle userProfile_Gentie 3 600 

if [ "$errorList" != "" ];then
    errorAlarm editorEvaluation_Wap:$errorList
fi

