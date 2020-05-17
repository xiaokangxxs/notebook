package cool.xiaokang;

import cool.xiaokang.exception.HBaseException;
import cool.xiaokang.pojo.NamespaceInfo;
import cool.xiaokang.service.NamespaceOperation;
import cool.xiaokang.service.impl.NamespaceOperationImpl;
import cool.xiaokang.utils.HBaseUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Test;

import java.util.List;

/**
 * HBase-API操作
 *
 * @author xiaokang
 */
public class NamespaceTest {
    NamespaceOperation namespaceOperation = new NamespaceOperationImpl();
    Task task = new Task();

    @Test
    public void listNamespace() {
        try {
            List<String> list = namespaceOperation.listNamespace();
            for (String s : list) {
                System.out.println(s);
            }
        } catch (HBaseException e) {
            e.printException();
        }
    }

    @Test
    public void testJsonTask() {
        try {
            NamespaceInfo hbase02 = task.getInfoFromNamespaceName("hbase02");
            System.out.println(hbase02);
        } catch (Exception e) {

        }
    }

    @Test
    public void testAllJsonTask() {
        try {
            List<NamespaceInfo> allInfo = task.getAllInfo();
            for (NamespaceInfo namespaceInfo : allInfo) {
                System.out.println(namespaceInfo);
            }
        } catch (Exception e) {

        }
    }
}
