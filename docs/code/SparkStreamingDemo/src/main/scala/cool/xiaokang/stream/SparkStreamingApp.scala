package cool.xiaokang.stream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * SparkStreaming-WordCount(微批处理)
 *
 * @author: xiaokang
 * @date: 2020/7/12 16:32
 *        微信公众号：小康新鲜事儿
 *        小康个人文档：https://www.xiaokang.cool/
 */
object SparkStreamingApp {
  def main(args: Array[String]): Unit = {
    // 创建StreamingContext入口对象
    val conf=new SparkConf()
      .setAppName("SparkStreamingWordCountDemo")
      .setMaster("local[*]")
    val stc=new StreamingContext(conf,Seconds(5))
    // 从流中读取数据并进行词频统计
    val lines = stc.socketTextStream("hadoop", 1124)
    val result = lines.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()
    // 启动服务
    stc.start()
    // 等待服务结束
    stc.awaitTermination()
  }

}
