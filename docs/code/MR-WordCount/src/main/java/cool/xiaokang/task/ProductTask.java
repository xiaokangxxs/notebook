package cool.xiaokang.task;

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

import java.io.IOException;
import java.net.URI;

/**
 * 商品-销量-时间
 *
 * @author xiaokang
 */
public class ProductTask {
    private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    //自定义Mapper
    static class ProductMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text productId;
        Text sale_time;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            productId = new Text();
            sale_time = new Text();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] infos = value.toString().split(",");
            productId.set(infos[0]);
            sale_time.set(infos[1] + "-" + infos[2]);
            context.write(productId, sale_time);
        }
    }

    //自定义Reducer
    static class ProductReducer extends Reducer<Text, Text, NullWritable, Text> {
        Text result;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            result = new Text();
        }

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String productId = key.toString();
            for (Text value : values) {
                result.set(productId + "-" + value);
                context.write(null, result);
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(null, new Text("执行完毕！！！微信公众号：小康新鲜事儿"));
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
            Job job = Job.getInstance(conf, "ProductTask");
            job.setJarByClass(ProductTask.class);
            //设置Mapper
            job.setMapperClass(ProductMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            //设置Reducer
            job.setReducerClass(ProductReducer.class);
            job.setOutputKeyClass(NullWritable.class);
            job.setOutputValueClass(Text.class);
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

}
