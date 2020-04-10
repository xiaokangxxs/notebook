package cool.xiaokang

/**
 * Array基本操作和重要方法
 *
 * @author: 小康
 * @date: 2020/4/9 20:00
 *        微信公众号：小康新鲜事儿
 *        小康个人文档：https://www.xiaokang.cool/
 */
object ArrayDemo {
  /**
   * immutable包：不可变-定长
   * mutable包：可变-变长
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    //声明定长数组，并赋初始值
    val a1 = Array(11, 24, 1, 2, 4)
    //声明定长数组，并指定初始长度
    val a2 = new Array[Int](5)
    //声明变长数组,并赋初始值
    val a3 = scala.collection.mutable.ArrayBuffer(11, 24, 1)

    //通过下标取值
    println(a1.apply(0))
    println(a1(0))
    //通过下标赋值
    a1(0)=10
    println(a1(0))
    a1.foreach(x=>{print(x+"|")})

    println()
    //变长数组追加操作
    a3.append(35)
    a3.foreach(x=>{print(x+"|")})

    println()
    val a4=Array(2,4,5,6)
    //取出前n个元素，并返回到新集合
    val r1=a4.take(2)
    //取出后n个元素，并返回到新集合
    val r2=a4.takeRight(1)
    //删除前n个元素，并将剩余元素返回到新集合
    val r3=a4.drop(2)
    //删除后n个元素，并将剩余元素返回到新集合
    val r4=a4.dropRight(2)
    //取出集合的头元素，和take(1)不同
    val r5=a4.head
    //去除集合的尾元素，和takeRight(1)不同
    val r6=a4.last
    println(r6)
    println(r5)
    a4.foreach(x=>{print(x+"|")})
    println()
    r1.foreach(x=>{print(x+"|")})
    println()
    r2.foreach(x=>{print(x+"|")})
    println()
    r3.foreach(x=>{print(x+"|")})
    println()
    r4.foreach(x=>{print(x+"|")})
    println("=====================")
    val a5=Array(11,24,66,1,2,4)
    //返回集合最大值
    val r7=a5.max
    //返回集合最小值
    val r8=a5.min
    //求和
    val r9=a5.sum
    //求平均值
    val r10=a5.sum/a5.length
    println(r7+"--"+r8+"--"+r9+"--"+r10)
    println("========")
    val a6=Array(1,2,4)
    val a7=Array(2,4,6,8)
    //取交集
    val r11=a6.intersect(a7)
    //取并集
    val r12=a6.union(a7)
    //去重
    val r13=r12.distinct
    //取差集
    val r14=a6.diff(a7)
    val r15=a7.diff(a6)
    r11.foreach(x=>{print(x+"|")})
    println()
    r12.foreach(x=>{print(x+"|")})
    println()
    r13.foreach(x=>{print(x+"|")})
    println()
    r14.foreach(x=>{print(x+"|")})
    println()
    r15.foreach(x=>{print(x+"|")})
    println("========")
    val a8=Array(1,2,3,4,5,6)
    //按照指定匿名函数规则过滤元素，并将结果返回到新集合中
//    val r16=a8.filter((x:Int)=>{x>3})
    val r16=a8.filter(x=>x>3)
//    val r16=a8.filter((_>3)
    r16.foreach(x=>{print(x+"|")})
    //操作a8，过滤出偶数且大于4的元素
    val r17=a8.filter(x => x%2==0&&x>4)
    println()
    r17.foreach(x=>{print(x+"|")})
    val a9=Array("xiaokang M 18","Tom M 15","Lucy F 22","Math F 19")
    //操作a9，过滤出男性数据
    val r18=a9.filter(x=>x.contains("M"))//这种可能有误差
    val r19=a9.filter(line=>line.split(" ")(1).equals("M"))//这种正确
    println()
    r18.foreach(x=>{print(x+"|")})
    println()
    r19.foreach(x=>{print(x+"|")})
    //操作a9，过滤出成年人数据
    val r20=a9.filter(line=>line.split(" ")(2).toInt>=18)
    println()
    r20.foreach(x=>{print(x+"|")})

  }
}
