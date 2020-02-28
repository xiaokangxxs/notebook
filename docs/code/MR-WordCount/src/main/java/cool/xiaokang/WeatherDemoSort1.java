package cool.xiaokang;

import cool.xiaokang.utils.NumUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.util.TreeSet;

/**
 * 分析每年平均温度-自定义分区
 *
 * @author xiaokang
 */
public class WeatherDemoSort1 {
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
            if (line.length() < 10) {
                return;
            }
            try {
                year.set(Integer.parseInt(line.substring(0, 4)));
                temperature.set(Double.parseDouble(line.substring(8, line.length())));
            } catch (Exception e) {
                System.out.println("error>>>" + e.getMessage() + "内容>>>" + line);
            }
            context.write(year, temperature);
        }
    }

    //自定义Partitioner
    static class WeatherPartitioner extends Partitioner<IntWritable, DoubleWritable> {
        @Override
        public int getPartition(IntWritable intWritable, DoubleWritable doubleWritable, int i) {
            int perYear = 0;
            if (intWritable.get() == 2016) {
                perYear = 0;
            } else if (intWritable.get() == 2017) {
                perYear = 1;
            } else if (intWritable.get() == 2018) {
                perYear = 2;
            }
            return perYear;
        }
    }

    //WeatherReducer
    static class WeatherReducer extends Reducer<IntWritable, DoubleWritable, IntWritable, Text> {
        IntWritable year;
        Text temperature;
        TreeSet<Double> result;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            year = new IntWritable();
            temperature = new Text();
            result = new TreeSet<>();
            context.write(null, new Text("年份|最高温度前2"));
        }

        @Override
        protected void reduce(IntWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            for (DoubleWritable temperature : values) {
                if (result.size() >= 2) {
                    if (temperature.get() > result.first()) {
                        result.remove(result.first());
                        result.add(temperature.get());
                    }
                } else {
                    result.add(temperature.get());
                }
            }
            year.set(key.get());
            temperature.set(String.valueOf(NumUtils.numFormat(result.first())));
            context.write(year, temperature);
            temperature.set(String.valueOf(NumUtils.numFormat(result.last())));
            context.write(year, temperature);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(null, new Text("执行完毕-微信公众号：小康新鲜事儿"));
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
            Job job = Job.getInstance(conf, "温度TOP2-Partitioner-微信公众号：小康新鲜事儿");
            job.setJarByClass(WeatherDemoSort1.class);
            //设置Mapper
            job.setMapperClass(WeatherMapper.class);
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(DoubleWritable.class);
            //自定义分区
            job.setPartitionerClass(WeatherPartitioner.class);
            job.setNumReduceTasks(3);

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
