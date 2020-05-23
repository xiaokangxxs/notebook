#!/bin/bash

#作者：小康
#描述：Kafka集群启动停止以及查看状态脚本
#微信公众号：小康新鲜事儿

USAGE="使用方法：sh kf.sh start/stop/status"
if [ $# -ne 1 ];then
	echo $USAGE
	exit 1
fi
SHELL_ZK=/home/xiaokang/bin/zk.sh
SHELL_CALL=/home/xiaokang/bin/call-cluster.sh
KAFKA_HOME=/opt/software/kafka-2.4.1
NODES=("hadoop01" "hadoop02" "hadoop03")
case $1 in
"start")
	#启动zookeeper集群
	$SHELL_ZK start
	sleep 4s	
	#开始启动kafka集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE启动kafka集群--------"
		ssh $NODE "$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties"	
	done
	;;
"stop")
	#开始停止kafka集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE停止kafka集群--------"
		ssh $NODE "$KAFKA_HOME/bin//kafka-server-stop.sh"	
	done
	#kafka停止比较慢,延迟几秒再停止zookeeper集群,不然zookeeper集群停止后kafka进程就无法停止了
    sleep 11s
	#停止zookeeper集群
	$SHELL_ZK stop
	;;
"status")
	echo "--------查看kafka集群状态--------"
	$SHELL_CALL jps
	;;
*)
	echo $USAGE 
	;;
esac
echo "----------------------------------------------------------------------------------------"
echo "--------kf.sh脚本执行完成!--------"
echo -e "--------微信公众号：\033[5;31m 小康新鲜事儿 \033[0m--------"
echo "--------小康老师微信：k1583223--------"
echo "--------公众号内回复【大数据】，获取系列教程及随堂文档--------"
echo "----------------------------------------------------------------------------------------"