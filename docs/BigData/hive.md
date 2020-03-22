# Hive


## Hive简介及核心概念

`官网`：[https://hive.apache.org/](https://hive.apache.org/) 

### 一、简介

Hive 是一个构建在 Hadoop 之上的数据仓库，能够有效的读取、写入和管理大型数据集合，它可以将结构化的数据文件映射成表，并提供类 SQL 查询功能，用于查询的 SQL 语句会被转化为 MapReduce 作业，然后提交到 Hadoop 上运行。

**特点**：

1. 简单、容易上手 (提供了类似 sql 的查询语言 hql)，避免复杂的 MapReduce的开发，能够节省大量开发成本，使得精通 sql 但是不了解 Java 编程的人也能很好地进行大数据分析；
2. 灵活性高，可以自定义用户函数 (UDF) 和存储格式；
3. Hive本质上作为一个工具，能够支持多种数据分析引擎。Hive可以支持Hadoop的 MapReduce分析引擎，也可以支持Spark、Tez等分析引擎；
4. 统一的元数据管理，可与 presto／impala／sparksql 等共享数据；
5. 执行延迟高，不适合做数据的实时处理，但适合做海量数据的离线处理；
6. Hive提供JDBC服务，可以通过JDBC连接Hive操作HDFS数据，并且可以整合多种BI可视化工具；
7. Hive能够支持多种数据类型和文件格式 。

**缺点**：

1. Hive不支持事务操作；
2. HiveSQL本身表达能力有限，不能够进行迭代式计算以及数据挖掘；
3. Hive操作默认基于MapReduce引擎，延迟比较高不适用于交互式查询。并且基于SQL 
   调优困难。

**Hive应用场景**：

1. Hive可构建基于Hadoop的数据仓库；
2. Hive适合大数据集的批处理作业，比如行为日志分析、多维数据分析；
3. 海量结构化数据离线分析。

### 二、Hive的体系架构

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hive/hive体系架构.png"/> </div>
#### 2.1 command-line shell & thrift/jdbc

可以用 command-line shell 和 thrift／jdbc 两种方式来操作数据：

+ **command-line shell**：通过 hive 命令行的的方式来操作数据；
+ **thrift／jdbc**：通过 thrift 协议按照标准的 JDBC 的方式操作数据。

#### 2.2 Metastore

在 Hive 中，表名、表结构、字段名、字段类型、表的分隔符等统一被称为元数据。所有的元数据默认存储在 Hive 内置的 derby 数据库中，但由于 derby 只能有一个实例，也就是说不能有多个命令行客户端同时访问，所以在实际生产环境中，通常使用 MySQL 代替 derby。

Hive 进行的是统一的元数据管理，就是说你在 Hive 上创建了一张表，然后在 presto／impala／sparksql 中都是可以直接使用的，它们会从 Metastore 中获取统一的元数据信息，同样的你在 presto／impala／sparksql 中创建一张表，在 Hive 中也可以直接使用。

#### 2.3 HQL的执行流程

Hive 在执行一条 HQL 的时候，会经过以下步骤：

1. 语法解析：Antlr 定义 SQL 的语法规则，完成 SQL 词法，语法解析，将 SQL 转化为抽象 语法树 AST Tree；
2. 语义解析：遍历 AST Tree，抽象出查询的基本组成单元 QueryBlock；
3. 生成逻辑执行计划：遍历 QueryBlock，翻译为执行操作树 OperatorTree；
4. 优化逻辑执行计划：逻辑层优化器进行 OperatorTree 变换，合并不必要的 ReduceSinkOperator，减少 shuffle 数据量；
5. 生成物理执行计划：遍历 OperatorTree，翻译为 MapReduce 任务；
6. 优化物理执行计划：物理层优化器进行 MapReduce 任务的变换，生成最终的执行计划。

> 关于 Hive SQL 的详细执行流程可以参考美团技术团队的文章：[Hive SQL 的编译过程](https://tech.meituan.com/2014/02/12/hive-sql-to-mapreduce.html)

## 数据仓库与传统数据库

|                | 数据仓库（Data Warehouse）                                   | 数据库（DataBase）                                           |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 数据量         | >=TB                                                         | 较小，<=GB                                                   |
| 数据种类       | 多样，结构化、半结构化、非结构化                             | 单一，结构化数据                                             |
| 数据来源       | 多样，日志、埋点、爬虫、数据库等                             | 单一，固定的某个应用                                         |
| 获取数据方式   | 一般存储的是历史数据                                         | 实时捕获数据                                                 |
| 针对数据的操作 | 一般只提供增和查                                             | 增删改查                                                     |
| 事务           | 弱事务甚至没有事务                                           | 强调事务                                                     |
| 冗余           | 人为制造冗余                                                 | 精简数据                                                     |
| 场景           | OLAP（Online Analytical Processing）联机分析处理，最终结果面向非技术人员 | OLTP（Online Transaction Processing）联机事务处理，最终结果面向技术人员 |

## Hive安装以及常见配置

[Linux下Hive的安装](https://www.xiaokang.cool/#/BigData/installation/Hive%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B)

