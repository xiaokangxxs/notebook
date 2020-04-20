package cool.xiaokang.service.impl;

import cool.xiaokang.pojo.User;
import cool.xiaokang.service.Task;
import cool.xiaokang.utils.HBaseORM;
import cool.xiaokang.utils.HBaseUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class TaskImpl implements Task {
    @Override
    public void putData(String tableName, String rowkey, String familyName, String qualifier, byte[] data) throws Exception {
        //1.获取表对象
        Table table = HBaseUtils.getAdmin().getConnection().getTable(tableName);
        //2.将数据封装成put对象
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), data);
        table.put(put);
        table.close();
    }

    @Override
    public Result getDataByRowkey(String tableName, String rowkey) throws Exception {
        Table table = HBaseUtils.getAdmin().getConnection().getTable(tableName);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        return result;
    }

    @Override
    public User getDataByRowkey(String tableName, String rowkey, Class<User> clazz) throws Exception {
        Table table = HBaseUtils.getAdmin().getConnection().getTable(tableName);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        User user = clazz.newInstance();
        user.setUserId(rowkey);
        HBaseORM.resultToBean(result, user);
        return user;
    }

    @Override
    public void putObj(String tableName, User obj) throws Exception {
        //1.获取表对象
        Table table = HBaseUtils.getAdmin().getConnection().getTable(tableName);
        Put put = HBaseORM.beanToPut(obj);
        table.put(put);
    }
}
