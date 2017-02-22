#! /bin/bash
basedir=$(cd "$(dirname "$0")"; pwd)
export TRANSDATA_HOME=$(cd "$(dirname "$basedir")"; pwd)
export CLASSPATH=$TRANSDATA_HOME:$TRANSDATA_HOME/conf:$TRANSDATA_HOME/lib
for file in `find -L $TRANSDATA_HOME -name  "*"`
do
	export CLASSPATH=$CLASSPATH:$file
done

java -Xms1000m -Xmx1000m $@ 2>&1


