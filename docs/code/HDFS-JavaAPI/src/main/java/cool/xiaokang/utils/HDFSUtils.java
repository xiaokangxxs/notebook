package cool.xiaokang.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;


/**
 * HDFS工具类
 *
 * @author xiaokang
 */
public class HDFSUtils {
    private static final String HDFS_PATH = "hdfs://192.168.239.161:9000";
    private static final String HDFS_USER = "xiaokang";

    /**
     * 获取FileSystem对象
     *
     * @return
     */
    public static FileSystem getFileSystem() {
        try {
            Configuration conf = new Configuration();
            FileSystem fileSystem = FileSystem.get(new URI(HDFS_PATH), conf, HDFS_USER);
            return fileSystem;
        } catch (Exception e) {
            System.out.println("获取FileSystem失败！！！---" + e.getMessage());
            return null;
        }
    }

    /**
     * 关闭FileSystem对象
     * @param fileSystem
     */
    public static void closeFileSystem(FileSystem fileSystem) {
        if (fileSystem != null) {
            try {
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
