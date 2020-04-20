package cool.xiaokang.pojo;

import com.alibaba.fastjson.JSONObject;
import cool.xiaokang.annotation.ColumnFamily;
import cool.xiaokang.annotation.PerColumn;
import cool.xiaokang.annotation.RowKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author xiaokang
 */
@ColumnFamily(familyName = "basic")
public class User {
    @RowKey
    private String userId;
    @PerColumn(name = "basic", qualifier = "user_name")
    private String userName;
    private String userPwd;
    private Date birthday;
    private int age;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getBirthday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(birthday);
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
