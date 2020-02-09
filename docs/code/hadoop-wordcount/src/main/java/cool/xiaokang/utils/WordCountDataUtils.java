package cool.xiaokang.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 模拟数据工具类
 *
 * @author:xiaokang
 */
public class WordCountDataUtils {
    public static final List<String> WORD_LIST = Arrays.asList("xiaokang", "Hadoop", "微信公众号：小康新鲜事儿", "MapReduce", "Hive", "HBase", "Spark", "Flink");

    /**
     * 模拟产生词频数据
     *
     * @return String 词频数据
     */
    private static String generateData() {
        StringBuilder wordData = new StringBuilder();
        for (int i = 0; i < 1124; i++) {
            Collections.shuffle(WORD_LIST);
            Random random = new Random();
            int endIndex = random.nextInt(WORD_LIST.size()) % (WORD_LIST.size()) + 1;
            String perLine = StringUtils.join(WORD_LIST, "\t", 0, endIndex);
            wordData.append(perLine).append("\n");
        }
        return wordData.toString();
    }

    /**
     * 模拟产生词频数据并输出到本地
     *
     * @param outputPath 输出文件路径
     * @throws IOException
     */
    private static void generateDataToLocal(String outputPath) throws IOException {
        java.nio.file.Path path = Paths.get(outputPath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.write(path, generateData().getBytes(), StandardOpenOption.CREATE);
    }

    /**
     * 模拟产生词频数据并输出到HDFS
     *
     * @param hdfsUrl          HDFS地址
     * @param user             Hadoop用户名
     * @param outputPathString 存储到HDFS上的路径
     */
    private static void generateDataToHDFS(String hdfsUrl, String user, String outputPathString) {
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(new URI(hdfsUrl), new Configuration(), user);
            Path outPath = new Path(outputPathString);
            if (fileSystem.exists(outPath)) {
                fileSystem.delete(outPath, true);
            }
            /*FSDataOutputStream out = fileSystem.create(path);
            out.write(generateData().getBytes());
            out.flush();
            out.close();*/
            fileSystem.create(outPath).write(generateData().getBytes());
            fileSystem.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //generateDataToLocal("wordcount_input.txt");
            generateDataToHDFS("hdfs://192.168.239.161:9000", "root", "/wordcount/wordcount_input.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
