package cool.xiaokang;

import org.apache.commons.lang.ObjectUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
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
 * 数据排序
 *
 * @author xiaokang
 */
public class SortNum {
    //日志
    private static Logger logger = Logger.getLogger(WordCount_xiaokang.class);
    //硬编码方式固定URL和USER
    private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    //自定义Mapper
    static class SortMapper extends Mapper<LongWritable, Text, IntWritable, NullWritable> {
        IntWritable intWritable = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            logger.info("map接收》》》每行数据偏移量（行号）：" + key + "--" + value.toString());
            intWritable.set(Integer.parseInt(value.toString()));
            logger.info("map一行：" + Integer.parseInt(value.toString()) + "-xiaokang");
            //将结果输出到环形缓冲区context（默认大小100m）
            context.write(intWritable, NullWritable.get());
        }
    }

    //自定义Reducer
    static class SortReducer extends Reducer<IntWritable, NullWritable, IntWritable, NullWritable> {
        IntWritable intWritable = new IntWritable();
        @Override
        protected void reduce(IntWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            intWritable.set(key.get());
            context.write(key, NullWritable.get());
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
            Job job = Job.getInstance(conf, "数据排序-微信公众号：小康新鲜事儿");
            job.setJarByClass(SortNum.class);
            //设置Mapper
            job.setMapperClass(SortMapper.class);
            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(NullWritable.class);
            //设置Reducer
            job.setReducerClass(SortReducer.class);
            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(NullWritable.class);
            //如果输出目录已存在，则先删除，否则程序运行时会抛出异常
            FileSystem fs = FileSystem.get(new URI(HDFS_URL), conf, HDFS_USER);
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
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
