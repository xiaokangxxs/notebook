#!/bin/bash

#作者：小康
#描述：ha-flink集群（yarn模式）启动停止以及进程查看脚本
#微信公众号：小康新鲜事儿

USAGE="使用方法：sh ha-flink.sh start/stop/status"
if [ $# -ne 1 ];then
	echo $USAGE
	exit 1
fi

SHELL_CALL=/home/xiaokang/bin/call-cluster.sh
SHELL_HADOOP=/home/xiaokang/bin/ha-hadoop.sh
FLINK_HOME=/opt/software/flink-1.10.1
NODES=("hadoop01" "hadoop02")

case $1 in
"start")
	#开始启动ha-flink集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE启动ha-flink集群--------"
		if [ "hadoop01" = $NODE  ];then
			ssh $NODE "$SHELL_HADOOP start && $FLINK_HOME/bin/start-cluster.sh && $FLINK_HOME/bin/historyserver.sh start"
		fi
	done
	;;
"stop")
	#开始停止ha-flink集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE停止ha-flink集群--------"
		if [ "hadoop01" = $NODE  ];then
			ssh $NODE "$FLINK_HOME/bin/stop-cluster.sh && $FLINK_HOME/bin/historyserver.sh stop"
		fi
	done
	#停止ha-hadoop集群以及zookeeper集群
	$SHELL_HADOOP stop
	;;
"status")
	echo "--------查看ha-flink集群进程信息"
	$SHELL_CALL jps
	;;
*)
	echo $USAGE
	;;
esac
echo "----------------------------------------------------------------------------------------"
echo "--------ha-flink.sh脚本执行完成!--------"
echo -e "--------微信公众号：\033[5;31m 小康新鲜事儿 \033[0m--------"
echo "--------小康老师微信：k1583223--------"
echo "--------公众号内回复【大数据】，获取系列教程及随堂文档--------"
echo "----------------------------------------------------------------------------------------"