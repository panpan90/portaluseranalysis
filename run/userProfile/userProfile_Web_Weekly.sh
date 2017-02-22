#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

beforeRunMRMoudle userProfile_Web_Weekly
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.WebInfoResultPercentMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.WebInfoRandomResultPercentMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.WeeklyWebInfoResultMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.WeeklywebInfoRandomResultMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.MonthWebInfoResultMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.MonthwebInfoRandomResultMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.MonthWebInfoResultPercentMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.resultData.MonthWebInfoRandomResultPercentMR -d $yesterday
afterRunMRMoudle userProfile_Web_Weekly 3 600 

if [ "$errorList" != "" ];then
    errorAlarm editorEvaluation_Wap:$errorList
fi

