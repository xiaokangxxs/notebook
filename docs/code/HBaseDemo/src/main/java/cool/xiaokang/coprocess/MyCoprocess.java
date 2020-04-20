package cool.xiaokang.coprocess;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;

import java.io.IOException;

/**
 * 第一步：继承相关类 BaseRegionObserver
 * 第二步：实现协处理方法
 * 第三步：给表添加协处理
 * 第四步：将协处理器进行打包jar 分发到hbase服务器集群 lib目录下
 * 第五步：重启hbase集群
 * 第六步：创建协处理器需要的表
 */
public class MyCoprocess extends BaseRegionObserver {
    //在主表插入完成之后插入下协处理的副表
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        TableName tableName = TableName.valueOf("test:coprocess_table");
        HTableInterface table = e.getEnvironment().getTable(tableName);
        table.put(put);
        table.close();
    }
}

