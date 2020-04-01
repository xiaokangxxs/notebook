# 家庭记账管理系统-PlantUML

## 实体类
### 1.用户实体类

```plantuml
@startuml

class User {
__ private field __
 -user_id : Long //用户编号
 -user_name : String //用户昵称
 -user_password : String //用户密码
 -birthday : Date //出生日期
 -idcard : String //身份证号
 -details : String //详细地址
 -realname : String //真实姓名
 -user_phone : String //手机号
 -user_sex : Character //性别
 -email : String //邮箱
 -question : String //密保问题
 -answer : String //密保答案
 -register_date : Date //注册时间
 -face : String //头像
 -state : Character //状态
 -level : Integer //用户级别
 -loginDate : Date //上次登录时间
.. Some Getter ..
  + getUser_id()
  + getUser_name()
  ...
.. Some setter ..
  + setUser_id(Long user_id)
  + setUser_name(String user_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle User
User -- (user_id)
User -- (用户昵称)
User -- (用户密码)
User -- (性别)
User -- (手机号)
User -- (出生日期)
User -- (身份证号)
User -- (详细地址)
User -- (真实姓名)
User -- (邮箱)
User -- (注册时间)
User -- (头像)
User -- (状态)
User -- (用户级别)
User -- (上次登录时间)
@enduml
```

### 2.管理员实体类

```plantuml
@startuml

class Manager {
__ private field __
 -manager_id : String //管理员编号
 -manager_name : String //管理员姓名
 -manager_pwd : String //管理员密码
 -manager_phone : String //管理员手机号
 -manager_sex : Character //管理员性别
 -birthday : Date //出生日期
 -idcard : String //身份证号
 -address : String //详细住址
.. Some Getter ..
  + getManager_id()
  + getManager_name()
  ...
.. Some setter ..
  + setManager_id(String manager_id)
  + setManager_name(String manager_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Manager
Manager -- (manager_id)
Manager -- (管理员姓名)
Manager -- (管理员密码)
Manager -- (管理员手机号)
Manager -- (管理员性别)
Manager -- (出生日期)
Manager -- (身份证号)
Manager -- (详细住址)
@enduml
```

### 3.角色实体类

```plantuml
@startuml

class Role {
__ private field __
 -role_id : String //角色编号
 -role_name : String //角色名称
 -role_desc : String //角色描述
.. Some Getter ..
  + getRole_id()
  + getRole_name()
  ...
.. Some setter ..
  + setRole_id(String role_id)
  + setRole_name(String role_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Role
Role -- (role_id)
Role -- (角色名称)
Role -- (角色描述)
@enduml
```

### 4.权限实体类

```plantuml
@startuml

class Menu {
__ private field __
 -menu_id : String //权限编号
 -menu_name : String //权限名称
 -menu_code : String //权限关键字
 -menu_desc : String //权限描述
 -menu_page : String //权限url
 -zindex : Integer //权限优先级
 -generatemenu : String //是否生成菜单
.. Some Getter ..
  + getMenu_id()
  + getMenu_name()
  ...
.. Some setter ..
  + getMenu_id(String menu_id)
  + getMenu_name(String menu_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Menu
Menu -- (menu_id)
Menu -- (权限名称)
Menu -- (权限关键字)
Menu -- (权限描述)
Menu -- (权限url)
Menu -- (权限优先级)
Menu -- (是否生成菜单)
@enduml
```

### 5.收入账单实体类

```plantuml
@startuml

class InAccount {
__ private field __
 -inaccount_id : Long //收入账单编号
 -inaccount_datetime : Long //收入日期时间
 -inaccount_money : Double //收入金额
 -inaccount_createtime : Date //收入账单创建时间
 -inaccount_desc : String //收入账单备注
 -in_user_id : Long //用户id
 -in_type_id : Long //收入类型id
.. Some Getter ..
  + getInaccount_id()
  + getInaccount_datetime()
  ...
.. Some setter ..
  + setInaccount_id(Long inaccount_id)
  + setInaccount_datetime(Date inaccount_datetime)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle InAccount
InAccount -- (inaccount_id)
InAccount -- (收入日期时间)
InAccount -- (收入金额)
InAccount -- (收入账单创建时间)
InAccount -- (收入账单备注)
InAccount -- (用户id)
InAccount -- (收入类型id)
@enduml
```

### 6.收入类型实体类

```plantuml
@startuml

class InAccountType {
__ private field __
 -inaccounttype_id : Long //收入类型编号
 -inaccounttype_name : String //收入类型名称
 -inaccounttype_createtime : Date //收入类型创建时间
 -inaccounttype_desc : String //收入类型备注
 -it_user_id : Long //用户id
.. Some Getter ..
  + getInaccounttype_id()
  + getInaccounttype_name()
  ...
.. Some setter ..
  + setInaccounttype_id(Long inaccounttype_id)
  + setInaccounttype_name(String inaccounttype_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle InAccountType
InAccountType -- (inaccounttype_id)
InAccountType -- (收入类型名称)
InAccountType -- (收入类型创建时间)
InAccountType -- (收入类型备注)
InAccountType -- (用户id)
@enduml
```

### 7.支出账单实体类

```plantuml
@startuml

class OutAccount {
__ private field __
 -outaccount_id : Long //支出账单编号
 -outaccount_datetime : Long //支出日期时间
 -outaccount_money : Double //支出金额
 -outaccount_createtime : Date //支出账单创建时间
 -outaccount_desc : String //支出账单备注
 -out_user_id : Long //用户id
 -out_type_id : Long //支出类型id 
.. Some Getter ..
  + getOutaccount_id()
  + getOutaccount_datetime()
  ...
.. Some setter ..
  + setOutaccount_id(Long outaccount_id)
  + setOutaccount_datetime(Date outaccount_datetime)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle OutAccount
OutAccount -- (outaccount_id)
OutAccount -- (支出日期时间)
OutAccount -- (支出金额)
OutAccount -- (支出账单创建时间)
OutAccount -- (支出账单备注)
OutAccount -- (用户id)
OutAccount -- (支出类型id)
@enduml
```

### 8.支出类型实体类

```plantuml
@startuml

class OutAccountType {
__ private field __
 -outaccounttype_id : Long //收入类型编号
 -outaccounttype_name : String //收入类型名称
 -outaccounttype_createtime : Date //收入类型创建时间
 -outaccounttype_desc : String //收入类型备注
 -ot_user_id : Long //用户id
.. Some Getter ..
  + getOutaccounttype_id()
  + getOutaccounttype_name()
  ...
.. Some setter ..
  + setOutaccounttype_id(Long outaccounttype_id)
  + setOutaccounttype_name(String outaccounttype_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle OutAccountType
OutAccountType -- (outaccounttype_id)
OutAccountType -- (支出类型名称)
OutAccountType -- (支出类型创建时间)
OutAccountType -- (支出类型备注)
OutAccountType -- (用户id)
@enduml
```

### 9.交流大区实体类

```plantuml
@startuml

class Region {
__ private field __
 -region_id : Integer // 交流大区编号
 -region_name : String // 交流大区名称
 -region_desc : String // 交流大区备注
.. Some Getter ..
  + getRegion_id()
  + getRegion_name()
  ...
.. Some setter ..
  + setRegion_id(Integer region_id)
  + setRegion_name(String region_name)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Region
Region -- (region_id)
Region -- (交流大区名称)
Region -- (交流大区备注)
@enduml
```

### 10.主题帖实体类

```plantuml
@startuml

class Topic {
__ private field __
 -topic_id : Long // 主题帖编号
 -topic_title : String // 主题帖标题
 -topic_content : String // 主题帖内容
 -topic_datetime : Date // 主题帖发布日期
 -is_top : Character // 是否置顶
 -is_good : Character // 是否加精
 -is_end : Character // 是否结贴
 -look_count : Integer // 浏览数量
 -topic_zan : Integer //点赞数
 -topic_bad : Integer //鄙视数
 -del : Character // 帖子是否删除 1:删除 0：未删除
 -solrDel : Character // 索引是否删除 1:删除 0：未删除
 -topic_user_id : Long //用户id
 -topic_region_id : Integer //交流大区id
.. Some Getter ..
  + getTopic_id()
  + getTopic_title()
  ...
.. Some setter ..
  + setTopic_id(Long topic_id)
  + setTopic_title(String topic_title)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Topic
Topic -- (topic_id)
Topic -- (主题帖标题)
Topic -- (主题帖内容)
Topic -- (主题帖发布日期)
Topic -- (是否置顶)
Topic -- (是否加精)
Topic -- (是否结帖)
Topic -- (点赞数)
Topic -- (鄙视数)
Topic -- (浏览数量)
Topic -- (帖子是否删除)
Topic -- (索引是否删除)
Topic -- (用户id)
Topic -- (交流大区id)
@enduml
```

### 11.回复贴实体类

```plantuml
@startuml

class Reply {
__ private field __
 -reply_id : Long // 回复贴编号
 -reply_content : String // 回复内容
 -reply_datetime : Date // 回复日期
 -zan : Integer // 点赞数
 -bad : Integer // 鄙视数
 -status : Integer // 是否被查看
 -reply_user_id : Long //用户id
 -reply_topic_id : Long //帖子id
.. Some Getter ..
  + getReply_id()
  + getReply_content()
  ...
.. Some setter ..
  + setReply_id(Long reply_id)
  + setReply_content(String reply_content)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Reply
Reply -- (reply_id)
Reply -- (回复内容)
Reply -- (回复日期)
Reply -- (点赞数)
Reply -- (鄙视数)
Reply -- (是否被查看)
Reply -- (用户id)
Reply -- (帖子id)
@enduml
```

### 12. 投资理财实体类

```plantuml
@startuml

class Invest {
__ private field __
 -invest_id : String // 投资理财编号
 -invest_name : String // 投资理财名称
 -invest_datetime : Date // 时间
 -invest_year : Integer // 投资周期
 -invest_target : String // 投资目标
 -invest_createtime : Date // 创建时间
 -interest_rates : Double // 利率
 -invest_money : Double // 投资金额
 -invest_desc : String // 投资理财备注
 -invest_user_id : Long //用户id
.. Some Getter ..
  + getInvest_id()
  + getInvestname()
  ...
.. Some setter ..
  + setInvest_id(String invest_id)
  + setInvestname(String investname)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Invest
Invest -- (invest_id)
Invest -- (投资理财名称)
Invest -- (投资时间)
Invest -- (投资周期)
Invest -- (投资目标)
Invest -- (创建时间)
Invest -- (利率)
Invest -- (投资金额)
Invest -- (投资理财备注)
Invest -- (用户id)
@enduml
```

### 13.借款还贷实体类

```plantuml
@startuml

class Loan {
__ private field __
 -loan_id : String // 借款还贷编号
 -loan_name : String // 借款还贷名称
 -loan_datetime : Date // 借款还贷日期
 -loan_year : Integer // 周期（按年）
 -interest_rates : Double // 利息率
 -loan_money : Double // 借款金额
 -loan_source : String // 借款来源
 -loan_desc : String // 借款还贷备注
 -loan_createtime : Date // 创建时间
 -loan_user_id : Long //用户id
.. Some Getter ..
  + getLoan_id()
  + getLoanname()
  ...
.. Some setter ..
  + setLoan_id(String loan_id)
  + setLoanname(String loanname)
  ...
}

@enduml
```

```plantuml
@startuml
rectangle Loan
Loan -- (loan_id)
Loan -- (借款还贷名称)
Loan -- (借款还贷日期)
Loan -- (周期（按年）)
Loan -- (利息率)
Loan -- (借款金额)
Loan -- (借款来源)
Loan -- (借款还贷备注)
Loan -- (创建时间)
Loan -- (用户id)
@enduml
```

## 数据备份机制

```shell
cription:  MySQL backup shell script  
# author:       xiaokang
# familyaccount.xiaokang.cool 为专门的备份服务器，需要做一下服务器之间免密码登录

MYSQLDUMP=`which mysqldump`
#备份的数据库名
DATABASE="familyaccount"
USER="root"
PASSWORD="123456"

MAIL="xiaokang.188@qq.com" 
BACKUP_DIR=/home/backup
LOGFILE=/home/backup/data_backup.log 
DATE=`date +%Y%m%d_%H%M`

cd $BACKUP_DIR
#开始备份之前，将备份信息头写入日记文件   
echo "--------------------" >> $LOGFILE   
echo "BACKUP DATE:" $(date +"%y-%m-%d %H:%M:%S") >> $LOGFILE   
echo "-------------------" >> $LOGFILE

$MYSQLDUMP -u$USER -p$PASSWORD --events  -R --opt  $DATABASE | gzip >${BACKUP_DIR}\/${DATABASE}_${DATE}.sql.gz
if [ $? == 0 ];then
    echo "$DATE--$DATABASE is backup succeed" >> $LOGFILE
else
    echo "Database Backup Failed!" >> $LOGFILE   
fi
#判断数据库备份是否全部成功，全部成功就同步到小康个人服务器
if [ $? == 0 ];then
  /usr/bin/rsync -zrtopg   --delete  /home/backup/* xiaokang@familyaccount.xiaokang.cool:/home/xiaokang/familyaccount_sql_backup/  >/dev/null 2>&1
else
  echo "Database Backup Fail!" >> $LOGFILE   
  #备份失败后向管理者发送邮件提醒
  mail -s "database Daily Backup Failed!" $MAIL   
fi

#删除30天以上的备份文件  
find $BACKUP_DIR  -type f -mtime +30 -name "*.gz" -exec rm -f {} \;

【crontab定时任务】
#每天凌晨过一分钟,对familyaccount数据库进行全量备份并同步到小康个人服务器
1 0 * * * sh /root/familyaccount_backup.sh
```

## 前端页面

### 1. 登录界面

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/login.png"/> </div>
### 2. 家庭户主主界面

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/family-leader.png"/> </div>
### 3. 家庭成员主界面

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/family-follower.png"/> </div>
### 4. 收入账单列表

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/inaccount_list.png"/> </div>
### 5. 收入账单类型列表

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/inaccounttype_list.png"/> </div>
### 6. 添加收入账单

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/inaccount_add.png"/> </div>
### 7. 添加收入账单类型

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/inaccounttype_add.png"/> </div>
### 8. 收入账单报表

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/inaccount_report.png"/> </div>
### 9. 交流大区

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/region.png"/> </div>
### 10. 投资理财列表（家庭户主）

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/invest.png"/> </div>
### 11. 借款还贷列表（家庭户主）

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/loan.png"/> </div>
### 12. 主题帖列表

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/topics.png"/> </div>
### 13. 帖子详情

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/perTopic.png"/> </div>
### 14. Solr搜索高亮显示

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/solr.png"/> </div>
### 15. 我的帖子

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/UML/familyaccount/myTopic.png"/> </div>
## 后台页面



## 登录时序图

```plantuml
@startuml
actor User

User -> 登录页面: 1.访问登录页面
activate 登录页面

登录页面 -> 登录页面: 2.创建登录会话
deactivate 登录页面

User -> 登录页面: 3.提交身份信息
activate 登录页面

登录页面 -> 登录页面: 4.系统进行验证
deactivate 登录页面

登录页面 -> 系统主界面: 5.验证通过进入系统主界面
deactivate 系统主界面

登录页面 -> User: 验证失败返回错误信息
deactivate 登录页面
@enduml
```

## 参考案例

### 1.思维导图

```plantuml
@startmindmap
caption figure 1
title Linux发行版之Debian系列

* <&flag>Debian
** <&globe>Ubuntu
*** Linux Mint
*** Kubuntu
*** Lubuntu
*** KDE Neon
** <&graph>LMDE
** <&pulse>SolydXK
** <&people>SteamOS
** <&star>Raspbian with a very long name
*** <s>Raspmbc</s> => OSMC
*** <s>Raspyfi</s> => Volumio

header
作者:小康
endheader

center footer 微信公众号：小康新鲜事儿

legend right
  Short
  legend
endlegend
@endmindmap
```

### 2.流程图

- 开始/结束

```plantuml
@startuml
start
:Hello world;
:This is on defined onseveral **lines**;
stop
@enduml
```

- 一个完整的例子

```plantuml
@startuml

start
:ClickServlet.handleRequest();
:new page;
if (Page.onSecurityCheck) then (true)
  :Page.onInit();
  if (isForward?) then (no)
	:Process controls;
	if (continue processing?) then (no)
	  stop
	endif
	
	if (isPost?) then (yes)
	  :Page.onPost();
	else (no)
	  :Page.onGet();
	endif
	:Page.onRender();
  endif
else (false)
endif

if (do redirect?) then (yes)
  :redirect process;
else
  if (do forward?) then (yes)
	:Forward request;
  else (no)
	:Render page template;
  endif
endif

stop

@enduml
```

```plantuml
@startuml
skinparam rectangle {
	roundCorner<<集群规划-xiaokang>> 25
}

rectangle "老大:hadoop01,小弟:hadoop02、hadoop03" <<集群规划-xiaokang>> {
node hadoop01 #red [
NameNode
DataNode
----
ResourceManager
NodeManager
] 
node hadoop02 #CCCCFF [
DataNode
----
NodeManager
]
node hadoop03 #CCCCFF [
DataNode
----
NodeManager
]
hadoop01 -- hadoop02
hadoop01 -- hadoop03
}
@enduml
```

