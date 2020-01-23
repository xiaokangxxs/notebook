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
 -province : String //省
 -city : String //市
 -details : String //详细地址
 -realname : String //真实姓名
 -user_phone : String //手机号
 -user_sex : Character //性别
 -email : String //邮箱
 -address : String //地址
 -question : String //密保问题
 -answer : String //密保答案
 -register_date : Date //注册时间
 -face : String //头像
 -score : Integer //积分
 -state : Character //状态
 -activecode : String //激活码
 -activestate : Character //激活状态
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

### 2.管理员实体类

```plantuml
@startuml

class Manager {
__ private field __
 -manager_id : String //管理员编号
 -manager_name : String //管理员姓名
 -manager_pwd : String //管理员密码
 -manager_number : String //管理员手机号
 -manager_sex : Character //管理员性别
 -birthday : Date //出生日期
 -manager_face : String //管理员头像
 -manager_idcard : String //管理员身份证号
 -manager_address : String //管理员住址
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

### 3.角色实体类

```plantuml
@startuml

class Role {
__ private field __
 -role_id : String //角色编号
 -role_name : String //角色名称
 -role_code : String //角色关键字
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

### 4.权限实体类

```plantuml
@startuml

class Function {
__ private field __
 -id : String //权限编号
 -name : String //权限名称
 -code : String //权限关键字
 -fundesc : String //权限描述
 -page : String //权限url
 -zindex : Integer //权限优先级
 -generatemenu : String //是否生成菜单
.. Some Getter ..
  + getId()
  + getName()
  ...
.. Some setter ..
  + setId(String id)
  + setName(String name)
  ...
}

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

### 6.收入类型实体类

```plantuml
@startuml

class InAccountType {
__ private field __
 -inaccounttype_id : Long //收入类型编号
 -inaccounttype_name : String //收入类型名称
 -inaccounttype_createtime : Date //收入类型创建时间
 -inaccounttype_desc : String //收入类型备注
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

### 8.支出类型实体类

```plantuml
@startuml

class OutAccountType {
__ private field __
 -outaccounttype_id : Long //收入类型编号
 -outaccounttype_name : String //收入类型名称
 -outaccounttype_createtime : Date //收入类型创建时间
 -outaccounttype_desc : String //收入类型备注
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
 -topic_bad : Integer //不赞数
 -del : Character // 是否删除 1:删除 0：未删除
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

### 11.回复贴实体类

```plantuml
@startuml

class Reply {
__ private field __
 -reply_id : Long // 回复贴编号
 -reply_content : String // 回复内容
 -reply_datetime : Date // 回复日期
 -is_top : Character // 是否置顶
 -zan : Integer // 赞数量
 -bad : Integer // 鄙视数
 -status : Integer // 是否被查看
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

### 12. 投资理财实体类

```plantuml
@startuml

class Invest {
__ private field __
 -invest_id : String // 投资理财编号
 -investname : String // 投资理财名称
 -invest_datetime : Date // 时间
 -invest_year : Integer // 投资周期
 -invest_target : String // 投资目标
 -invest_createtime : Date // 创建时间
 -interest_rates : Double // 利率
 -invest_money : Double // 投资金额
 -invest_desc : String // 投资理财备注
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

### 13.借款还贷实体类

```plantuml
@startuml

class Loan {
__ private field __
 -invest_id : String // 投资理财编号
 -loan_id : String // 借款还贷编号
 -loanname : String // 借款还贷名称
 -loan_datetime : Date // 借款还贷日期
 -loan_year : Integer // 周期（按年）
 -interest_rates : Double // 利息率
 -loan_money : Double // 借款金额
 -loan_source : String // 借款来源
 -loan_desc : String // 借款还贷备注
 -loan_createtime : Date // 创建时间
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