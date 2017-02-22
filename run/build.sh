##第一个参数是产品版本(默认是master)
## build             (默认为 build master)
## build master

if [ $# == 1 ]; then
	branch=$1
else
	branch=master
fi

basedir=$(cd "$(dirname "$0")"; pwd)
echo $basedir
cd $basedir
cd ..

mvn clean
git pull https://git.ws.netease.com/jngao/portaluseranalysis.git ${branch}
mvn package

installTime=`date +%Y%m%d-%H:%M`

cd ../../

installDir=./release/$installTime
if [ -d $installDir ];then
	rm -r $installDir
fi
mkdir -p $installDir

cp $basedir/../target/*.jar $installDir
cp -r $basedir/../target/classes/* $installDir

runDir=./portalUserAnalysis
if [ -h $runDir ];then
	rm $runDir
fi

ln -s $installDir $runDir

