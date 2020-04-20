package cool.xiaokang.coprocess;

import cool.xiaokang.utils.HBaseUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;

public class TestMyCoprocess {
    static String tableName = "test:test_coprocess";
    static String familyName = "info";

    //表添加协处理 表创建的时候
    public static void main(String[] args) throws Exception {
        addData();
    }

    public static void createTable() throws Exception {
        HBaseAdmin admin = HBaseUtils.getAdmin();
        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
        HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(familyName);
        tableDescriptor.addFamily(hColumnDescriptor);
        //添加协处理器
        tableDescriptor.addCoprocessor("cool.xiaokang.coprocess.MyCoprocess");
        admin.createTable(tableDescriptor);
        System.out.println("创建表成功");
    }

    public static void addData() throws Exception {
        HTableInterface table = HBaseUtils.getAdmin().getConnection().getTable(tableName);
        Put put = new Put(String.valueOf((int) (Math.random() * 10000000)).getBytes());
        put.addColumn(familyName.getBytes(), "aa".getBytes(), String.valueOf((int) (Math.random() * 10000000)).getBytes());
        table.put(put);
        table.close();
    }
}
