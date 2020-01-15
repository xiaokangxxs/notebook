# Linux下JDK的安装

>**系统环境**：centos 7.6
>
>**JDK 版本**：jdk 1.8.0_191

### 1. 下载并解压

在[官网](https://www.oracle.com/technetwork/java/javase/downloads/index.html) 下载所需版本的 JDK，这里我下载的版本为jdk 1.8.0_191 ,下载后进行解压(先创建java文件夹)：

```bash
[root@xk1181259634]# mkdir -p /usr/local/src/java
```

```shell
[root@xk1181259634]# tar -zxvf jdk-8u191-linux-x64.tar.gz -C /usr/local/src/java
```

### 2. 设置环境变量

```shell
[root@xk1181259634]# vim /etc/profile
```

输入G定位到文件最后，添加如下配置：

```shell
export JAVA_HOME=/usr/local/src/java/jdk1.8.0_191
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
```

执行 `source` 命令，使得配置立即生效：

```shell
[root@xk1181259634]# source /etc/profile
```

### 3. 检查是否安装成功

```shell
[root@xk1181259634]# java -version
```

显示出对应的版本信息则代表安装成功。

```shell
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)
```

