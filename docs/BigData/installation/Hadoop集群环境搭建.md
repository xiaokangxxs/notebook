<div align="center"> <img width="600px" src="ht

# Hadoop集群环境搭建


## 一、集群规划

这里搭建一个 3 节点的 Hadoop 集群，其中三台主机均部署 `DataNode` 和 `NodeManager` 服务，但其中 hadoop02 上部署 `SecondaryNameNode`服务，hadoop01 上部署 `NameNode` 、 `ResourceManager` 和`JobHistoryServer` 服务。

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/cluster.png"/> </div>

节点ip分配情况如下：

| hostname | ip              |
| -------- | --------------- |
| hadoop01 | 192.168.239.161 |
| hadoop02 | 192.168.239.162 |
| hadoop03 | 192.168.239.163 |

## 二、前置条件

Hadoop 的运行依赖 JDK，需要预先安装。其安装步骤见：

+ [Linux下jdk的安装](https://xiaokangxxs.github.io/notebook/#/Linux/jdk)

## 三、集群配置

### 3.1 主节点基础网络配置

- 固定IP地址
- 配置主机名
- 关闭防火墙

参考视频教程：[Hadoop前置准备](https://mp.weixin.qq.com/s/sTcyhn5hCYD6ThqZOR6g1Q)

### 3.2 配置映射

配置 ip 地址和主机名映射：

```shell
[xiaokang@hadoop01 ~]$ sudo vim /etc/hosts
# 文件末尾增加
192.168.239.161 hadoop01
192.168.239.162 hadoop02
192.168.239.163 hadoop03
```

### 3.3 根据主节点hadoop01克隆两份系统

将hadoop01先关机，然后鼠标右击管理->克隆->创建完整克隆->填写名称、选择存放位置即可

### 3.4 修改各个从节点网络配置

修改IP地址和主机名

```shell
[xiaokang@hadoop01 ~]$ sudo vim /etc/sysconfig/network-scripts/ifcfg-ens33
#将原来的192.168.239.161改为192.168.239.162

#重启网络服务生效
[xiaokang@hadoop01 ~]$ sudo systemctl restart network
```

IP修改完成后，使用XShell进行连接，之后再进行主机名的修改

```shell
[xiaokang@hadoop01 ~]$ sudo hostname hadoop02

[xiaokang@hadoop01 ~]$ sudo vim /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=hadoop02

[xiaokang@hadoop01 ~]$ sudo vim /etc/hostname
#将原来的hadoop01改为hadoop02
```

退出当前XSell连接，再次登录查看是否配置成功

### 3.5 配置主从节点免密登录

#### 3.5.1 生成密匙

在**每台主机**上使用 `ssh-keygen` 命令生成公钥私钥对：

```shell
[xiaokang@hadoop01 ~]$ ssh-keygen -t rsa -C "xiaokang.188@qq.com"
```

#### 3.5.2 复制公钥

将 `hadoop01` 的公钥写到本机和远程机器的 ` ~/ .ssh/authorized_key` 文件中(另外两台机器上需要做同样的动作)：

```shell
[xiaokang@hadoop01 .ssh]$ ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop01
[xiaokang@hadoop01 .ssh]$ ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop02
[xiaokang@hadoop01 .ssh]$ ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop03
```

#### 3.5.3 验证免密登录

```she
ssh hadoop01
ssh hadoop02
ssh hadoop03
```

#### 3.5.4 Gif动图演示

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/sshpwd.gif"/> </div>

### 3.6 同步集群时间

关于时间可以采用`sudo date -s "2020-11-24 11:24:00"`或`sudo ntpdate -u ntp.api.bz`进行设置，但我们一般会采用单独设置一台时间服务器，让其它所有节点与时间服务器的时间进行同步。

#### 3.6.1 配置时间服务器（hadoop02）

1. 检查ntp包是否安装

   ```shell
   [xiaokang@hadoop02 ~]$ rpm -qa | grep ntp
   ntpdate-4.2.6p5-29.el7.centos.x86_64
   ntp-4.2.6p5-29.el7.centos.x86_64
   ```

   没有安装的话，执行以下命令进行安装：

   ```shell
   [xiaokang@hadoop02 root]$ sudo yum -y install ntp
   ```

2. 设置时间配置文件

   ```shell
   [xiaokang@hadoop02 ~]$ sudo vim /etc/ntp.conf
   
   ```

   ```properties
   #修改一（设置本地网络上的主机不受限制）
   #restrict 192.168.1.0 mask 255.255.255.0 nomodify notrap 为
   restrict 192.168.239.0 mask 255.255.255.0 nomodify notrap
   #修改二（添加默认的一个内部时钟数据，使用它为局域网用户提供服务）
   server 127.127.1.0
   fudge 127.127.1.0 stratum 10
   #修改三（设置为不采用公共的服务器）
   server 0.centos.pool.ntp.org iburst
   server 1.centos.pool.ntp.org iburst
   server 2.centos.pool.ntp.org iburst
   server 3.centos.pool.ntp.org iburst 为
   #server 0.centos.pool.ntp.org iburst
   #server 1.centos.pool.ntp.org iburst
   #server 2.centos.pool.ntp.org iburst
   #server 3.centos.pool.ntp.org iburst
   
   ```

3. 设置BIOS与系统时间同步

   ```shell
   [xiaokang@hadoop02 ~]$ sudo vim /etc/sysconfig/ntpd
   
   ```

   ```shell
   #增加如下内容（让硬件时间与系统时间一起同步）
   OPTIONS="-u ntp:ntp -p /var/run/ntpd.pid -g"
   SYNC_HWCLOCK=yes
   
   ```

4. 启动ntp服务并测试

   ```shell
   [xiaokang@hadoop02 ~]$ sudo systemctl start ntpd
   [xiaokang@hadoop02 ~]$ systemctl status ntpd
   #设置ntp服务开机自启
   [xiaokang@hadoop02 ~]$ sudo systemctl enable ntpd.service
   
   #测试
   [xiaokang@hadoop02 ~]$ ntpstat
   synchronised to local net (127.127.1.0) at stratum 11
      time correct to within 3948 ms
      polling server every 64 s
   [xiaokang@hadoop02 ~]$ sudo ntpq -p
        remote           refid      st t when poll reach   delay   offset  jitter
   ==============================================================================
   *LOCAL(0)        .LOCL.          10 l   26   64    3    0.000    0.000   0.000
   
   ```

#### 3.6.2 其它节点与时间服务器同步时间

先关闭非时间服务器节点的ntpd服务`sudo systemctl stop ntpd`

1）手动同步

```shell
[xiaokang@hadoop03 ~]$ sudo ntpdate hadoop02
24 Nov 11:25:13 ntpdate[2878]: step time server 192.168.239.125 offset 24520304.363894 sec
[xiaokang@hadoop03 ~]$ date 
2020年 11月 24日 星期二 11:25:20 CST

```

2）定时同步

1. 在其他机器配置10分钟与时间服务器同步一次

   ```shell
   [xiaokang@hadoop03 root]$ sudo vim /etc/crontab
   
   ```

    ```properties
   编写定时任务如下：
   */1 * * * * /usr/sbin/ntpdate hadoop02
   
    ```

    ```shell
   #加载任务,使之生效
   [xiaokang@hadoop03 ~]$ sudo crontab /etc/crontab
   
    ```

2. 修改时间服务器时间

   ```shell
   [xiaokang@hadoop02 ~]$ sudo date -s "2020-11-24 11:24:11"
   
   ```

3. 十分钟后查看机器是否与时间服务器同步

   ```shell
   [xiaokang@hadoop03 ~]$ date
   
   ```

   `ps：测试的时候可以将10分钟调整为1分钟，节省时间`

### 3.7 修改主节点配置文件

先创建好所需目录

```shell
[xiaokang@hadoop01 hadoop-2.7.7]$ mkdir /opt/software/hadoop-2.7.7/tmp
[xiaokang@hadoop01 hadoop-2.7.7]$ mkdir -p /opt/software/hadoop-2.7.7/dfs/namenode_data
[xiaokang@hadoop01 hadoop-2.7.7]$ mkdir -p /opt/software/hadoop-2.7.7/dfs/datanode_data
[xiaokang@hadoop01 hadoop-2.7.7]$ mkdir -p /opt/software/hadoop-2.7.7/checkpoint/dfs/cname

```

#### 1. hadoop-env.sh

```shell
#25行 export JAVA_HOME
export JAVA_HOME=/opt/moudle/jdk1.8.0_191
#33行 export HADOOP_CONF_DIR
export HADOOP_CONF_DIR=/opt/software/hadoop-2.7.7/etc/hadoop

```

#### 2. core-site.xml

```xml
<configuration>
        <property>
            <!--用来指定hdfs的老大，namenode的地址-->
            <name>fs.defaultFS</name>
            <value>hdfs://hadoop01:9000</value>
		</property>  
		<property>
            <!--用来指定hadoop运行时产生文件的存放目录-->   
            <name>hadoop.tmp.dir</name>
            <value>file:///opt/software/hadoop-2.7.7/tmp</value>
		</property>
		<property>
			<!--设置缓存大小，默认4kb-->
	        <name>io.file.buffer.size</name>
	        <value>4096</value>
        </property>
</configuration>

```

#### 3. hdfs-site.xml

```xml
<configuration>
        <property>
	       <!--数据块默认大小128M-->
	       <name>dfs.block.size</name>
	       <value>134217728</value>
        </property>
        <property>
            <!--副本数量，不配置的话默认为3-->
            <name>dfs.replication</name> 
            <value>3</value>
        </property>
        <property>
            <!--定点检查--> 
            <name>fs.checkpoint.dir</name>
            <value>file:///opt/software/hadoop-2.7.7/checkpoint/dfs/cname</value>
        </property>
        <property>
            <!--namenode节点数据（元数据）的存放位置-->
            <name>dfs.name.dir</name> 
            <value>file:///opt/software/hadoop-2.7.7/dfs/namenode_data</value>
        </property>
        <property>
            <!--datanode节点数据（元数据）的存放位置-->
            <name>dfs.data.dir</name> 
            <value>file:///opt/software/hadoop-2.7.7/dfs/datanode_data</value>
        </property>
        <property>
            <!--指定secondarynamenode的web地址-->
            <name>dfs.namenode.secondary.http-address</name> 
            <value>hadoop02:50090</value>
        </property>
        <property>
            <!--hdfs文件操作权限,false为不验证-->
            <name>dfs.permissions</name> 
            <value>false</value>
        </property>
</configuration>

```

#### 4. mapred-site.xml

```xml
<configuration>
        <property>  
            <!--指定mapreduce运行在yarn上-->
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
        <property>
            <!--配置任务历史服务器IPC-->
            <name>mapreduce.jobhistory.address</name>
            <value>hadoop01:10020</value>
        </property>
        <property>
            <!--配置任务历史服务器web-UI地址-->
            <name>mapreduce.jobhistory.webapp.address</name>
            <value>hadoop01:19888</value>
        </property>
</configuration>

```

#### 5. yarn-site.xml

```xml
<configuration>
        <property>
            <!--指定yarn的老大resourcemanager的地址-->
            <name>yarn.resourcemanager.hostname</name>
            <value>hadoop01</value>
        </property>
        <property>
            <name>yarn.resourcemanager.address</name>
            <value>hadoop01:8032</value>
        </property>
        <property>
            <name>yarn.resourcemanager.webapp.address</name>
            <value>hadoop01:8088</value>
        </property>
        <property>
            <name>yarn.resourcemanager.scheduler.address</name>
            <value>hadoop01:8030</value>
        </property>
        <property>
            <name>yarn.resourcemanager.resource-tracker.address</name>
            <value>hadoop01:8031</value>
        </property>
        <property>
            <name>yarn.resourcemanager.admin.address</name>
            <value>hadoop01:8033</value>
        </property>
        <property>
            <!--NodeManager获取数据的方式-->
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
        <property>
            <!--开启日志聚集功能-->
            <name>yarn.log-aggregation-enable</name>
            <value>true</value>
        </property>
        <property>
            <!--配置日志保留7天-->
            <name>yarn.log-aggregation.retain-seconds</name>
            <value>604800</value>
        </property>
</configuration>

```

#### 6. master

在当前配置文件目录内是不存在master文件的，我们使用**vim**写入内容到master内保存即可

```shell
[xiaokang@hadoop01 hadoop]$ vim master
hadoop01

```

#### 7. slaves

配置所有从属节点的主机名或 IP 地址，每行一个。所有从属节点上的 `DataNode` 服务和 `NodeManager` 服务都会被启动。

```properties
hadoop01
hadoop02
hadoop03

```

### 3.8 分发程序

将 Hadoop 安装包分发到其他两台服务器，分发后建议在这两台服务器上也配置一下 Hadoop 的环境变量。

```shell
# 将安装包分发到hadoop002
[xiaokang@hadoop01 hadoop]$ sudo scp -r /opt/software/hadoop-2.7.7/ xiaokang@hadoop02:/opt/software/
# 将安装包分发到hadoop003
[xiaokang@hadoop01 hadoop]$ sudo scp -r /opt/software/hadoop-2.7.7/ xiaokang@hadoop03:/opt/software/

```

shell脚本实现

```shell
scp-config.sh
#!/bin/bash
#description：节点间复制文件
#author：xiaokang

#首先判断参数是否存在
args=$#
if [ args -eq 0 ];then
	echo "no args"
	exit 1
fi
#获取文件名称
p1=$1
fname=$(basename $p1)
echo faname=$fname
#获取上级目录到绝对路径
pdir=$(cd $(dirname $p1);pwd -P)
echo pdir=$pdir
#获取当前用户名称
user=$(whoami)
#循环分发
for(( host=2;host<4;host++ ));do
	echo "------hadoop0$host------"
	scp -r $pdir/$fname $user@hadoop0$host:$pdir
done
echo "------分发完成------"

```

### 3.9 初始化

```shell
[xiaokang@hadoop01 ~]$ hdfs namenode -format

```

## 四、启动集群

在 `hadoop01` 上启动 Hadoop。此时 `hadoop02` 和 `hadoop03` 上的相关服务也会被启动：

```shell
# 启动dfs服务
[xiaokang@hadoop01 ~]$ start-dfs.sh
# 启动yarn服务
[xiaokang@hadoop01 ~]$ start-yarn.sh
# 启动任务历史服务器
[xiaokang@hadoop01 ~]$ mr-jobhistory-daemon.sh start historyserver

```

### 4.1 查看集群

在每台服务器上使用 `jps` 命令查看服务进程，或直接进入 Web-UI 界面进行查看，端口为 `50070`。可以看到此时有三个可用的 `Datanode`：

```shell
[xiaokang@hadoop01 ~]$ jps
2804 NameNode
3684 Jps
3302 NodeManager
3194 ResourceManager
2939 DataNode
3613 JobHistoryServer

[xiaokang@hadoop02 ~]$ jps
8771 SecondaryNameNode
9116 Jps
8958 NodeManager

[xiaokang@hadoop03 hadoop-2.7.7]$ jps
3410 Jps
3285 NodeManager

```

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/liveNodes.png"/> </div>

<br/>

点击 `Live Nodes` 进入，可以看到每个 `DataNode` 的详细情况：

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/dataNodes.png"/> </div>

<br/>

接着可以查看 Yarn 的情况，端口号为 `8088` ：

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/yarn-cluster.png"/> </div>

## 五、提交服务到集群

提交作业到集群的方式和单机环境完全一致，这里以提交 Hadoop 内置的计算 Pi 的示例程序为例，在任何一个节点上执行都可以，命令如下：

```shell
#第1个11指的是要运行11次map任务 
#第2个数字指的是每个map任务，要投掷多少次 
[xiaokang@hadoop03 ~]$ hadoop jar /opt/software/hadoop-2.7.7/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.7.jar pi 11 24
```

Web-UI界面刚才执行的任务状况：

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/mapreduce-pi.png"/> </div>

最终计算结果：

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/mapreduce-result.png"/> </div>

tps://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/mapreduce-result.png"/> </div>