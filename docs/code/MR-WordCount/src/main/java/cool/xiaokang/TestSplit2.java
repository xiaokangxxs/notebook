package cool.xiaokang;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

/**
 * 测试合并文件
 *
 * @author xiaokang
 */
public class TestSplit2 {
    //硬编码方式固定URL和USER
    private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    static class CustomMapper extends Mapper<Object, Text, Text, NullWritable> {
        Text text = new Text();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            text.set(value.toString());
            context.write(text, NullWritable.get());
        }
    }


    //第3部分，编写Driver(main方法)
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("至少2个参数");
                System.exit(0);
            }
            //1)创建Configuration对象，指明namespace的路径
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", HDFS_URL);
            //
            //2)创建Job
            Job job = Job.getInstance(conf, "多个小文件物理合并");
            job.setJarByClass(TestSplit2.class);

            //3)自定义Mapper进行输出参数(key,value)的配置
            job.setMapperClass(CustomMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);

            job.setNumReduceTasks(0);

//            //4)小文件优化
            job.setInputFormatClass(CombineTextInputFormat.class);
//            CombineTextInputFormat.setMaxInputSplitSize(job, 128 * 1024 * 1024); //最大
            CombineTextInputFormat.setMaxInputSplitSize(job, 90); //最大

            //如果输出目录已存在，则先删除，否则程序运行时会抛出异常
            FileSystem fs = FileSystem.get(new URI(HDFS_URL), conf, HDFS_USER);
            Path outputPath = new Path(args[1]);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
            }

            //5)配置处理的文件的路径(input)以及处理结果存放的路径(output)
            FileInputFormat.setInputPaths(job, new Path(args[0])); //data/sortnumber.txt
            FileOutputFormat.setOutputPath(job, outputPath);

            //6)让程序执行
            boolean result = job.waitForCompletion(true);
            if (result) {
                System.out.println("执行正确！！！");
            } else {
                System.out.println("执行失败.....");
            }
        } catch (Exception ex) {
            System.out.println("执行出错:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
