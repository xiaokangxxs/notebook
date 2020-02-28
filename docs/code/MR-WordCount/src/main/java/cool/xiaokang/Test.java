package cool.xiaokang;

import org.apache.hadoop.io.Text;

import java.util.TreeSet;

public class Test {
    private static final Text text = new Text("2016052810.36");
    private static final String a = "2016052810.36";

    public static void main(String[] args) {
//        TreeSet<Double> treeSet=new TreeSet<Double>();
//        treeSet.add(20d);
//        treeSet.add(40d);
//        treeSet.add(10d);
//        treeSet.add(5d);
//
//        System.out.println(treeSet.first());
        String abc = "8.35.201.160 - - [30/May/2018:17:38:21 +0800] \"www.baidu.com\" 200 ";
        String[] split = abc.split("\"");
        System.out.println(split[0]);
        System.out.println(split[1]);
        String[] customers = split[0].split(" ");
        System.out.println(customers[0]);
    }
}
