package cool.xiaokang;

import cool.xiaokang.pojo.User;
import cool.xiaokang.service.Task;
import cool.xiaokang.service.impl.TaskImpl;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * 测试
 *
 * @author xiaokang
 */
public class TaskTest {
    Task task = new TaskImpl();


    @Test
    public void testPutData() throws Exception {
        task.putData("family:user", "xk1997", "basic", "user_sex", "male".getBytes());
    }

    @Test
    public void testPutObj() throws Exception {
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setUserId("user000" + i);
//            user.setUserName("xiaokang" + i);
//            user.setUserPwd("xiaokangxxs" + i);
//            user.setBirthday(new Date());
//            user.setAge(23 + i);
//            task.putObj("family:user", user);
//        }
        User user = new User();
        user.setUserId("20200330");
        user.setUserName("xiaokang7777");
        user.setUserPwd("xiaokangxxs777");
        user.setBirthday(new Date());
        user.setAge(17);
        task.putObj("family:user", user);
    }

    @Test
    public void testGetData() throws Exception {
        Result user001 = task.getDataByRowkey("family:user", "user001");
        List<Cell> cells = user001.listCells();
        for (Cell cell : cells) {
            System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
        }
    }

    @Test
    public void testGetObjData() throws Exception {
        User user001 = task.getDataByRowkey("family:user", "user001", User.class);
        System.out.println(user001);
    }

}
