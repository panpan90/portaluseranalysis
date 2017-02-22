#!/bin/bash

baseDir=$(cd "$(dirname "$0")"; pwd)

source $baseDir/../portalUserAnalysisHeader.sh

beforeRunMRMoudle userProfile_Main
##--gentie------------------------
sh $baseDir/userProfile_Gentie.sh $yesterday
userProfile_GentieStatus=$?
echo userProfile_GentieStatus=$userProfile_GentieStatus

##--web------------------------
sh $baseDir/userProfile_Web.sh $yesterday
userProfile_WebStatus=$?
echo userProfile_WebStatus=$userProfile_WebStatus

##--wap------------------------
sh $baseDir/userProfile_Wap.sh $yesterday
userProfile_WapStatus=$?
echo userProfile_WapStatus=$userProfile_WapStatus


if [ $userProfile_GentieStatus -ne 0 -o $userProfile_WebStatus -ne 0 -o $userProfile_WapStatus -ne 0 ];then
	errorAlarm userProfile_GentieStatus=${userProfile_GentieStatus},userProfile_WebStatus=${userProfile_WebStatus},userProfile_WapStatus=${userProfile_WapStatus}
	exit 1
fi
##--结果合并------------------------

afterRunMRMoudle userProfile_Main 3 600 
if [ "$errorList" != "" ];then
	errorAlarm userProfile_Main:$errorList
fi





 








