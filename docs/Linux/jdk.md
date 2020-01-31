# Linux下JDK的安装

>**系统环境**：centos 7.6
>
>**JDK 版本**：jdk 1.8.0_191

### 1.以root用户身份先创建组和用户

```bash
[root@xk1181259634]# groupadd [-g 1124] hadoopenv
```

`cat /etc/group`

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/Linux/JDK/group.png"/> </div>

```bash
[root@xk1181259634]# useradd -m [-u 1124] -g hadoopenv xiaokang
```

`cat /etc/passwd`

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/Linux/JDK/user-xiaokang.png"/> </div>

### 2.让刚创建的普通用户xiaokang拥有超级管理员权限

`ll /etc/sudoers`(该文件**默认权限440**)

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/Linux/JDK/sudoers.png"/> </div>

修改该文件权限并进行内容的修改

```bash
[root@xk1181259634]# chomd 640 /etc/sudoers
```

`ll /etc/sudoers`

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/Linux/JDK/sudoers-1.png"/> </div>

```bash
[root@xk1181259634]# vim /etc/sudoers
```

`xiaokang        ALL=(ALL)       NOPASSWD:ALL`

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/Linux/JDK/sudoers-xiaokang.png"/> </div>

最后将该文件权限改回默认

```bash
[root@xk1181259634]# chomd 440 /etc/sudoers
```

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/Linux/JDK/default.png"/> </div>

### 3. 下载并解压

以下所有操作都以xiaokang用户的身份执行，在[官网](https://www.oracle.com/technetwork/java/javase/downloads/index.html) 下载所需版本的 JDK，这里我下载的版本为jdk 1.8.0_191 ,下载后进行解压(先创建moudle文件夹)：

```bash
[root@xk1181259634]# su xiaokang
```

```bash
[xiaokang@xk1181259634 ~]$ sudo mkdir -p /opt/moudle
```

```shell
[xiaokang@xk1181259634 ~]$ sudo tar -zxvf jdk-8u191-linux-x64.tar.gz -C /opt/moudle/
```

### 4. 设置环境变量

```shell
[xiaokang@xk1181259634 ~]$ sudo vim /etc/profile
```

输入G定位到文件最后，添加如下配置：

```shell
export JAVA_HOME=/opt/moudle/jdk1.8.0_191
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
```

执行 `source` 命令，使得配置立即生效：

```shell
[xiaokang@xk1181259634 ~]$ source /etc/profile
```

### 5. 检查是否安装成功

```shell
[xiaokang@xk1181259634 ~]$ java -version
[xiaokang@xk1181259634 ~]$ which java
```

显示出对应的版本信息和命令位置则代表安装成功。

```shell
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)

/opt/moudle/jdk1.8.0_191/bin/java
```

