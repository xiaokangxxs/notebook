package cool.xiaokang.lritem

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 回归模型预测
 *  Y=β1X1+β2X2+β0
 *  Y=-6.497X1+0.016X2+106.368
 * @author: xiaokang
 * @date: 2020/6/28 14:58
 *        微信公众号：小康新鲜事儿
 *        小康个人文档：https://www.xiaokang.cool/
 */
object PredictItemDemo {
  def main(args: Array[String]): Unit = {
    // Spark配置对象
    val conf=new SparkConf()
      .setMaster("local[*]")
      .setAppName("PredictItemDemo")
    // SparkContext入口对象
    val sc=new SparkContext(conf)
    // SparkSession对象
    val sparkSession=SparkSession.builder()
      .master("local[*]")
      .appName("PredictItemDemo")
      .getOrCreate()
    val data:RDD[String] = sc.textFile("D:/IDEA-Projects/SparkML/lritem.txt")
    // 第一步：将RDD[String]->RDD[(X1,X2,Y)]
    val itemR1:RDD[(Double,Double,Double)] = data.map(line => {
      val info = line.split("\\|")
      val Y = info(0).toDouble
      val X1 = info(1).split(" ")(0).toDouble
      val X2 = info(1).split(" ")(1).toDouble
      (X1, X2, Y)
    })
    // 第二步：将RDD[(X1,X2,Y)]->DataFrame(X1,X2,Y)
    val itemDf:DataFrame = sparkSession.createDataFrame(itemR1).toDF("X1", "X2", "Y")
    // 第三步：将DataFrame(X1,X2,Y)->DataFrame(Vectors(X1,X2),Y)才能建模
    // 转换工具类
    val vectorAssembler=new VectorAssembler()
      .setInputCols(Array("X1","X2")) //指定自变量列名
      .setOutputCol("features") //所有自变量起别名（自行DIY），可以通过此列名找到所有自变量
    val itemDfVectors:DataFrame = vectorAssembler.transform(itemDf)
    // 第四步：建立回归模型，底层使用最小二乘法进行建模
    val model=new LinearRegression()
      .setFeaturesCol("features") // 告诉模型哪些是自变量
      .setLabelCol("Y") // 告诉模型因变量
      .setFitIntercept(true) // 计算截距系数
      .fit(itemDfVectors) // 带入数据进行建模
    // 自变量系数
    //println(model.coefficients)
    // 截距系数
    //println(model.intercept)
    // 第五步：通过模型进行预测
    val itemPredict = model.transform(itemDfVectors)
    //itemPredict.show()

    //  +---+------+-----+------------+------------------+
    //  | X1|    X2|    Y|    features|        prediction|
    //  +---+------+-----+------------+------------------+
    //  |5.0|1000.0|100.0|[5.0,1000.0]|  90.2012541654423|
    //  |7.0| 600.0| 75.0| [7.0,600.0]| 70.67991272108885|
    //  |6.0|1200.0| 80.0|[6.0,1200.0]|  86.9677455657187|
    //  |6.0| 500.0| 70.0| [6.0,500.0]| 75.54521185142573|
    //  |8.0|  30.0| 50.0|  [8.0,30.0]|54.881617035642996|
    //  |7.0| 400.0| 65.0| [7.0,400.0]|  67.4163316598623|
    //  |5.0|1300.0| 90.0|[5.0,1300.0]| 95.09662575728214|
    //  |4.0|1100.0|100.0|[4.0,1100.0]| 98.33013435700573|
    //  |3.0|1300.0|110.0|[3.0,1300.0]|108.09080507918245|
    //  |9.0| 300.0| 60.0| [9.0,300.0]|  52.7903618073487|
    //  +---+------+-----+------------+------------------+

    // X1=11,X2=450时，预测Y
    val testR1:RDD[(Int,Int,Int)] = sc.makeRDD(List((11, 450, 0)))
    val testDf = sparkSession.createDataFrame(testR1).toDF("X1", "X2", "Y")
    val testDfVectors = vectorAssembler.transform(testDf)
    val testPredict = model.transform(testDfVectors)
    testPredict.show()

    //  +---+---+---+------------+-----------------+
    //  | X1| X2|  Y|    features|       prediction|
    //  +---+---+---+------------+-----------------+
    //  | 11|450|  0|[11.0,450.0]|42.24386828136832|
    //  +---+---+---+------------+-----------------+
  }
}
