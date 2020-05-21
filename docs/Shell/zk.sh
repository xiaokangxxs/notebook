#!/bin/bash

#作者：小康
#描述：zookeeper集群启动/停止/查看状态脚本
#微信公众号：小康新鲜事儿

USAGE="使用方法：sh zk.sh start/stop/status"
if [ $# -ne 1 ];then
        echo $USAGE
        exit 1
fi
NODES=("hadoop01" "hadoop02" "hadoop03")
ZOOKEEPER_HOME=/opt/software/zookeeper-3.5.7
case $1 in
"start")
        for NODE in ${NODES[*]};do
                echo "--------$NODE启动zookeeper--------"
                ssh $NODE "$ZOOKEEPER_HOME/bin/zkServer.sh start"
        done
        ;;
"stop")
        for NODE in ${NODES[*]};do
                echo "--------$NODE停止zookeeper--------"
                ssh $NODE "$ZOOKEEPER_HOME/bin/zkServer.sh stop"
        done
        ;;
"status")
        for NODE in ${NODES[*]};do
                echo "--------$NODE查看zookeeper状态--------"
                ssh $NODE "$ZOOKEEPER_HOME/bin/zkServer.sh status"
        done
        ;;
*)
        echo $USAGE
        ;;
esac
echo "----------------------------------------------------------------------------------------"
echo "--------zk.sh脚本执行完成!--------"
echo -e "--------微信公众号：\033[5;31m 小康新鲜事儿 \033[0m--------"
echo "--------小康老师微信：k1583223--------"
echo "--------公众号内回复【大数据】，获取系列教程及随堂文档--------"
echo "----------------------------------------------------------------------------------------"