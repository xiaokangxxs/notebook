package cool.xiaokang.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * HBase工具类,获取HBase操作对象
 *
 * @author xiaokang
 */
public class HBaseUtils {
    private static HBaseAdmin admin = null;

    static {
        //获取HBase配置
        Configuration conf = HBaseConfiguration.create();
        //通过Zookeeper获取HBase的连接地址，将hadoop主机名在windows中进行映射配置
        conf.set("hbase.zookeeper.quorum", "192.168.239.160:2181");
        try {
            //获取连接对象
            Connection conn = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin) conn.getAdmin();
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }

    public static HBaseAdmin getAdmin() {
        return admin;
    }
}
