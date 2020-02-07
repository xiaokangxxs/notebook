# 大数据概论及Hadoop生态系统

## 1.大数据概论

### 1.1 大数据概念

大数据（Big Data）：指无法在**一定时间范围内**用常规软件工具进行捕捉、管理和处理的**数据集合**。

`数据存储单位：bit、Byte、KB、MB、GB、TB、PB、EB、ZB、YB、BB、NB、DB`

### 1.2 大数据特征

**5V特性**

- Volume（ [ˈvɒljuːm] **大量**）
- Velocity（ [vəˈlɒsəti] **高速**）
- Variety（ [vəˈraɪəti] **多样**）：结构化数据（关系型数据）、半结构化数据（xml、json）、非结构化数据（网络日志、视频、图片、地理位置信息等）
- Value（ [ˈvæljuː] **低价值密度**）
- Veracity（ [vəˈræsəti] **真实**）

### 1.3 大数据应用场景

1. 物流仓储：大数据分析系统助力商家精细化运营、提升销量、节约成本
2. 零售：分析用户消费习惯，为用户购买商品提供方便，从而提升商品销量
3. 旅游：深度结合大数据能力与旅游行业需求，共建旅游产业智慧管理、服务和营销
4. 商品广告推荐：给用户推荐可能喜欢的商品
5. 保险：海量数据挖掘及风险预测，助力保险行业精准营销，提升精细化定价能力
6. 金融：多维度体现用户特征，帮助金融机构推荐优质客户，防范欺诈风险
7. 房产：大数据全面助力房地产行业，打造精准投策与营销，选出更合适的地，建造更合适的楼，卖给更合适的人
8. 人工智能

### 1.4 大数据部门业务流程分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/transaction.png"/> </div>

### 1.5 大数据部门组织结构

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/department.png"/> </div>

## 2.什么是分布式计算

把一个需要非常巨大的计算能力才能解决的问题**分成许多小的部分**，然后把这些部分分配给多个计算机进行**并行**处理，最后把这些计算结果综合起来得到最终的结果。 

|              | 传统分布式计算       | 新的分布式计算-Hadoop  |
| ------------ | -------------------- | ---------------------- |
| 计算方式     | 将数据复制到计算节点 | 在不同数据节点并行计算 |
| 可处理数据量 | 小数据量             | 大数据量               |
| CPU性能限制  | 受CPU限制较大        | 受单台设备限制小       |
| 提升计算力   | 提升单台机器计算能力 | 扩展低成本服务器集群   |

## 3.Hadoop深入理解

### 3.1 什么是Hadoop

Hadoop是一个由Apache基金会所开发的**开源**的**分布式**系统基础架构，用来解决海量数据的**存储**和海量数据的**分析计算**问题。

三个重要的组成部分：

1. 分布式文件存储系统 —— HDFS
2. 分布式计算框架 —— MapReduce
3. 集群资源管理器 —— YARN

### 3.2 什么是Hadoop生态圈

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/circle.png"/> </div>

### 3.3 Hadoop的发展及其版本

- Hadoop发展史

  1）Lucene是Doug Cutting开创的开源软件，用Java书写代码，实现与Google类似的全文搜索功能，它提供了全文检索引擎的架构，包括完整的查询引擎和索引引擎

  2）2001年年底成为Apache基金会的一个子项目

  3）对于大数量的场景，Lucene面对与Google一样的困难，Doug Cutting通过学习和模仿Google解决这些问题创造了：微型版Nutch

  4）Google是Hadoop的思想之源（Google在大数据方面的三大论文）

  ​		GFS->HDFS

  ​		Map-Reduce->MR

  ​		BigTable->Hbase

  5）2003-2004年，Google公开了部分GFS和MapReduce思想的细节，以此为基础Doug Cutting用了两年业余时间实现了DFS和MapReduce机制，使得Nutch性能飙升

  6）2005年Hadoop作为Lucene的子项目Nutch的一部分正式引入Apache基金会。2006年3月份，MapReduce和Nutch Distributed File System（NDFS）分别被纳入称为Hadoop的项目中

  7）项目名字来源于Doug Cutting儿子的玩具大象

- Hadoop三大发行版本

  Hadoop三大发行版本：**Apache**、**Cloudera**、**Hortonworks**

  Apache版本最原始（最基础）的版本，对于入门学习最好

  Cloudera在大型互联网企业中用的较多

  Hortonworks文档较好

  Apache Hadoop官网：http://hadoop.apache.org/

  Cloudera Hadoop官网： https://www.cloudera.com/ 

  Hortonworks Hadoop官网： https://hortonworks.com/ （目前Hortonworks已经和Cloudera合并）

### 3.4 Hadoop优势

- 高可靠性：Hadoop底层维护多个数据副本
- 高扩展性：在集群间分配任务数据，可方便的横向扩展数以千计的节点
- 高效性：在MapReduce的思想下，Hadoop是并行工作的，以加快任务处理速度
- 高容错性：能自动将失败的任务重新分配

### 3.5 Hadoop生态圈理解

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/circle-1.png"/> </div>

1）HDFS （Hadoop Distributed File System）是Hadoop下的分布式文件系统，具有高容错(fault-tolerant)、高吞吐量(high throughput)等特性，可以部署在低成本(low-cost)的硬件上。 

2）Apache YARN (Yet Another Resource Negotiator)  是 hadoop 2.0 引入的集群资源管理系统。用户可以将各种服务框架部署在 YARN 上，由 YARN 进行统一地管理和资源分配。 

3）MapReduce 是一个分布式计算框架，用于编写批处理应用程序。编写好的程序可以提交到 Hadoop 集群上用于并行处理大规模的数据集。 

4）Sqoop：sqoop是一款开源的工具，主要用于在Hadoop（Hive）与传统的关系型数据库（MySQL、Oracle）间进行数据的传递，可以将一个关系型数据库中的数据导入到Hadoop的HDFS中，也可以将HDFS的数据导出到关系型数据库中。

5）Flume：Flume是Cloudera提供的一个高可用的、高可靠的、分布式的海量日志收集、聚合和传输的系统，Flume支持在日志系统中定制各类数据发送方，用于收集数据，同时，Flume提供对数据进行简单处理，并写到各种数据接收方（可定制）的能力。

6）HBase：HBase是一个分布式的、面向列的开源数据库。HBase不同于一般的关系型数据库，它是一个适合于非结构化数据存储的数据库。

7）Mahout：Apache Mahout是个可扩展的机器学习和数据挖掘库。

8）Zookeeper：Zookeeper是Google的Chubby一个开源的实现。它是一个针对大型分布式系统的可靠协调系统，提供的功能包括：配置维护、名字服务、分布式同步、组服务等。Zookeeper的目标就是封装好复杂容易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。

9）Hive：Hive是基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张数据库表，并提供简单的sql查询功能，可以将sql语句转换为MapReduce统计，不必开发专门的MapReduce应用，十分适合数据仓库的统计分析。

10）Spark：Spark是当前最流行的开源大数据内存计算框架。可以基于Hadoop上存储的大数据进行计算。

11）Apache Pig是一个高级过程语言，是MapReduce的一个抽象，它是一个工具/平台，用于分析较大的数据集，使用一种较为简便的类似于SQL的面向数据流的语言Pig Latin进行数据处理。

12）Tez：Tez是一个Apache的开源项目，意在构建一个应用框架，能通过复杂任务的DAG来处理数据。它是基于当前的Hadoop Yarn之上，换句话就是Yarn为其提供资源。Tez主要的两个设计目标:

| 目标             | 具体说明                                                     |
| ---------------- | ------------------------------------------------------------ |
| 增强终端用户使用 | 灵活的数据流定义API；灵活的输入输出运行时模型(强调处理模型)；数据类型无关；简洁部署 |
| 高性能执行       | 通过MapReduce提高性能；资源优化管理；执行时计划重定义；物理数据流的动态决策 |

13）Presto：Presto是一个开源的分布式SQL查询引擎，适用于交互式分析查询，是一种Massively parallel processing (MPP)架构，多个节点管道式执⾏，⽀持任意数据源（通过扩展式Connector组件），数据量支持GB到PB字节。 

14）Impala：Impala是Cloudera公司主导开发的新型查询系统，它提供SQL语义，能查询存储在Hadoop的HDFS和HBase中的PB级大数据。Impala支持内存中数据处理，它访问/分析存储在Hadoop数据节点上的数据，而无需数据移动。支持各种文件格式，如LZO、序列文件、Avro、RCFile和Parquet。

15）HCatalog：HCatalog是Hadoop的表存储管理工具。它将Hive Metastore的表格数据公开给其他Hadoop应用程序。使得具有不同数据处理工具（Pig，MapReduce）的用户能够轻松将数据写入网格。它确保用户不必担心数据存储在何处或以何种格式存储。HCatalog像Hive的一个关键组件一样工作，它使用户能够以任何格式和任何结构存储他们的数据。HCatalog可以为正确的作业启用正确的工具，捕获处理状态以启用共享，将Hadoop与其他一切集成在一起。

16）Kafka：Kafka是一种高吞吐量的分布式发布订阅消息系统，有如下特性：

| 序号 | 特性                                                         |
| ---- | ------------------------------------------------------------ |
| 1    | 通过O(1)的磁盘数据结构提供消息的持久化，这种结构对于即使数以TB的消息存储也能保持长时间的稳定性能。 |
| 2    | 即使是非常普通的硬件Kafka也可以支持每秒数百万的消息          |
| 3    | 支持通过Kafka服务器和消费机集群来分区消息                    |
| 4    | 支持Hadoop并行数据加载                                       |

17）Storm：Storm为分布式实时计算提供了一组通用原语，可被用于“流处理”之中，实时处理消息并更新数据库。这是管理队列以及工作者集群的另一种方式。Storm也可被用于“连续计算”（continuous computation），对数据流做连续查询，在计算时就将结果以流的形式输出给用户。

18）Oozie：Oozie是一个管理Hadoop作业（Job）的工作流程调度管理系统。Oozie协调作业就是通过时间（频率）和有效数据触发当前的Oozie工作流程。

19）R语言：R是用于统计分析、绘图的语言和操作环境。R是属于GNU系统的一个自由、免费、源代码开放的软件，它是一个用于统计计算和统计制图的优秀工具。

### 3.6 Hadoop技术生态体系

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/ecology.png"/> </div>