# 达梦数据库V8-DTS工具使用

## 前置准备

DBeaver Ultimate-22.1.0、MySQL-5.7.39、DM8、达梦数据迁移工具-DTS(2022.10.19)、达梦管理工具-MANAGER(2022.10.19)、MobaXterm、sakila.sql、update_sakila.sql、update_sakila_dm.sql

本期给大家演示的是MySQL2DM8数据迁移，想要完成本期视频中所有操作，需要以下准备：

> [达梦数据库V8单机安装及基本使用](https://mp.weixin.qq.com/s/W2Y5jTRVDWgHv_GPn8VoMg)

## 一、概述

达梦官方技术文档：[达梦技术文档](https://eco.dameng.com/document/dm/zh-cn/start/migrate-mysql-dm.html)

DM 数据迁移工具 DM DTS 提供了主流大型数据库迁移到 DM、DM 到 DM、文件迁移到 DM 以及 DM 迁移到文件等功能。得益于 DM 数据库对目前主流大型关系型数据库系统有着业界领先的兼容性，在存储层面、语法层面、接口层面和它们保持高度兼容，借助于 DM 图形界面且采用向导方式引导各个迁移步骤的 DTS 工具，移植工作可以变得非常的简单。

## 二、迁移前准备

### 2.1 初始化库

这里需要关注五个参数：PAGE_SIZE、EXTENT_SIZE、CASE_SENSITIVE、LENGTH_IN_CHAR、CHARSET

```shell
PAGE_SIZE
# 数据文件使用的页大小。取值：4、8、16、32，单位：K。默认值为 8。可选参数。 选择的页大小越大，则 DM 支持的元组长度也越大，但同时空间利用率可能下降。

EXTENT_SIZE
# 数据文件使用的簇大小，即每次分配新的段空间时连续的页数。取值：16、32、64。单位：页数。缺省值 16。可选参数。

CASE_SENSITIVE
# 标识符大小写敏感。当大小写敏感时，小写的标识符应用""括起，否则被系统自动转换为大写；当大小写不敏感时，系统不会转换标识符的大小写，在标识符比较时也不能区分大小写。取值：Y、y、1 表示敏感；N、n、0 表示不敏感。默认值为 Y。可选参数。建议 MySQL 和 SQLSERVER 迁移过来的系统，使用大小写不敏感。

LENGTH_IN_CHAR
# VARCHAR 类型对象的长度是否以字符为单位。取值：1、Y表示是，0、N表示否。默认值为 0。可选参数。
# 1 或 Y：是，所有 VARCHAR 类型对象的长度以字符为单位。这种情况下，定义长度并非真正按照字符长度调整，而是将存储长度值按照理论字符长度进行放大。所以会出现实际可插入字符数超过定义长度的情况，这种情况也是允许的。同时，存储的字节长度 8188 上限仍然不变，也就是说，即使定义列长度为 8188 字符，其实际能插入的字符串占用总字节长度仍然不能超过 8188；0 或 N：否，所有 VARCHAR 类型对象的长度以字节为单位。

CHARSET
# 字符集选项。取值：0 代表 GB18030，1 代表 UTF-8，2 代表韩文字符集 EUC-KR。默认为 0。可选参数。
```

上期视频示例初始化命令如下：

```shell
# 初始化XIAOKANG数据库实例，参考DM8_dminit使用手册
[dmdba@hadoop ~]$ dminit path=/data/dm8/data DB_NAME=XIAOKANG INSTANCE_NAME=XIAOKANG PAGE_SIZE=32 EXTENT_SIZE=64 CASE_SENSITIVE=N LENGTH_IN_CHAR=1 CHARSET=1 PORT_NUM=5238 SYSDBA_PWD=Xiaokang_202211 SYSAUDITOR_PWD=Xiaokang_202211
```

### 2.2 兼容参数

使用dmdba用户，编辑/data/dm8/data/XIAOKANG/dm.ini文件，修改COMPATIBLE_ MODE参数值：

```shell
# 找到COMPATIBLE_ MODE，将它的值修改为4，找到ENABLE_BLOB_CMP_FLAG，将它的值修改为1
[dmdba@hadoop ~]$ vi /data/dm8/data/XIAOKANG/dm.ini
```

修改完成后重启XIAOKANG数据库实例：

```shell
[dmdba@hadoop bin]$ cd /data/dm8/dinstall/bin
[dmdba@hadoop bin]$ ./DmServiceXIAOKANG restart
[dmdba@hadoop bin]$ ./DmServiceXIAOKANG status
[dmdba@hadoop bin]$ ps -ef|grep dmserver
```

![xiaokangxxs-image-20221203135005004](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203135005004.4f23bf2wt480.jpg)

![xiaokangxxs-image-20221201084513366](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221201084513366.2a75l35q99no.jpg)

### 2.3 创建用户和表空间

分析移植需要从源库中移植哪一个库或者哪几个库的数据，然后为每一个库，分别在达梦中创建独立的表空间和用户，本次迁移的库为sakila：

```sql
create tablespace xiaokang_sakila datafile 'xiaokang_sakila.dbf' size 200 autoextend on maxsize 10240;
create user xiaokang_sakila identified by "xiaokang_202212" default tablespace xiaokang_sakila default index tablespace xiaokang_sakila;
grant "RESOURCE","PUBLIC","DBA","VTI" to xiaokang_sakila;
```

![xiaokangxxs-image-20221203155041410](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203155041410.4qugzpqs2jk0.jpg) 

### 2.4 图形化界面显示

```shell
# 安装图形相关rpm包
[root@hadoop ~]# yum -y install gtk2 libXtst xorg-x11-fonts-Type1
# 安装X窗口包和字体包
[root@hadoop ~]# yum -y groupinstall "X Window System" "Fonts"
# 设置图形在本机进行显示
[dmdba@hadoop ~]$ export DISPLAY=192.168.118.1:0.0
```

![xiaokangxxs-image-20221203154856904](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203154856904.1kidq2w2rhj4.jpg)

## 三、数据迁移

### 3.1 调整mysql数据

使用DBeaver UE连接MySQL，执行以下语句（update_sakila.sql）进行数据调整：

```sql
-- 调整sakila数据库编码格式为utf8
alter database sakila character set
utf8;
-- 调整表编码格式为utf8
alter table actor convert to character set
utf8;

alter table address convert to character set
utf8;

alter table category convert to character set
utf8;

alter table city convert to character set
utf8;

alter table country convert to character set
utf8;

alter table customer convert to character set
utf8;

alter table film convert to character set
utf8;

alter table film_actor convert to character set
utf8;

alter table film_category convert to character set
utf8;

alter table film_text convert to character set
utf8;

alter table inventory convert to character set
utf8;

alter table `language` convert to character set
utf8;

alter table payment convert to character set
utf8;

alter table rental convert to character set
utf8;

alter table staff convert to character set
utf8;

alter table store convert to character set
utf8;

alter table film_actor drop foreign key fk_film_actor_actor;

alter table film_actor drop foreign key fk_film_actor_film;

alter table address drop foreign key fk_address_city;

alter table city drop foreign key fk_city_country;

alter table customer drop foreign key fk_customer_address;

alter table customer drop foreign key fk_customer_store;

alter table film drop foreign key fk_film_language;

alter table film drop foreign key fk_film_language_original;

alter table film_category drop foreign key fk_film_category_category;

alter table film_category drop foreign key fk_film_category_film;

alter table inventory drop foreign key fk_inventory_film;

alter table inventory drop foreign key fk_inventory_store;

alter table payment drop foreign key fk_payment_customer;

alter table payment drop foreign key fk_payment_rental;

alter table payment drop foreign key fk_payment_staff;

alter table rental drop foreign key fk_rental_customer;

alter table rental drop foreign key fk_rental_inventory;

alter table rental drop foreign key fk_rental_staff;

alter table staff drop foreign key fk_staff_address;

alter table staff drop foreign key fk_staff_store;

alter table store drop foreign key fk_store_address;

alter table store drop foreign key fk_store_staff;

alter table actor modify column actor_id smallint(5) not null auto_increment;

alter table address modify column address_id smallint(5) not null auto_increment;

alter table address modify column city_id smallint(5) not null;

alter table category modify column category_id tinyint(3) not null auto_increment;

alter table city modify column city_id smallint(5) not null auto_increment;

alter table city modify column country_id smallint(5) not null;

alter table country modify column country_id smallint(5) not null auto_increment;

alter table customer modify column customer_id smallint(5) not null auto_increment;

alter table customer modify column store_id tinyint(3) not null;

alter table customer modify column address_id smallint(5) not null;

alter table film modify column film_id smallint(5) not null auto_increment;

alter table film modify column language_id tinyint(3) not null;

alter table film modify column original_language_id tinyint(3);

alter table film modify column rental_duration tinyint(3) not null;

alter table film modify column length smallint(5);

alter table film_actor modify column actor_id smallint(5) not null;

alter table film_actor modify column film_id smallint(5) not null;

alter table film_category modify column film_id smallint(5) not null;

alter table film_category modify column category_id tinyint(3) not null;

alter table inventory modify column inventory_id mediumint(8) not null auto_increment;

alter table inventory modify column film_id smallint(5) not null;

alter table inventory modify column store_id tinyint(3) not null;

alter table language modify column language_id tinyint(3) not null auto_increment;

alter table payment modify column payment_id smallint(5) not null auto_increment;

alter table payment modify column customer_id smallint(5) not null;

alter table payment modify column staff_id tinyint(3) not null;

alter table rental modify column inventory_id mediumint(8) not null;

alter table rental modify column customer_id smallint(5) not null;

alter table rental modify column staff_id tinyint(3) not null;

alter table staff modify column staff_id tinyint(3) not null auto_increment;

alter table staff modify column address_id smallint(5) not null;

alter table staff modify column store_id tinyint(3) not null;

alter table store modify column store_id tinyint(3) not null auto_increment;

alter table store modify column manager_staff_id tinyint(3) not null;

alter table store modify column address_id smallint(5) not null;

alter table customer modify column create_date datetime not null default '0001-01-01 00:00:00';

alter table payment modify column payment_date datetime not null default '0001-01-01 00:00:00';

alter table rental modify column rental_date datetime not null default '0001-01-01 00:00:00';

alter table rental modify column return_date datetime not null default '0001-01-01 00:00:00';

alter table address drop key `idx_location`;

alter table address modify column location varchar(100) not null;

set
FOREIGN_KEY_CHECKS = 0;

alter table city add constraint fk_city_country foreign key(country_id) references country(country_id);

alter table address add constraint fk_address_city foreign key(city_id) references city(city_id);

alter table staff add constraint fk_staff_address foreign key(address_id) references address(address_id);

alter table staff add constraint fk_staff_store foreign key(staff_id) references store(store_id);

alter table film add constraint fk_film_language foreign key(language_id) references language(language_id);

alter table film add constraint fk_film_language_original foreign key(original_language_id) references language(language_id);

alter table store add constraint fk_store_address foreign key(address_id) references address(address_id);

alter table store add constraint fk_store_staff foreign key(manager_staff_id) references staff(staff_id);

alter table film_actor add constraint fk_film_actor_actor foreign key(actor_id) references actor(actor_id);

alter table film_actor add constraint fk_film_actor_film foreign key(film_id) references film(film_id);

alter table film_category add constraint fk_film_category_category foreign key(category_id) references category(category_id);

alter table film_category add constraint fk_film_category_film foreign key(film_id) references film(film_id);

alter table inventory add constraint fk_inventory_film foreign key(film_id) references film(film_id);

alter table inventory add constraint fk_inventory_store foreign key(store_id) references store(store_id);

alter table customer add constraint fk_customer_address foreign key(address_id) references address(address_id);

alter table customer add constraint fk_customer_store foreign key(store_id) references store(store_id);

alter table rental add constraint fk_rental_customer foreign key(customer_id) references customer(customer_id);

alter table rental add constraint fk_rental_inventory foreign key(inventory_id) references inventory(inventory_id);

alter table rental add constraint fk_rental_staff foreign key(staff_id) references staff(staff_id);

alter table payment add constraint fk_payment_customer foreign key(customer_id) references customer(customer_id);

alter table payment add constraint fk_payment_rental foreign key(rental_id) references rental(rental_id);

alter table payment add constraint fk_payment_staff foreign key(staff_id) references staff(staff_id);
```

![xiaokangxxs-image-20221203154654099](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203154654099.7ao7cejk7pk0.jpg)

### 3.2 迁移

切换到dmdba用户，打开dts工具图形化界面：

```shell
# 确保DISPLAY已经加载好再执行dts
[dmdba@hadoop ~]$ echo $DISPLAY
[dmdba@hadoop ~]$ dts
```

（1）新建工程并填写基本信息

![xiaokangxxs-image-20221203161320298](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203161320298.2sxmt4tnij20.jpg)

（2）新建迁移并填写基本信息

![xiaokangxxs-image-20221203161615642](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203161615642.3ci5611xctu0.jpg)

（3）选择迁移方式并填写MySQL数据源与达梦目的信息

![xiaokangxxs-image-20221203162258722](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203162258722.71d89earxrg0.jpg)

![xiaokangxxs-image-20221203162351344](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203162351344.6tksxyx3r7w0.jpg)

![xiaokangxxs-image-20221203162444409](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203162444409.7dknq63ev88.jpg)

（4）指定对象复制或查询

![xiaokangxxs-指定对象复制或查询](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-指定对象复制或查询.4aav2rdfm2k0.gif)

（5）选择迁移对象

![xiaokangxxs-选择迁移对象](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-选择迁移对象.cfc6l3i12ds.gif)

（6）审阅任务并开始执行迁移

![xiaokangxxs-image-20221203171341996](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203171341996.2els3r2zfo4k.jpg)

![xiaokangxxs-image-20221203171417924](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203171417924.5giuoacxc3g0.jpg)

任务执行结束后会有3个报错，如下图所示，这是关于创建视图的错误，继续往下操作，手动进行解决。

![xiaokangxxs-image-20221203133401056](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203133401056.6y00qad4muo0.jpg)

### 3.3 调整遗漏数据

使用disql连接DM8，执行以下语句（update_sakila_dm.sql）进行数据调整：

```shell
[dmdba@hadoop ~]$ disql xiaokang_sakila/xiaokang_202212@127.0.0.1:5238

服务器[127.0.0.1:5238]:处于普通打开状态
登录使用时间 : 2.148(ms)
disql V8
SQL> start /home/dmdba/update_sakila_dm.sql
```

```sql
-- 创建actor_info视图
CREATE VIEW "XIAOKANG_SAKILA"."actor_info" ("actor_id",
"first_name",
"last_name",
"film_info")  
AS 
SELECT
	"a"."actor_id" AS "actor_id",
	"a"."first_name" AS "first_name",
	"a"."last_name" AS "last_name",
	LISTAGG(DISTINCT concat("c"."name",
	': ',
	(
	SELECT
		LISTAGG("f"."title" ,
		', ') WITHIN GROUP(
	ORDER BY
		"f"."title" ASC)
	FROM
		(("film" "f"
	JOIN "film_category" "fc" ON
		(("f"."film_id" = "fc"."film_id")))
	JOIN "film_actor" "fa" ON
		(("f"."film_id" = "fa"."film_id")))
	WHERE
		(("fc"."category_id" = "c"."category_id")
			AND ("fa"."actor_id" = "a"."actor_id")))) ,
	'; ') WITHIN GROUP(
ORDER BY
	"c"."name" ASC) AS "film_info"
FROM
	((("actor" "a"
LEFT JOIN "film_actor" "fa" ON
	(("a"."actor_id" = "fa"."actor_id")))
LEFT JOIN "film_category" "fc" ON
	(("fa"."film_id" = "fc"."film_id")))
LEFT JOIN "category" "c" ON
	(("fc"."category_id" = "c"."category_id")))
GROUP BY
	"a"."actor_id",
	"a"."first_name",
	"a"."last_name";

-- 创建film_list视图
CREATE VIEW "XIAOKANG_SAKILA"."film_list" ("FID",
"title",
"description",
"category",
"price",
"length",
"rating",
"actors")  
AS 
SELECT
	"film"."film_id" AS "FID",
	"film"."title" AS "title",
	"film"."description" AS "description",
	"category"."name" AS "category",
	"film"."rental_rate" AS "price",
	"film"."length" AS "length",
	"film"."rating" AS "rating",
	LISTAGG(concat("actor"."first_name",
	' ',
	"actor"."last_name") ,
	', ') AS "actors"
FROM
	(((("category"
LEFT JOIN "film_category" ON
	(("category"."category_id" = "film_category"."category_id")))
LEFT JOIN "film" ON
	(("film_category"."film_id" = "film"."film_id")))
JOIN "film_actor" ON
	(("film"."film_id" = "film_actor"."film_id")))
JOIN "actor" ON
	(("film_actor"."actor_id" = "actor"."actor_id")))
GROUP BY
	"film"."film_id",
	"category"."name";

-- 创建nicer_but_slower_film_list视图
CREATE VIEW "XIAOKANG_SAKILA"."nicer_but_slower_film_list" ("FID",
"title",
"description",
"category",
"price",
"length",
"rating",
"actors")  
AS 
SELECT
	"film"."film_id" AS "FID",
	"film"."title" AS "title",
	"film"."description" AS "description",
	"category"."name" AS "category",
	"film"."rental_rate" AS "price",
	"film"."length" AS "length",
	"film"."rating" AS "rating",
	LISTAGG(concat(concat(upper(substr("actor"."first_name", 1, 1)),
	lower(substr("actor"."first_name", 2, LENGTH("actor"."first_name"))),
	' ',
	concat(upper(substr("actor"."last_name", 1, 1)),
	lower(substr("actor"."last_name", 2, LENGTH("actor"."last_name"))))) ,
	', ')) AS "actors"
FROM
	(((("category"
LEFT JOIN "film_category" ON
	(("category"."category_id" = "film_category"."category_id")))
LEFT JOIN "film" ON
	(("film_category"."film_id" = "film"."film_id")))
JOIN "film_actor" ON
	(("film"."film_id" = "film_actor"."film_id")))
JOIN "actor" ON
	(("film_actor"."actor_id" = "actor"."actor_id")))
GROUP BY
	"film"."film_id",
	"category"."name";
	
-- 在actor_info视图上添加注释
COMMENT ON VIEW "XIAOKANG_SAKILA"."actor_info" IS 'VIEW';
-- 在film_list视图上添加注释
COMMENT ON VIEW "XIAOKANG_SAKILA"."film_list" IS 'VIEW';
-- 在nicer_but_slower_film_list视图上添加注释
COMMENT ON VIEW "XIAOKANG_SAKILA"."nicer_but_slower_film_list" IS 'VIEW';

alter table xiaokang_sakila.actor  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.address  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.category  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.city  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.country  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.customer  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.film  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.film_actor  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.film_category  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.inventory  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.language  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.payment  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.rental  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.staff  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();
alter table xiaokang_sakila.store  modify  last_update timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP();

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_actor
BEFORE UPDATE ON xiaokang_sakila."actor"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_address
BEFORE UPDATE ON xiaokang_sakila."address"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_category
BEFORE UPDATE ON xiaokang_sakila."category"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_city
BEFORE UPDATE ON xiaokang_sakila."city"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_country
BEFORE UPDATE ON xiaokang_sakila."country"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_customer
BEFORE UPDATE ON xiaokang_sakila."customer"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_film
BEFORE UPDATE ON xiaokang_sakila."film"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_film_actor
BEFORE UPDATE ON xiaokang_sakila."film_actor"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_film_category
BEFORE UPDATE ON xiaokang_sakila."film_category"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_inventory
BEFORE UPDATE ON xiaokang_sakila."inventory"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_language
BEFORE UPDATE ON xiaokang_sakila."language"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_payment
BEFORE UPDATE ON xiaokang_sakila."payment"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_rental
BEFORE UPDATE ON xiaokang_sakila."rental"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_staff
BEFORE UPDATE ON xiaokang_sakila."staff"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/

CREATE OR REPLACE TRIGGER SAKILA_TRIGGER_UPDATE_store
BEFORE UPDATE ON xiaokang_sakila."store"
FOR EACH ROW 
BEGIN
  :NEW."last_update" = CURRENT_TIMESTAMP();
END;	
/
```

![xiaokangxxs-image-20221203151709511](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203151709511.3ssec3g602i0.jpg)

### 3.4 验证

使用DBeaver UE连接DM8，执行以下语句查看表情况与表数据情况：

```sql
select owner,table_name from dba_tables where owner='xiaokang_sakila';
select * from "actor" ;
```

![xiaokangxxs-image-20221203172221102](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203172221102.13pwsa88zkyk.jpg)

![xiaokangxxs-image-20221203172349547](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203172349547.2yzfp0jgkay0.jpg)

## 四、manager授权

### 4.1 访问修改其它表空间

使用DBeaver UE连接DM8，执行以下语句：

```sql
select * from xiaokang_system.Account;
insert into xiaokang_system.Account (name) values ('小康新鲜事儿');
-- 使用sysdba才能回收DBA权限
revoke "DBA" from xiaokang_sakila;
```

有DBA权限时，能对其它表空间的表数据进行增删改查操作

![xiaokangxxs-image-20221203173318915](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203173318915.plcdzskv1xc.jpg)

回收DBA权限后，对其它表空间的表数据没有任何权限

![xiaokangxxs-image-20221203173449799](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-image-20221203173449799.5iq7pf8r06o0.jpg)

通过manager工具（使用sysdba用户）授权xiaokang_system表空间的权限后，能够进行增删改查操作

![xiaokangxxs-manager授权](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-manager授权.42g2opimdgy0.gif)

### 4.2 备份数据库

通过manager工具（使用sysdba用户）授权xiaokang_system备份数据库权限

![xiaokangxxs-manager授备份数据库权限](https://cdn.staticaly.com/gh/xiaokangxxs/images@master/dm/xiaokangxxs-manager授备份数据库权限.31y9p5qb2j00.gif)

**sql脚本获取方式：微信关注公众号：小康新鲜事儿，回复【sakila】获取**