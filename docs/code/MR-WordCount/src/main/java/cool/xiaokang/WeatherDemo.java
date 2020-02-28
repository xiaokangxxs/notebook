package cool.xiaokang;

import cool.xiaokang.pojo.Transaction;
import cool.xiaokang.pojo.Weather;
import cool.xiaokang.utils.NumUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;

/**
 * 分析每年平均温度
 *
 * @author xiaokang
 */
public class WeatherDemo {
    //日志
    private static Logger logger = Logger.getLogger(WritableDemo.class);
    //硬编码方式固定URL和USER
    private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    //WeatherMapper
    static class WeatherMapper extends Mapper<LongWritable, Text, IntWritable, DoubleWritable> {
        IntWritable year;
        DoubleWritable temperature;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            year = new IntWritable();
            temperature = new DoubleWritable();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();

            try {
                year.set(Integer.parseInt(line.substring(0, 4)));
                temperature.set(Double.parseDouble(line.substring(8, line.length())));
            } catch (Exception e) {
                System.out.println("error>>>" + e.getMessage() + "内容>>>" + line);
            }
            context.write(year, temperature);
        }
    }

    //WeatherReducer
    static class WeatherReducer extends Reducer<IntWritable, DoubleWritable, IntWritable, Text> {
        IntWritable year = new IntWritable();
        Text temperature = new Text();

        @Override
        protected void reduce(IntWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            double sumTemperature = 0;
            for (DoubleWritable temperature : values) {
                sumTemperature += temperature.get();
                count++;
            }
            double avgTemperature = sumTemperature / count;
            year.set(key.get());
            temperature.set(String.valueOf(NumUtils.numFormat(avgTemperature)));
            context.write(year, temperature);
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
            Job job = Job.getInstance(conf, "序列化平均温度-微信公众号：小康新鲜事儿");
            job.setJarByClass(WeatherDemo.class);
            //设置Mapper
            job.setMapperClass(WeatherMapper.class);
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(DoubleWritable.class);
            //设置Reducer
            job.setReducerClass(WeatherReducer.class);
            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(Text.class);
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
