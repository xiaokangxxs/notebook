#!/bin/bash

#作者：小康
#描述：ha-spark集群启动停止以及进程查看脚本
#微信公众号：小康新鲜事儿

USAGE="使用方法：sh ha-spark.sh start/stop/status"
if [ $# -ne 1 ];then
	echo $USAGE
	exit 1
fi

SHELL_ZK=/home/xiaokang/bin/zk.sh
SHELL_CALL=/home/xiaokang/bin/call-cluster.sh
SHELL_HADOOP=/home/xiaokang/bin/ha-hadoop.sh
SPARK_HOME=/opt/software/spark-2.4.5
NODES=("hadoop01" "hadoop02")

case $1 in
"start")
	#开始启动ha-spark集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE启动ha-spark集群"
		if [ "hadoop01" = $NODE  ];then
			ssh $NODE "$SHELL_HADOOP start && $SPARK_HOME/sbin/start-all.sh && $SPARK_HOME/sbin/start-history-server.sh"
		fi
		if [ "hadoop02" = $NODE  ];then
			ssh $NODE "$SPARK_HOME/sbin/start-master.sh"
		fi
	done
	;;
"stop")
	#开始停止ha-spark集群
	for NODE in ${NODES[*]};do
		echo "--------$NODE停止ha-spark集群"
		if [ "hadoop01" = $NODE  ];then
			ssh $NODE "$SPARK_HOME/sbin/stop-all.sh && $SPARK_HOME/sbin/stop-history-server.sh"
		fi
		if [ "hadoop02" = $NODE  ];then
			ssh $NODE "$SPARK_HOME/sbin/stop-master.sh"
		fi
		#停止ha-hadoop集群以及zookeeper集群
		$SHELL_HADOOP stop
	done
	;;
"status")
	echo "--------查看ha-spark集群进程信息"
	$SHELL_CALL jps
	;;
*)
	echo $USAGE
	;;
esac
echo "----------------------------------------------------------------------------------------"
echo "--------ha-spark.sh脚本执行完成!--------"
echo -e "--------微信公众号：\033[5;31m 小康新鲜事儿 \033[0m--------"
echo "--------小康老师微信：k1583223--------"
echo "--------公众号内回复【大数据】，获取系列教程及随堂文档--------"
echo "----------------------------------------------------------------------------------------"
