#!/bin/bash

#作者：小康
#描述：flume启动停止以及查看状态脚本
#微信公众号：小康新鲜事儿

USAGE="使用方法：sh fl.sh start/stop/status"
if [ $# -ne 1 ];then
	echo $USAGE
	exit 1
fi
SHELL_CALL=/home/xiaokang/bin/call-cluster.sh
FLUME_HOME=/opt/software/flume-1.9.0
AGENT_NAME=a1
CONF_FILE=/home/xiaokang/file-flume-kafka.properties
NODES=("hadoop01")
case $1 in
"start")
	#启动flume
	for NODE in ${NODES[*]};do
		echo "--------$NODE启动flume--------"
		#ssh $NODE "nohup $FLUME_HOME/bin/flume-ng agent -n $AGENT_NAME -c $FLUME_HOME/conf -f $CONF_FILE -Dflume.root.logger=INFO,LOGFILE >$FLUME_HOME/flume-run.log 2>&1 &"
		ssh $NODE "nohup $FLUME_HOME/bin/flume-ng agent -n $AGENT_NAME -c $FLUME_HOME/conf -f $CONF_FILE >/dev/null 2>&1 &"	
	done
	;;
"stop")
	#停止flume
	for NODE in ${NODES[*]};do
		echo "--------$NODE停止flume,不要着急查看状态,停止较慢--------"
		ssh $NODE "ps -ef | grep $CONF_FILE | grep -v grep | awk '{print \$2}' | xargs kill"	
	done
	;;
"status")
	echo "--------查看flume状态--------"
	$SHELL_CALL jps -l
	;;
*)
	echo $USAGE 
	;;
esac
echo "----------------------------------------------------------------------------------------"
echo "--------fl.sh脚本执行完成!--------"
echo -e "--------微信公众号：\033[5;31m 小康新鲜事儿 \033[0m--------"
echo "--------小康老师微信：k1583223--------"
echo "--------公众号内回复【大数据】，获取系列教程及随堂文档--------"
echo "----------------------------------------------------------------------------------------"