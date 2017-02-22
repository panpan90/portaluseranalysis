#! /bin/sh

param=$@

while [ "$1" != "" ];do
    if [ "$1" == "-c" ];then
        shift
        clazzName=`echo $1 | awk -F'.' '{print $NF}'`
    fi

    if [ "$1" == "-d" ];then
        shift
        dt=$1
    fi
    
    shift
done

if [ "$dt" == "" ];then
   dt=`date +%Y%m%d -d "-1days"`
fi


${HADOOP} jar ${JAR} com.netease.jurassic.hadoopjob.RunMRJob -force $param 2>&1
status=$?

if [ $status -ne 0 ];then
	echo $param"##"$clazzName"##"$status >> ${TAG_Dir}/${MOUDLE_NAME}_$dt
fi