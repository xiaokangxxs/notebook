package cool.xiaokang.service;

import cool.xiaokang.pojo.User;
import org.apache.hadoop.hbase.client.Result;

public interface Task {
    public void putObj(String tableName, User obj) throws Exception;

    public void putData(String tableName, String rowkey, String familyName, String qualifier, byte[] data) throws Exception;

    public Result getDataByRowkey(String tableName, String rowkey) throws Exception;

    public User getDataByRowkey(String tableName, String rowkey, Class<User> clazz) throws Exception;

}
