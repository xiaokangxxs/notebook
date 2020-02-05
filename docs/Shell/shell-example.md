# 实战Shell脚本

## 1. 获取随机字符串或数字

### 获取随机8位字符串

```shell
#方法一
[root@hadoop01 ~]# echo $RANDOM | md5sum | cut -c 1-8
45a0e047
#方法二
[root@hadoop01 ~]# openssl rand -base64 4
TlgZ9Q==
#方法三
[root@hadoop01 ~]# cat /proc/sys/kernel/random/uuid |cut -c 1-8
8584c8cd
```

### 获取随机8位数字

` cksum：打印CRC效验和统计字节 `

```shell
#方法一
[root@hadoop01 ~]# echo $RANDOM | cksum | cut -c 1-8
61673158
#方法二
[root@hadoop01 ~]# openssl rand -base64 4 | cksum | cut -c 1-8
19479123
#方法三
[root@hadoop01 ~]# date +%N | cut -c 1-8
60615424
```

## 2.定义一个颜色输出字符串函数

```shell
#方式一
function echo_color() {
    if [ $1 == "green" ]; then
        echo -e "\033[32m$2\033[0m"
    elif [ $1 == "red" ]; then
        echo -e "\033[31m$2\033[0m"
    elif [ $1 == "yellow" ]; then
        echo -e "\033[33m$2\033[0m"
    elif [ $1 == "blue" ]; then
        echo -e "\033[34m$2\033[0m"
    elif [ $1 == "purple" ]; then
        echo -e "\033[35m$2\033[0m"
    fi
}
#方式二
function echo_color() {
    case $1 in
        green)
            echo -e "\033[32m$2\033[0m"
            ;;
        red)
            echo -e "\033[31m$2\033[0m"
            ;;
        yellow)
            echo -e "\033[33m$2\033[0m"
            ;;
        blue)
            echo -e "\033[34m$2\033[0m"
            ;;
        purple)
            echo -e "\033[35m$2\033[0m"
            ;;
        *) 
            echo "Example: echo_color red string"
    esac
}
```

`function关键字定义一个函数，可加或不加。`

`使用方法：echo_color green "test"`

## 3.批量创建用户

```shell
#!/bin/bash
# description:  MySQL buckup shell script  
# author:       xiaokang

DATE=$(date +%F_%T)
USER_FILE=/root/user.txt
echo_color() {
    if [ $1 == "green" ]; then
        echo -e "\033[32m$2\033[0m"
    elif [ $1 == "red" ]; then
        echo -e "\033[31m$2\033[0m"
    elif [ $1 == "yellow" ]; then
        echo -e "\033[33m$2\033[0m"
    elif [ $1 == "blue" ]; then
        echo -e "\033[34m$2\033[0m"
    elif [ $1 == "purple" ]; then
        echo -e "\033[35m$2\033[0m"
    fi
}
# 如果用户文件存在并且大小大于0就备份
if [ -s $USER_FILE ]; then
    mv $USER_FILE ${USER_FILE}-${DATE}.bak
    echo_color green "$USER_FILE exist, rename ${USER_FILE}-${DATE}.bak"
fi
echo -e "User\tPassword" >> $USER_FILE
echo "----------------" >> $USER_FILE
for USER in user{1..10}; do
    if ! id $USER &>/dev/null; then
        PASS=$(echo $RANDOM |md5sum |cut -c 1-8)
        useradd -m $USER
        echo $PASS | passwd --stdin $USER >/dev/null 2>&1
        echo -e "$USER\t$PASS" >> $USER_FILE
        echo "User>>$USER create successful."
    else
        echo_color red "User>>$USER already exists!"
    fi
done
```

##  4.MySQL数据库备份脚本,下面的脚本是Mysql全量备份+异地备份

```shell
#!/bin/bash  
# description:  MySQL buckup shell script  
# author:       xiaokang
# 192.168.239.124 为专门的备份服务器，需要做一下服务器之间免密码登录

MYSQLDUMP=`which mysqldump`
#备份的数据库名
DATABASES=(
            "xiaokang01"
            "xiaokang02"                    
)
USER="root"
PASSWORD="xiaokang"

MAIL="xiaokang.188@qq.com" 
BACKUP_DIR=/data/backup
LOGFILE=/data/backup/data_backup.log 
DATE=`date +%Y%m%d_%H%M`

cd $BACKUP_DIR
#开始备份之前，将备份信息头写入日记文件   
echo "--------------------" >> $LOGFILE   
echo "BACKUP DATE:" $(date +"%y-%m-%d %H:%M:%S") >> $LOGFILE   
echo "-------------------" >> $LOGFILE

for DATABASE in ${DATABASES};do
  $MYSQLDUMP -u$USER -p$PASSWORD --events  -R --opt  $DATABASE | gzip >${BACKUP_DIR}\/${DATABASE}_${DATE}.sql.gz
  if [ $? == 0 ];then
    echo "$DATE--$DATABASE is backup succeed" >> $LOGFILE
  else
    echo "Database Backup Failed!" >> $LOGFILE   
done
#判断数据库备份是否全部成功，全部成功就同步到异地备份f服务器
if [ $? == 0 ];then
  /usr/bin/rsync -zrtopg   --delete  /data/backup/* root@192.168.239.124:/data/backup/  >/dev/null 2>&1
else
  echo "Database Backup Fail!" >> $LOGFILE   
  #备份失败后向管理者发送邮件提醒
  mail -s "database Daily Backup Failed!" $MAIL   
fi

#删除30天以上的备份文件  
find $BACKUP_DIR  -type f -mtime +30 -name "*.gz" -exec rm -f {} \;
```

 command>/dev/null 2>&1，/dev/null表示一个空设备，就是把 command的执行结果重定向到空设备中，说白了就是不显示任何信息。2>&1就是把标准错误重定向到标准输出， **&1 就是对标准输出的引用**  

**rsync是可以实现增量备份的工具。配合任务计划，rsync能实现定时或间隔同步。rsync选项说明**

```properties
-v：显示rsync过程中详细信息。可以使用"-vvvv"获取更详细信息。
-P：显示文件传输的进度信息。(实际上"-P"="--partial --progress"，其中的"--progress"才是显示进度信息的)。
-n --dry-run  ：仅测试传输，而不实际传输。常和"-vvvv"配合使用来查看rsync是如何工作的。
-a --archive  ：归档模式，表示递归传输并保持文件属性。等同于"-rtopgDl"。
-r --recursive：递归到目录中去。
-t --times：保持mtime属性。强烈建议任何时候都加上"-t"，否则目标文件mtime会设置为系统时间，导致下次更新
          ：检查出mtime不同从而导致增量传输无效。
-o --owner：保持owner属性(属主)。
-g --group：保持group属性(属组)。
-p --perms：保持perms属性(权限，不包括特殊权限)。
-D        ：是"--device --specials"选项的组合，即也拷贝设备文件和特殊文件。
-l --links：如果文件是软链接文件，则拷贝软链接本身而非软链接所指向的对象。
-z        ：传输时进行压缩提高效率。
-R --relative：使用相对路径。意味着将命令行中指定的全路径而非路径最尾部的文件名发送给服务端，包括它们的属性。用法见下文示例。
--size-only ：默认算法是检查文件大小和mtime不同的文件，使用此选项将只检查文件大小。
-u --update ：仅在源mtime比目标已存在文件的mtime新时才拷贝。注意，该选项是接收端判断的，不会影响删除行为。
-d --dirs   ：以不递归的方式拷贝目录本身。默认递归时，如果源为"dir1/file1"，则不会拷贝dir1目录，使用该选项将拷贝dir1但不拷贝file1。
--max-size  ：限制rsync传输的最大文件大小。可以使用单位后缀，还可以是一个小数值(例如："--max-size=1.5m")
--min-size  ：限制rsync传输的最小文件大小。这可以用于禁止传输小文件或那些垃圾文件。
--exclude   ：指定排除规则来排除不需要传输的文件。
--delete    ：以SRC为主，对DEST进行同步。多则删之，少则补之。注意"--delete"是在接收端执行的，所以它是在
            ：exclude/include规则生效之后才执行的。
-b --backup ：对目标上已存在的文件做一个备份，备份的文件名后默认使用"~"做后缀。
--backup-dir：指定备份文件的保存路径。不指定时默认和待备份文件保存在同一目录下。
-e          ：指定所要使用的远程shell程序，默认为ssh。
--port      ：连接daemon时使用的端口号，默认为873端口。
--password-file：daemon模式时的密码文件，可以从中读取密码实现非交互式。注意，这不是远程shell认证的密码，而是rsync模块认证的密码。
-W --whole-file：rsync将不再使用增量传输，而是全量传输。在网络带宽高于磁盘带宽时，该选项比增量传输更高效。
--existing  ：要求只更新目标端已存在的文件，目标端还不存在的文件不传输。注意，使用相对路径时如果上层目录不存在也不会传输。
--ignore-existing：要求只更新目标端不存在的文件。和"--existing"结合使用有特殊功能，见下文示例。
--remove-source-files：要求删除源端已经成功传输的文件。
```

## 5.系统初始化脚本（CentOS7）

```shell
#!/bin/bash
# Filename:    centos7-init.sh
# Author:     xiaokang

#判断是否为root用户
if [ `whoami` != "root" ];then
    echo " only root can run it"
    exit 1
fi
#执行前提示
echo -e "\033[31m 这是centos7系统初始化脚本，将更新系统内核至最新版本，请慎重运行！\033[0m" 
read -s -n1 -p "Press any key to continue or ctrl+C to cancel"
echo "Your inputs: $REPLY"
#1.定义配置yum源的函数
yum_config(){
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
yum clean all && yum makecache
}
#2.定义配置NTP的函数
ntp_config(){
yum –y install chrony
systemctl start chronyd && systemctl enable chronyd
timedatectl set-timezone Asia/Shanghai && timedatectl set-ntp yes
}
#3.定义关闭防火墙的函数
close_firewalld(){
systemctl stop firewalld.service &> /dev/null 
systemctl disable firewalld.service &> /dev/null
}
#4.定义关闭selinux的函数
close_selinux(){
setenforce 0
sed -i 's/enforcing/disabled/g' /etc/selinux/config
}
#5.定义安装常用工具的函数
yum_tools(){
yum install –y vim wget curl curl-devel bash-completion lsof iotop iostat unzip bzip2 bzip2-devel
yum install –y gcc gcc-c++ make cmake autoconf openssl-devel openssl-perl net-tools
source /usr/share/bash-completion/bash_completion
}
#6.定义升级最新内核的函数
update_kernel (){
rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-3.el7.elrepo.noarch.rpm
yum --enablerepo=elrepo-kernel install -y kernel-ml
grub2-set-default 0
grub2-mkconfig -o /boot/grub2/grub.cfg
}
#执行脚本
main(){
    yum_config;
    ntp_config;
    close_firewalld;
    close_selinux;
    yum_tools;
    update_kernel;
}
main
```



