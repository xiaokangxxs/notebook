package cool.xiaokang.task;

import cool.xiaokang.WritableDemo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;

/**
 * 统计每个客户访问网站最多，前两名
 *
 * @author xiaokang
 */
public class SiteTask1 {
    //日志
    private static Logger logger = Logger.getLogger(WritableDemo.class);
    //硬编码方式固定URL和USER
    private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    //自定义Mapper
    static class SiteMapper extends Mapper<LongWritable, Text, Text, Text> {
        //域名
        Text domain;
        //客户
        Text customer;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            domain = new Text();
            customer = new Text();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] info = line.split("\"");
            domain.set(info[1]);
            String[] customers = info[0].split(" ");
            customer.set(customers[0]);
            context.write(customer, domain);
        }
    }

    //自定义Reducer
    static class SiteReducer extends Reducer<Text, Text, Text, IntWritable> {
        Text customer;
        IntWritable visits;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            customer = new Text();
            visits = new IntWritable();
        }

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (Text value : values) {
                count++;
            }
            customer.set(key.toString());
            visits.set(count);
            context.write(customer, visits);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text("执行成功--微信公众号：小康新鲜事儿"), null);
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
            //创建一个Job作业
            Job job = Job.getInstance(conf, "统计每个客户访问网站最多前两名-微信公众号：小康新鲜事儿");
            job.setJarByClass(SiteTask1.class);
            //设置Mapper
            job.setMapperClass(SiteMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            //设置Reducer
            job.setReducerClass(SiteReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            //如果输出目录已存在，则先删除，否则程序运行时会抛出异常
            FileSystem fs = FileSystem.get(new URI(HDFS_URL), conf, HDFS_USER);
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
                System.out.println("已删除原有路径~~~");
                logger.info("已删除原有路径~~~");
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


}
