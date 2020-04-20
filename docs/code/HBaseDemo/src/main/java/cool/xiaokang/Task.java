package cool.xiaokang;

import cool.xiaokang.pojo.NamespaceInfo;
import cool.xiaokang.utils.HBaseAssert;
import cool.xiaokang.utils.HBaseUtils;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2020.3.30作业
 *
 * @author xiaokang
 */
public class Task {
    // 根据命名空间的名称 查询NamespaceInfo对象信息
    public NamespaceInfo getInfoFromNamespaceName(String namespaceName) throws Exception {
        NamespaceInfo namespaceInfo = new NamespaceInfo();
        HBaseAssert.isNull(namespaceName);
        //封装命名空间名称
        namespaceInfo.setName(namespaceName);
        //根据命名空间名称获取配置，封装到NamespaceInfo
        NamespaceDescriptor namespaceDescriptor = HBaseUtils.getAdmin().getNamespaceDescriptor(namespaceName);
        Map<String, String> configuration = namespaceDescriptor.getConfiguration();
        namespaceInfo.setConfig(configuration);
        //封装所有表名
        TableName[] tableNames = HBaseUtils.getAdmin().listTableNamesByNamespace(namespaceName);
        //封装命名空间下表的数量
        namespaceInfo.setTableNum(tableNames.length);
        List<String> tableNameList = new ArrayList<>(tableNames.length);
        int i = 0;
        int j = 0;
        for (TableName tableName : tableNames) {
            tableNameList.add(Bytes.toString(tableName.getName()));
            //封装不可用表数量
            boolean tableDisabled = HBaseUtils.getAdmin().isTableDisabled(tableName.getName());
            if (tableDisabled) {
                i++;
            }
            //封装可用表数量
            boolean tableEnabled = HBaseUtils.getAdmin().isTableEnabled(tableName.getName());
            if (tableEnabled) {
                j++;
            }
        }
        namespaceInfo.setDisableTableNum(i);
        namespaceInfo.setEnableTableNum(j);
        namespaceInfo.setTableNames(tableNameList);
        return namespaceInfo;
    }

    //获取所有的命名空间信息NamespaceInfo
    public List<NamespaceInfo> getAllInfo() throws Exception {
        NamespaceDescriptor[] namespaceDescriptors = HBaseUtils.getAdmin().listNamespaceDescriptors();
        List<NamespaceInfo> namespaceInfos = new ArrayList<>(namespaceDescriptors.length);
        for (NamespaceDescriptor descriptor : namespaceDescriptors) {
            NamespaceInfo infoFromNamespaceName = getInfoFromNamespaceName(descriptor.getName());
            namespaceInfos.add(infoFromNamespaceName);
        }
        return namespaceInfos;
    }
}
