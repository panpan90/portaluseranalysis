#! /bin/sh

if [ $# == 1 ]; then
    dt=$1
    model=online
elif [ $# == 2 ]; then
	dt=$1
	model=$2
else
	dt=`date +%Y%m%d -d "-1days"`
	model=online
fi

yesterday=`echo $dt | awk '{gsub("-","",$0);print $0}'`
yest=`date +%Y-%m-%d -d $yesterday`

echo yesterday = $yesterday

export JAVA_HOME=${JAVA_HOME}
export HADOOP_HOME=${HADOOP_HOME}
export HADOOP_CONF_DIR=${HADOOP_CONF_DIR}

function printTimeStamp(){
	echo `date "+%Y-%m-%d-%H:%M:%S"` $@
}

#参数：消息
function errorAlarm(){
	message=$1
	completeMessage=[Error]_channelAnalysis_${yesterday}_${message}
	echo completeMessage
	sh /home/weblog/monitorfile/alarm.sh $completeMessage /home/weblog/monitorfile/contactlist.txt
}

function waringAlarm(){
	message=$1
	completeMessage=[Waring]_channelAnalysis_${yesterday}_${message}
	echo completeMessage
	sh /home/weblog/monitorfile/alarm.sh $completeMessage /home/weblog/monitorfile/contactlist.txt
}

#参数：模块名
#beforeRunMRMoudle genTie
function beforeRunMRMoudle(){
	if [ ! -d ${TAG_Dir} ];then
		mkdir -p ${TAG_Dir}
	fi
	moudleName=$1
	export MOUDLE_NAME=$moudleName
	rFile=${TAG_Dir}/${moudleName}_$yesterday
	if [ -f $rFile ];then
	    rm $rFile
	fi
}

#参数：模块名, 重复执行次数, 执行间隔(秒)
#afterRunMRMoudle genTie 3 10
function afterRunMRMoudle(){
	moudleName=$1
	retry=$2
	interval=$3
	rFile=${TAG_Dir}/${moudleName}_$yesterday
	
	#loop
	while [ -f $rFile -a $retry -ge 1 ];do
		sleep $interval"s"
	    now=`date "+%Y%m%d%H%M%S"`
	    tFile=$rFile.$now
	    mv $rFile $tFile
	    while read line;do
	        echo "retry: "$line
	        sh ${RUN_DIR}/runMRJob.sh `echo $line | awk -F'##' '{print $1}'`
	    done < $tFile
	    retry=$((retry-1))
	done
	
	errorList=""
	
	if [ -f $rFile ];then
		now=`date "+%Y%m%d%H%M%S"`
	    tFile=$rFile.$now.error
	    mv $rFile $tFile
	    while read line;do
	        if [ "$errorList" == "" ];then
	            errorList=`echo $line | awk -F'##' '{print $2}'`
	        else
	            errorList=$errorList"/"`echo $line | awk -F'##' '{print $2}'`
	        fi
	    done < $tFile
	fi
	
	printTimeStamp "$moudleName end"
}

function hadoopDirPrepare(){
	hadoopDir=$1
	dt=$2
	now=`date "+%H%M%S"`
	hadoopDateDirExist=`${HADOOP} fs -ls $hadoopDir |awk -F'/' '{print $NF}'|grep -v _ |grep -c $dt`
	if [ $hadoopDateDirExist -eq 0 ];then
		${HADOOP} fs -mkdir -p $hadoopDir/$dt
    else 
        ${HADOOP} fs -mkdir -p $hadoopDir/$dt"_"$now
	    ${HADOOP} fs -mv  $hadoopDir/$dt/* $hadoopDir/$dt"_"$now/

	fi
}