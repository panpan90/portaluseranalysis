#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

beforeRunMRMoudle userProfile_Wap
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.statistics.userProfile.wap.NeedColumnZyLogMR -d $yesterday \
     -input /ntes_weblog/commonData/devilfishLogClassification/wap/$yesterday \
     -output /ntes_weblog/weblog/statistics/userProfile/temp/needColumnFormZy4Wap/$yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.statistics.userProfile.wap.ActionPerUserWapMR -d $yesterday
sh $baseDir/../runMRJob.sh -c com.netease.portalUserAnalysis.statistics.userProfile.wap.CombineWapStatisticInfoMR -d $yesterday
afterRunMRMoudle userProfile_Wap 3 600 

if [ "$errorList" != "" ];then
    errorAlarm editorEvaluation_Wap:$errorList
fi

