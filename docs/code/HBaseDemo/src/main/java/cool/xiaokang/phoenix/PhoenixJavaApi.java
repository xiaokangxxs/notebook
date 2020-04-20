package cool.xiaokang.phoenix;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Phoenix的JavaAPI
 *
 * @author xiaokang
 */
public class PhoenixJavaApi {


//    CREATE TABLE IF NOT EXISTS t_user (
//          username VARCHAR NOT NULL,
//          sex CHAR(1) NOT NULL,
//          CONSTRAINT my_pk PRIMARY KEY (userid));
    public static void main(String[] args) throws Exception {
        // 加载数据库驱动
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");

        /*
         * 指定数据库地址,格式为 jdbc:phoenix:Zookeeper 地址
         */
        Connection connection = DriverManager.getConnection("jdbc:phoenix:hadoop:2181");

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM us_population");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("city") + " "
                    + resultSet.getInt("population"));
        }

        statement.close();
        connection.close();
    }
}
