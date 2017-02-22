#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

beforeRunMRMoudle userProfile_Web
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.statistics.userProfile.web.ActionPerUserWebMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.statistics.userProfile.web.CombineWebStatisticInfoMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.WebInfoResultMR -d $yesterday

afterRunMRMoudle userProfile_Web 3 600 

if [ "$errorList" != "" ];then
    errorAlarm editorEvaluation_Wap:$errorList
fi

