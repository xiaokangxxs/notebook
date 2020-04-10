package cool.xiaokang

/**
 * @author: 小康
 * @date: 2020/4/10 20:25
 *        微信公众号：小康新鲜事儿
 *        小康个人文档：https://www.xiaokang.cool/
 */
object ArrayDemo1 {
  def main(args: Array[String]): Unit = {
    val a1 = Array("xiaokang M 18", "Tom M 15", "Lucy F 22", "Math F 19")
    //根据指定的匿名函数，判断元素是否存在，存在为true，不存在为false
    val r1 = a1.exists(line => {
      line.split(" ")(2).toInt > 19
    })
    println("a1是否存在年龄大于19岁的元素？" + r1)
    val a2 = Array(11, 24, 1, 2, 4)
    //重要方法，将集合中每个元素映射到另一个形式，只改变形式，不改变个数
    val r2 = a2.map(num => num << 1)
    println("a2映射后的结果为：" )
    r2.foreach(num=>{print(num+"|")})
    println
    //操作a1，使用map映射返回Array("xiaokang","Tom","Lucy","Math")
    val r3=a1.map(line => {line.split(" ")(0)})
    println("a1映射后的结果为：" )
    r3.foreach(num=>{print(num+"|")})
    println
    //操作a1，求出男性的年龄之和
    val r4=a1.filter(line => {line.split(" ")(1).equals("M")})
      .map(line => {line.split(" ")(2).toInt})
      .sum
    println("a1中所有男性的年龄之和>>>"+r4)
    //操作a1，求出男性的年龄之和-另一种简洁书写格式
    val r5=a1.filter(_.split(" ")(1).equals("M"))
      .map(_.split(" ")(2).toInt)
      .sum
    println("a1中所有男性的年龄之和>>>"+r5)
    val a3=Array(1,2,3,4,5)
    //reduce规约方法，底层为多次迭代
    /**
     * x=1 y=2 x+y=3
     * x=3 y=3 x+y=6
     * x=6 y=4 x+y=10
     * x=10 y=5 x+y=15
     */
    val r6=a3.reduce((x,y) => {x+y})
    println(r6)
    //操作a1,求阶乘结果
    val r7=a3.reduce((x,y)=>{x*y})
    println(r7)
    val a4=Array(11,24,35,66,1,88)
    //操作a4，使用reduce方法返回a4中最大值（不能使用max）
    /**
     * x=11 y=24 result=24
     * x=24 y=35 result=35
     * x=35 y=66 result=66
     * x=66 y=1 result=66
     * x=66 y=88 result=88
     */
    val r8=a4.reduce((x,y)=>{if (x>y)x else y})
    println(r8)
    //sortBy按指定的匿名函数规则排序，并将结果返回到新集合中
    //升序
    val r9=a4.sortBy(num=>num)
    r9.foreach(num=>{print(num+"|")})
    println
    //降序
    val r10=a4.sortBy(num=>num).reverse
    r10.foreach(num=>{print(num+"|")})
    println
    //降序-另一种写法
    val r11=a4.sortBy(num => -num)
    r11.foreach(num=>{print(num+"|")})
    println
    //操作a1，按姓名升序排序并返回结果集
    val r12=a1.sortBy(line=>{line.split(" ")(0)})
    r12.foreach(num=>{print(num+"|")})
    println
    //操作a1,按照年龄降序排序并返回结果集
    val r13=a1.sortBy(line=>{-line.split(" ")(2).toInt})
    r13.foreach(num=>{print(num+"|")})
    println
    //操作a1,求出年龄最小的前三个人的年龄的平均值
    val r14=a1.map(line=>{line.split(" ")(2).toInt})
      .sortBy(age=>age)
      .take(3)
    val r15=r14.sum/r14.length
    println("a1中年龄最小的前三个人的年龄的平均值>>>"+r15)
    //操作a1,求出年龄最小的前三个人的年龄的平均值--降序写法
    val r16=a1.map(line=>{line.split(" ")(2).toInt})
      .sortBy(age=> -age)
      .takeRight(3)
    val r17=r16.sum/r16.length
    println("a1中年龄最小的前三个人的年龄的平均值>>>"+r17)
    val a5=Array(118,125,96,34)
    //将集合中的所有元素返回为一个字符串
    val r18=a5.mkString
    println("a5转为字符串>>>"+r18)
    //以指定的分隔符将集合中的所有元素返回为一个字符串
    val r19=a5.mkString("|")
    println("a5转为字符串(指定分隔符为|)>>>"+r19)
    //操作a1，将集合中每个元素中的姓名、性别和年龄都分别作为一个元素返回到新集合
    //扁平化映射flatMap，将集合中每个元素映射到另一个形式，既改变形式，也改变个数
    val r20=a1.flatMap(line=>{line.split(" ")})
    r20.foreach(num=>{print(num+"|")})
  }
}
