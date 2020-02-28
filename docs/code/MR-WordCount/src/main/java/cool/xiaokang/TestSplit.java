package cool.xiaokang;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 修改切片大小
 *
 * @author xiaokang
 */
public class TestSplit {
    static class CustomMapper extends Mapper<Object, Text, Text, NullWritable> {
        Text text = new Text();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            text.set(value.toString());
            context.write(text, NullWritable.get());
        }
    }

    static class CustomReducer extends Reducer<Text, NullWritable, Text, Text> {
        Text text = new Text();

        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            text.set(key.toString());
            context.write(text, text);
        }
    }


    //第3部分，编写Driver(main方法)
    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("至少4个参数");
                System.exit(0);
            }
            //1)创建Configuration对象，指明namespace的路径
            Configuration conf = new Configuration();
            // 计算方法：Math.max(minSize, Math.min(maxSize, blockSize));
            conf.set("fs.defaultFS", "hdfs://192.168.239.161:9000");
            conf.set("mapreduce.input.fileinputformat.split.minsize", args[2]);
            conf.set("mapreduce.input.fileinputformat.split.maxsize", args[3]);
            //
            //2)创建Job
            Job job = Job.getInstance(conf, "修改切片大小");
            job.setJarByClass(TestSplit.class);

            //3)自定义Mapper进行输出参数(key,value)的配置
            job.setMapperClass(CustomMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);

            //4)自定义Reducer进行参数的配置
            job.setReducerClass(CustomReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            //5)配置处理的文件的路径(input)以及处理结果存放的路径(output)
            FileInputFormat.setInputPaths(job, new Path(args[0])); //data/sortnumber.txt
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

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
