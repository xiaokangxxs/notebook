package cool.xiaokang;

import cool.xiaokang.pojo.Transaction;
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
 * 序列化案例
 *
 * @author xiaokang
 */
public class WritableDemo {
    //日志
    private static Logger logger = Logger.getLogger(WritableDemo.class);
    //硬编码方式固定URL和USER
    private static final String HDFS_URL = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    static double numFormat(double score) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return Double.parseDouble(decimalFormat.format(score));
    }

    //自定义Mapper
    static class TxMapper extends Mapper<LongWritable, Text, Text, Transaction> {
        Transaction transaction = new Transaction();
        Text text = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //拿到每行的数据
            String line = value.toString();
            if (line.startsWith("编号")) {
                return;
            }
            //按照制表符分割
            String[] transactions = line.split(",");
            //获得手机号
            String phoneNum = transactions[1];
            text.set(phoneNum);
            //获得交易金额
            double tradeMoney = Double.parseDouble(transactions[3]);
            //获得退款金额
            double refundMoney = Double.parseDouble(transactions[4]);
            //获得评分
            int mark = Integer.parseInt(transactions[5]);
            transaction.setThree(tradeMoney, refundMoney, mark);
            //将结果输出
            context.write(text, transaction);
        }
    }

    //自定义比较器
//    static class TxComparator extends WritableComparator {
//        protected TxComparator() {
//            super(Transaction.class, true);
//        }
//
//        @Override
//        public int compare(WritableComparable a, WritableComparable b) {
//            Transaction txA = (Transaction) a;
//            Transaction txB = (Transaction) b;
//            return Double.compare(txB.getSumTradeMoney(), txA.getSumTradeMoney());
//        }
//    }

    //自定义Reducer
    static class TxReducer extends Reducer<Text, Transaction, Text, Text> {
        Transaction resultTransaction = new Transaction();
        Text text = new Text();
        Text text1 = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Text text = new Text();
            text.set("手机号,交易金额,退款金额,平均积分");
            context.write(null, text);
        }

        @Override
        protected void reduce(Text key, Iterable<Transaction> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            double sumTradeMoney = 0;
            double sumRefundMoney = 0;
            double sumMark = 0;
            for (Transaction transaction : values) {
                sumTradeMoney += transaction.getTradeMoney();
                sumRefundMoney += transaction.getRefundMoney();
                sumMark += transaction.getMark();
                count++;
            }
            double avgMark = sumMark / count;
            resultTransaction.setThreeOutput(numFormat(sumTradeMoney), numFormat(sumRefundMoney), numFormat(avgMark));
            text.set(key.toString());
            text1.set(resultTransaction.toString());
            context.write(text, text1);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            Text text = new Text();
            text.set("执行完毕--微信公众号：小康新鲜事儿");
            context.write(null, text);
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
            Job job = Job.getInstance(conf, "序列化消费额-微信公众号：小康新鲜事儿");
            job.setJarByClass(WritableDemo.class);
            //设置Mapper
            job.setMapperClass(TxMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Transaction.class);
            //设置Reducer
            job.setReducerClass(TxReducer.class);
            job.setOutputKeyClass(Text.class);
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
            //设置Reduce端的比较器
            //job.setGroupingComparatorClass(TxComparator.class);
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
