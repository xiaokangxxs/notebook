package cool.xiaokang.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
 * 统计每年、每月各个网站的总的上行和下行流量
 * @author: xiaokang
 * @date: 2020/7/10 16:47
 * 微信公众号：小康新鲜事儿
 * 小康个人文档：https://www.xiaokang.cool/
 */

public class UserFlow {
//    private static final String HDFS_URL = "hdfs://192.168.244.160:9000";
//    private static final String HDFS_USER = "xiaokang";
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
        if (args == null || args.length < 4) {
            System.out.println("Fsurl、Fsuser、Input、Output paths are necessary!");
            return;
        }
        //编写Driver类
        try {
            String HDFS_URL=args[0];
            String HDFS_USER=args[1];
            String inputPath=args[2];
            String outPath=args[3];
            //配置类，指定HDFS的路径
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", HDFS_URL);
            //指定结果文件的文件名前缀，默认part
            conf.set("mapreduce.output.basename", "flow-1");
            //指定结果文件中键和值之间的分隔符，默认为\t
            conf.set("mapreduce.output.textoutputformat.separator", "--");
            //创建一个Job作业
            Job job = Job.getInstance(conf, "流量处理-微信公众号：小康新鲜事儿");
            job.setJarByClass(UserFlow.class);
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
            Path outputPath = new Path(outPath);
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
                System.out.println("已删除原有路径~~~");
            }
            //指定作业的输入文件和输出结果文件的路径
            FileInputFormat.setInputPaths(job, new Path(inputPath));
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

