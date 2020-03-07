# MapReduce排序案例

## 一、统计手机用户信息

```properties
【待处理数据】
手机号1，mac地址，网址域名ip,网址，网站类型,当天的上行量，下行量，访问状态码，日期
1363157985066 00-FD-07-A4-72-B8:CMCC 120.196.100.82 i02.c.aliimg.com 财经 2481 24681 200 2018-1-1
1363157985066 00-FD-07-A4-72-B8:CMCC 120.196.100.82 i02.c.aliimg.com 财经 20 100 200 2018-1-1
1363157985066 00-FD-07-A4-72-B8:CMCC 120.196.100.82 i02.c.aliimg.com 财经 30 200 200 2018-3-1
1363157985066 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 3056 2936 200 2018-6-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 3156 2936 200 2018-6-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 3056 2936 200 2018-6-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 315 3936 400 2019-3-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 3000 2000 400 2018-3-1
1383157995033 00-FD-07-A4-72-B8:CMCC 120.196.100.82 i02.c.aliimg.com 财经 30 200 200 2018-3-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 316 2836 200 2018-6-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 3356 2936 200 2018-6-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 100 300 200 2018-1-1
1383157995033 5C-0E-8B-C7-BA-20:CMCC 120.197.40.4 sug.so.360.cn 信息安全 200 200 200 2018-1-1
1383157993044 94-71-AC-CD-E6-18:CMCC-EASY 120.196.100.99 iface.qiyi.com 视频网站 100 200 200 2018-3-1
1383157993044 94-71-AC-CD-E6-18:CMCC-EASY 120.196.100.99 iface.qiyi.com 视频网站 200 300 200 2018-3-1
1383157993044 94-71-AC-CD-E6-18:CMCC-EASY 120.196.100.99 iface.qiyi.com 视频网站 200 300 200 2018-1-1
1383157993044 94-71-AC-CD-E6-18:CMCC-EASY 120.196.100.99 iface.qiyi.com 视频网站 200 300 200 2018-1-1
1383157993044 94-71-AC-CD-E6-18:CMCC-EASY 120.196.100.99 iface.qiyi.com 视频网站 1527 2106 200 2018-3-1
1363157995052 5C-0E-8B-C7-F1-E0:CMCC 120.197.40.4   264 0 200  2018-1-1
1363157991076 20-10-7A-28-CC-0A:CMCC 120.196.100.99  132 1512 200  2018-1-2
1363154400022 5C-0E-8B-8B-B1-50:CMCC 120.197.40.4   240 0 200  2018-3-1
1383157995074 5C-0E-8B-8C-E8-20:7DaysInn 120.197.40.4 122.72.52.12  4116 1432 200 2018-4-4
1383157993055 C4-17-FE-BA-DE-D9:CMCC 120.196.100.99  1116 954 200 2018-5-1
1363157983019 68-A1-B7-03-07-B1:CMCC-EASY 120.196.100.82  240 0 200 2018-8-2
1533157993044 94-71-AC-CD-E6-18:CMCC-EASY 120.196.100.99 iface.tengxu.com 视频网站 1527 2106 200 2018-3-1
```

### 1. 统计每年、每月各个网站的总的上行流量和下行流量

`由于是统计每年、每月各个网站的总的上行和下行流量不涉及排名，所以首先确定不需要自定义Bean`
预期输出结果格式：*year-month,网站地址，总上行流量，总下行流量*

```java
String[] info = value.toString().split(" ");
```

**有效数据中一共9部分**：info[3]:网址、info[info.length - 1]:日期、info[info.length - 4]:上行流量、info[info.length - 3]：下行流量

编码前的分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/flow-1.png"/> </div>
由于是统计总的上行和下行总流量，所以可以使用本地Combiner（这里我就省略了，逻辑和Reducer的一样）

```java
private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
private static final String HDFS_USER = "xiaokang";
//自定义Mapper
static class FlowMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text siteIP = new Text();
        Text flow = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            try {
                //过滤第一行
                if (!line.startsWith("手")) {
                    String[] info = line.split(" ");
                    //去除无效数据
                    if (info.length == 9 && info[3].length() > 1) {
                        String[] time = info[info.length - 1].split("-");
                        siteIP.set(time[0] + "-" + time[1] + "," + info[3]);
                        flow.set(Long.parseLong(info[info.length - 4]) + "-" + Long.parseLong(info[info.length - 3]));
                        context.write(siteIP, flow);
                    }
                 }
            } catch (Exception e) {
                System.out.println("execute failed>>>" + line);
                System.out.println(e.getMessage());
            }
        }
    }
//自定义Reducer
static class FlowReducer extends Reducer<Text, Text, Text, NullWritable> {
        Text result = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            long sumUpFlow = 0;
            long sumDownFlow = 0;
            for (Text value : values) {
                String[] flow = value.toString().split("-");
                sumUpFlow += Long.parseLong(flow[0]);
                sumDownFlow += Long.parseLong(flow[1]);
            }
            result.set(key.toString() + "," + sumUpFlow + "," + sumDownFlow);
            context.write(result, NullWritable.get());
        }
    }
public static void main(String[] args) {
        //  文件输入路径和输出路径由外部传参指定
        if (args == null || args.length < 2) {
            System.out.println("Input and Output paths are necessary!");
            return;
        }
        //编写Driver类
        try {
            //配置类，指定HDFS的路径
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", HDFS_URL);
            //指定结果文件的文件名前缀，默认part
            conf.set("mapreduce.output.basename", "flow-1");
            //指定结果文件中键和值之间的分隔符，默认为\t
            conf.set("mapreduce.output.textoutputformat.separator", "--");
            //创建一个Job作业
            Job job = Job.getInstance(conf, "流量第一次-微信公众号：小康新鲜事儿");
            job.setJarByClass(FlowTask.class);
            //设置Mapper
            job.setMapperClass(FlowMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            //设置Reducer
            job.setReducerClass(FlowReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            //如果输出目录已存在，则先删除，否则程序运行时会抛出异常
            FileSystem fs = FileSystem.get(new URI(HDFS_URL), conf, HDFS_USER);
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
                System.out.println("已删除原有路径~~~");
            }
            //指定作业的输入文件和输出结果文件的路径
            FileInputFormat.setInputPaths(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, outputPath);
            //将作业提交到集群并等待完成，参数设置true代表打印显示对应的进度
            boolean result = job.waitForCompletion(true);
            if (result) {
                System.out.println(job.getJobName() + ">>>execute success！");
            } else {
                System.out.println(job.getJobName() + ">>>execute failed！");
            }
        } catch (Exception e) {
            System.out.println("程序执行异常" + e.getMessage());
        }
    }
```

### 2. 统计每年、每月各个网站的总的上行流量排名前两名的网站

```properties
【部分待处理数据】（上一题处理结果）
2018-1,i02.c.aliimg.com,2531,24981
2018-6,sug.so.360.cn,9268,8808
```

编码前的分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/flow-2.png"/> </div>
`由于涉及排名，所以首先确定需要自定义Bean`

```java
//年
private int year;
//月
private int month;
//上行流量
private long upFlow;

//先按年升序，再按月升序，最后按总的上行流量降序
public int compareTo(Flow o) {
    int r1 = this.year - o.getYear();
    if (r1 == 0) {
        int r2 = this.month - o.getMonth();
        if (r2 == 0) {
            return (int) (o.getUpFlow() - this.upFlow);
        }
        return r2;
    }
    return r1;
}
```

核心代码

```java
//自定义Mapper
static class FlowMapper extends Mapper<LongWritable, Text, Flow, Text> {
    Flow flow = new Flow();
    Text site_upflow = new Text();
    
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            String[] info = line.split(",");
            if (info.length == 4) {
                String[] time = info[0].split("-");
                flow.setYear(Integer.parseInt(time[0]));
                flow.setMonth(Integer.parseInt(time[1]));
                flow.setUpFlow(Long.parseLong(info[2]));
                site_upflow.set(info[1] + "," + Long.parseLong(info[2]));
                context.write(flow, site_upflow);
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
    }
}
//自定义分组，同年同月为一组
static class FlowGroupCmoparator extends WritableComparator {
    public FlowGroupCmoparator() {
        super(Flow.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Flow flowA = (Flow) a;
        Flow flowB = (Flow) b;
        int r1 = flowA.getYear() - flowB.getYear();
        if (r1 == 0) {
            return flowA.getMonth() - flowB.getMonth();
        }
        return r1;
    }
}
//自定义Reducer
static class FlowReducer extends Reducer<Flow, Text, Text, NullWritable> {
    Text result = new Text();
    TreeSet<Long> treeSet = null;

    @Override
    protected void reduce(Flow key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        treeSet = new TreeSet<Long>();
        String[] site_upflow={};
        for (Text value : values) {
            site_upflow = value.toString().split(",");
            long upFlow = Long.valueOf(site_upflow[1]);
            if (treeSet.size() >= 2) {
                if (upFlow > treeSet.first()) {
                    treeSet.remove(treeSet.first());
                    treeSet.add(upFlow);
                }
            } else {
                treeSet.add(upFlow);
            }
        }
        for (Long aLong : treeSet) {
            result.set(key.getYear() + "-" + key.getMonth() + "," + site_upflow[0] + "," + aLong);
            context.write(result, NullWritable.get());
        }
    }
}
public static void main(String[] args) {
        //  文件输入路径和输出路径由外部传参指定
        if (args == null || args.length < 2) {
            System.out.println("Input and Output paths are necessary!");
            return;
        }
        //编写Driver类
        try {
            //配置类，指定HDFS的路径
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", HDFS_URL);
            //指定结果文件的文件名前缀，默认part
            conf.set("mapreduce.output.basename", "flow-2");
            //指定结果文件中键和值之间的分隔符，默认为\t
            //conf.set("mapreduce.output.textoutputformat.separator", "--");
            //创建一个Job作业
            Job job = Job.getInstance(conf, "流量第二次-微信公众号：小康新鲜事儿");
            job.setJarByClass(FlowTask2.class);
            //设置Mapper
            job.setMapperClass(FlowMapper.class);
            job.setMapOutputKeyClass(Flow.class);
            job.setMapOutputValueClass(Text.class);
            //设置Reducer
            job.setReducerClass(FlowReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            //设置分组，同年同月为一组
            job.setGroupingComparatorClass(FlowGroupCmoparator.class);
            //如果输出目录已存在，则先删除，否则程序运行时会抛出异常
            FileSystem fs = FileSystem.get(new URI(HDFS_URL), conf, HDFS_USER);
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
                System.out.println("已删除原有路径~~~");
            }
            //指定作业的输入文件和输出结果文件的路径
            FileInputFormat.setInputPaths(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, outputPath);
            //将作业提交到集群并等待完成，参数设置true代表打印显示对应的进度
            boolean result = job.waitForCompletion(true);
            if (result) {
                System.out.println(job.getJobName() + ">>>execute success！");
            } else {
                System.out.println(job.getJobName() + ">>>execute failed！");
            }
        } catch (Exception e) {
            System.out.println("程序执行异常" + e.getMessage());
        }
    }
```

```java
//自定义Reducer第二种写法
static class FlowReducer extends Reducer<Flow, Text, Text, NullWritable> {
    Text result = new Text();
    TreeSet<Long> treeSet = null;

    @Override
    protected void reduce(Flow key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        treeSet = new TreeSet<Long>();
        String[] site_upflow = {};
        for (Text value : values) {
            site_upflow = value.toString().split(",");
            long sumFlow = Long.valueOf(site_upflow[1]);
            if (treeSet.size() < 2) {
                treeSet.add(sumFlow);
            } else {
                break;
            }
        }
        result.set(key.getYear() + "-" + key.getMonth() + "," + site_upflow[0] + "," + treeSet.last());
        context.write(result, NullWritable.get());
        result.set(key.getYear() + "-" + key.getMonth() + "," + site_upflow[0] + "," + treeSet.first());
        context.write(result, NullWritable.get());
    }
}
```

```java
private static final int TOPN = 2;
//自定义Reducer第三种写法
static class ShopReducer extends Reducer<Flow, Text, Text, NullWritable> {
    Text result = new Text();
    int i = 0;

    @Override
    protected void reduce(Flow key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            site_upflow = value.toString().split(",");
            long sumFlow = Long.valueOf(site_upflow[1]);
            if (i < TOPN) {
                result.set(key.getYear() + "-" + key.getMonth() + "," + site_upflow[0] + "," + sumFlow);
                context.write(result, NullWritable.get());
                i++;
            } else {
                break;
            }
        }
    }
}
```

### 3. 统计每年、每月各个网站的总流量排后两名的网站

```properties
【部分待处理数据】（第一题处理结果）
2018-1,i02.c.aliimg.com,2531,24981
2018-6,sug.so.360.cn,9268,8808
```

编程前的分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/flow-3.png"/> </div>
`由于涉及排名，所以首先确定需要自定义Bean`

```java
//年
private int year;
//月
private int month;
//总流量
private long sumFlow;

//先按年升序，再按月升序，最后按总流量升序
@Override
public int compareTo(Flow o) {
    int r1 = this.year - o.getYear();
    if (r1 == 0) {
        int r2 = this.month - o.getMonth();
        if (r2 == 0) {
            return (int) (this.sumFlow - o.getSumFlow());
        }
        return r2;
    }
    return r1;
}
```

核心代码

```java
//自定义Mapper
static class FlowMapper extends Mapper<LongWritable, Text, Flow, Text> {
    Flow flow = new Flow();
    Text site_sumflow = new Text();
    long sumFlow = 0;
    
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            String[] info = line.split(",");
            if (info.length == 4) {
                String[] time = info[0].split("-");
                sumFlow = Long.parseLong(info[2])+Long.parseLong(info[3]);
                flow.setYear(Integer.parseInt(time[0]));
                flow.setMonth(Integer.parseInt(time[1]));
                flow.setSumFlow(sumFlow);
                site_sumflow.set(info[1] + "," + sumFlow);
                context.write(flow, site_sumflow);
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
    }
}
//自定义分组，同年同月为一组
static class FlowGroupCmoparator extends WritableComparator {
    public FlowGroupCmoparator() {
        super(Flow.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Flow flowA = (Flow) a;
        Flow flowB = (Flow) b;
        int r1 = flowA.getYear() - flowB.getYear();
        if (r1 == 0) {
            return flowA.getMonth() - flowB.getMonth();
        }
        return r1;
    }
}
//自定义Reducer
static class FlowReducer extends Reducer<Flow, Text, Text, NullWritable> {
    Text result = new Text();
    TreeSet<Long> treeSet = null;

    @Override
    protected void reduce(Flow key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        treeSet = new TreeSet<Long>();
        String[] site_sumflow={};
        for (Text value : values) {
            site_sumflow = value.toString().split(",");
            long sumFlow = Long.valueOf(site_sumflow[1]);
            if (treeSet.size() >= 2) {
                if (upFlow < treeSet.first()) {
                    treeSet.remove(treeSet.last());
                    treeSet.add(sumFlow);
                }
            } else {
                treeSet.add(sumFlow);
            }
        }
        for (Long aLong : treeSet) {
            result.set(key.getYear() + "-" + key.getMonth() + "," + site_sumflow[0] + "," + aLong);
            context.write(result, NullWritable.get());
        }
    }
}
public static void main(String[] args) {
        //  文件输入路径和输出路径由外部传参指定
        if (args == null || args.length < 2) {
            System.out.println("Input and Output paths are necessary!");
            return;
        }
        //编写Driver类
        try {
            //配置类，指定HDFS的路径
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", HDFS_URL);
            //指定结果文件的文件名前缀，默认part
            conf.set("mapreduce.output.basename", "flow-3");
            //指定结果文件中键和值之间的分隔符，默认为\t
            //conf.set("mapreduce.output.textoutputformat.separator", "--");
            //创建一个Job作业
            Job job = Job.getInstance(conf, "流量第三次-微信公众号：小康新鲜事儿");
            job.setJarByClass(FlowTask3.class);
            //设置Mapper
            job.setMapperClass(FlowMapper.class);
            job.setMapOutputKeyClass(Flow.class);
            job.setMapOutputValueClass(Text.class);
            //设置Reducer
            job.setReducerClass(FlowReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            //设置分组，同年同月为一组
            job.setGroupingComparatorClass(FlowGroupCmoparator.class);
            //如果输出目录已存在，则先删除，否则程序运行时会抛出异常
            FileSystem fs = FileSystem.get(new URI(HDFS_URL), conf, HDFS_USER);
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
                System.out.println("已删除原有路径~~~");
            }
            //指定作业的输入文件和输出结果文件的路径
            FileInputFormat.setInputPaths(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, outputPath);
            //将作业提交到集群并等待完成，参数设置true代表打印显示对应的进度
            boolean result = job.waitForCompletion(true);
            if (result) {
                System.out.println(job.getJobName() + ">>>execute success！");
            } else {
                System.out.println(job.getJobName() + ">>>execute failed！");
            }
        } catch (Exception e) {
            System.out.println("程序执行异常" + e.getMessage());
        }
    }
```

### 4. 统计136、138手机用户，在2018年访问各个网站总流量排名第一的用户

```properties
【待处理数据就是最开始的数据】
分为两个阶段：
	第一阶段：先输出136、138用户2018年访问各个网站的总流量
	第二节阶段：对第一阶段的总流量进行排序
```

第一阶段编程前分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/flow-4-1.png"/> </div>
核心代码

```java
//自定义Mapper
//info[3]:网址,info[info.length - 1]:日期
// info[info.length - 4]：上行流量，info[info.length - 3]：下行流量
static class FlowMapper extends Mapper<LongWritable, Text, Text, Text> {
    Text site_phone = new Text();
    Text sumFlow = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (line.startsWith("手")) {
            return;
        }
        try {
            String[] info = line.split(" ");
            if (info.length == 9 && info[3].length() > 1) {
                if (info[0].startsWith("136") || info[0].startsWith("138")) {
                    String[] time = info[info.length - 1].split("-");
                    if (time[0].equals("2018")) {
                        site_phone.set(info[3] + "," + info[0]);
                        sumFlow.set(Long.valueOf(info[info.length - 4]) + Long.valueOf(info[info.length - 3]) + "");
                        context.write(site_phone, sumFlow);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
    }
}
//自定义Reducer
static class FlowReducer extends Reducer<Text, Text, Text, NullWritable> {
    Text result = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long sumFlow = 0;
        for (Text value : values) {
            sumFlow += Long.valueOf(value.toString());
        }
        result.set(key.toString() +","+ sumFlow);
        context.write(result, NullWritable.get());
    }
}
```

第二阶段编程前分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/flow-4-2.png"/> </div>
`由于涉及排名，所以首先确定需要自定义Bean`

```java
//网址
private String site;
//手机号
private String phoneNum;
//总流量
private long sumFlow;

//先按网站升序，再按总流量降序
@Override
public int compareTo(Flow222 o) {
    int r1 = this.site.compareTo(o.getSite());
    if (r1 == 0) {
        return (int) (o.getSumFlow() - this.sumFlow);
    }
    return r1;
}
```

核心代码

```java
//自定义Mapper
static class FlowMapper extends Mapper<LongWritable, Text, Flow, NullWritable> {
    Flow flow = new Flow();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            String[] info = line.split(",");
            flow.setSite(info[0]);
            flow.setPhoneNum(info[1]);
            flow.setSumFlow(Long.valueOf(info[2]));
            context.write(flow, NullWritable.get());
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
    }
}
//自定义分组，同一个网站为一组
static class FlowGroupCmoparator extends WritableComparator {
    public FlowGroupCmoparator() {
        super(Flow.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Flow flowA = (Flow) a;
        Flow flowB = (Flow) b;
        return flowA.getSite().compareTo(flowB.getSite());
    }
}
//自定义Reducer，3次
static class FlowReducer extends Reducer<Flow, NullWritable, Text, NullWritable> {
    Text result = new Text();

    @Override
    protected void reduce(Flow key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        result.set(key.getSite() + "," + key.getPhoneNum() + "," + key.getSumFlow());
        context.write(result, NullWritable.get());
    }
}
```

## 二、统计营业额信息

```properties
【待处数据】
账户,营业额,花费,日期
zhangsan@163.com 6000 0 2017-02-20
lisi@163.com 2000 0 2018-02-20
lisi@163.com 0 100 2017-02-60
zhangsan@163.com 3000 0 2017-03-23
wangwu@126.com 9000 0 2018-05-26
wangwu@126.com 0 200 2017-01-20
wangwu@126.com 1 200 2019-01-20
wangwu@126.com 2 200 2019-02-20
```

### 1. 计算出每个加盟店账户的总营业额和总花费（营业额降序,花费升序）

```properties
分为两个阶段：
	第一阶段：先输出每个加盟店账户的总营业额和总花费
	第二节阶段：对第一阶段的总营业额降序,总花费升序
```

第一阶段编程前的分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/shop-1-1.png"/> </div>
核心代码

```java
//自定义Mapper
static class ShopMapper extends Mapper<LongWritable, Text, Text, Text> {
    Text shopAccount = new Text();
    Text turnover_cost = new Text();
    String yearParams = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //只初始化一次
        yearParams = context.getConfiguration().get("yearParams");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (line.startsWith("账户")) {
            return;
        }
        try {
            String[] info = line.split(" ");
            if (info.length == 4) {
                String[] time = info[3].split("-");
                if (yearParams == null || TimeUtils.checkYear(yearParams, time[0])) {
                    shopAccount.set(info[0]);
                    turnover_cost.set(Integer.parseInt(info[1]) + "," + Integer.parseInt(info[2]));
                    context.write(shopAccount, turnover_cost);
                }
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
    }
}
//自定义Reducer
static class ShopReducer extends Reducer<Text, Text, Text, NullWritable> {
    Text firstResult = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int sumTurnover = 0;
        int sumCost = 0;
        for (Text value : values) {
            String[] shop = value.toString().split(",");
            sumTurnover += Integer.valueOf(shop[0]);
            sumCost += Integer.valueOf(shop[1]);
        }
        firstResult.set(key.toString() + "," + sumTurnover + "," + sumCost);
        context.write(firstResult, NullWritable.get());
    }
}
```

第二阶段编程前的分析

<div align="center"> <img width="600px" src="https://raw.githubusercontent.com/xiaokangxxs/notebook/master/docs/BigData/Hadoop/shop-1-2.png"/> </div>
`涉及排序，所以确定需要自定义Bean`

```java
//账户
private String shopAccount;
//营业额
private int turnover;
//花费
private int cost;

//营业额降序,花费升序
@Override
public int compareTo(Shop o) {
    int r1 = o.getTurnover() - this.turnover;
    if (r1 == 0) {
        return this.cost - o.getCost();
    }
    return r1;
}
```

核心代码

```java
//自定义Mapper
static class ShopMapper extends Mapper<LongWritable, Text, Shop, Text> {
    Shop shop = new Shop();
    Text shopAccount=new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            String[] info = line.split(",");
            if (info.length == 3){
                shopAccount.set(info[0]);
                shop.setShopAccount(info[0]);
                shop.setTurnover(Integer.parseInt(info[1]));
                shop.setCost(Integer.parseInt(info[2]));
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
        context.write(shop, shopAccount);
    }
}
//自定义Reducer
static class ShopReducer extends Reducer<Shop, Text, Text, NullWritable> {
    Text firstResult = new Text();

    @Override
    protected void reduce(Shop key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        firstResult.set(key.getShopAccount() + "," + key.getTurnover() + "," + key.getCost());
        context.write(firstResult, NullWritable.get());
    }
}
```

### 2. 统计17年、18年营业状况优秀的前两个账户信息

本题目由于是统计17、18年营业状况，后期可能会有18、19，19、20这种需求，所以考虑封装参数

```properties
Configuration conf = new Configuration();
//在Driver中封装Mapper端接收的参数
conf.set("yearParams", args[2]);
参数格式=17,18

//在Mapper端中的setup方法中初始化参数
String yearParams = null;

@Override
protected void setup(Context context) throws Exception{
    //只初始化一次
    yearParams = context.getConfiguration().get("yearParams");
}
```

封装一个工具类TimeUtils

```java
package cool.xiaokang.utils;

/**
 * 时间工具类
 *
 * @author xiaokang
 */
public class TimeUtils {
    /**
     * 将命令行输入的参数和指定参数做比较，相同的留下返回true
     *
     * @param yearParams 命令行输入的年份参数
     * @param dataYear   指定的年份
     * @return boolean
     */
    public static boolean checkYear(String yearParams, String dataYear) {
        String[] years = yearParams.split(",");
        for (String year : years) {
            if (year.equals(dataYear)) {
                return true;
            }
        }
        return false;
    }
}
```

还是分两个阶段，第一阶段先将原始数据处理，中介结果为17、18年账户的信息（和第一题中第一阶段的Mapper一样），第二阶段再排序输出最终结果。

自定义Bean

```java
//账户
private String shopAccount;
//利润
private int totalIncome;

//利润降序
@Override
public int compareTo(Shop o) {
    return o.getTotalIncome() - this.totalIncome;
}
```

核心代码

```java
//自定义Mapper
static class ShopMapper extends Mapper<LongWritable, Text, Shop, NullWritable> {
    Shop shop = new Shop();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            String[] info = line.split(",");
            if (info.length == 3) {
                shop.setShopAccount(info[0]);
                shop.setTotalIncome(Integer.parseInt(info[1])-Integer.parseInt(info[2]));
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
        context.write(shop, NullWritable.get());
    }
}
//自定义Reducer
static class ShopReducer extends Reducer<Shop, NullWritable, Text, NullWritable> {
    Text result = new Text();
    int i = 0;

    @Override
    protected void reduce(Shop1 key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        for (NullWritable value : values) {
            if (i < TOPN) {
                if (key.getTurnover() < key.getCost()) {
                    result.set(key.getShopAccount() + "," + key.getTotalIncome());
                    context.write(result, NullWritable.get());
                    i++;
                }
            } else {
                break;
            }
        }
    }
}
```

### 3. 统计17年、18年营业状况出现问题的两个账户信息（不使用自定义Bean，默认就是按key升序）

核心代码

```java
private static final int TOPN = 2;
//自定义Mapper
static class ShopMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
    LongWritable incomes = new LongWritable();
    Text shop = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            String[] info = line.split(",");
            if (info.length == 3) {
                shop.set(info[0]);
                incomes.set(Integer.parseInt(info[1]) - Integer.parseInt(info[2]));
                context.write(incomes, shop);
            }
        } catch (Exception e) {
            System.out.println("execute failed>>>" + line);
            System.out.println(e.getMessage());
        }
    }
}
//自定义Reducer
static class ShopReducer extends Reducer<LongWritable, Text, Text, NullWritable> {
    Text result = new Text();
    int i = 0;

    @Override
    protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            if (i < TOPN) {
                result.set(key.get() + "," + value.toString());
                context.write(result, NullWritable.get());
                i++;
            } else {
                break;
            }
        }
    }
}
```

