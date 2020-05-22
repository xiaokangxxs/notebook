#!/bin/bash

#作者：小康
#描述：ha-hadoop集群启动停止以及查看进程脚本
#微信公众号：小康新鲜事儿

USAGE="使用方法：sh ha-hadoop.sh start/stop/status"
if [ $# -ne 1 ];then
	echo $USAGE
	exit 1
fi
SHELL_ZK=/home/xiaokang/bin/zk.sh
HADOOP_HOME=/opt/software/hadoop-2.7.7
NODES=("hadoop01" "hadoop02" "hadoop03")
case $1 in
"start")
	#启动zookeeper集群
	$SHELL_ZK start
	sleep 4s	
	#开始启动ha-hadoop集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE启动ha-hadoop集群--------"
		if [ "hadoop01" = $NODE ];then
			ssh $NODE "$HADOOP_HOME/sbin/start-dfs.sh && $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver"		
		fi
		if [ "hadoop02" = $NODE ];then
			ssh $NODE "$HADOOP_HOME/sbin/yarn-daemon.sh start resourcemanager"	
		fi
		if [ "hadoop03" = $NODE ];then
			ssh $NODE "$HADOOP_HOME/sbin/start-yarn.sh"	
		fi
	done
	;;
"stop")
	#开始停止ha-hadoop集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE停止ha-hadoop集群--------"
		if [ "hadoop01" = $NODE ];then
			ssh $NODE "$HADOOP_HOME/sbin/stop-dfs.sh && $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh stop historyserver"		
		fi
		if [ "hadoop02" = $NODE ];then
			ssh $NODE "$HADOOP_HOME/sbin/yarn-daemon.sh stop resourcemanager"	
		fi
		if [ "hadoop03" = $NODE ];then
			ssh $NODE "$HADOOP_HOME/sbin/stop-yarn.sh"	
		fi
	done
	#停止zookeeper集群
	$SHELL_ZK stop
	;;
"status")
	for NODE in ${NODES[*]};do
		echo "--------$NODE查看ha-hadoop集群进程--------"
		ssh $NODE "jps"	
	done
	;;
*)
	echo $USAGE 
	;;
esac
echo "----------------------------------------------------------------------------------------"
echo "--------ha-hadoop.sh脚本执行完成!--------"
echo -e "--------微信公众号：\033[5;31m 小康新鲜事儿 \033[0m--------"
echo "--------小康老师微信：k1583223--------"
echo "--------公众号内回复【大数据】，获取系列教程及随堂文档--------"
echo "----------------------------------------------------------------------------------------"
