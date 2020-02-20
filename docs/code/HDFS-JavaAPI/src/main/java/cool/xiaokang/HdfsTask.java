package cool.xiaokang;

import cool.xiaokang.utils.HDFSUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * HDFS常用javaapi
 * @author xiaokang
 */
public class HdfsTask {

    public static void main(String[] args) throws Exception{
        if (args==null || args.length<2){
            System.out.println("Input and Output are necessary!!!");
            return;
        }
//        testMkdirs("/test/2");
//        testFileStatus("/input");
//        testFileDetails("/input");
//        testRename("/input/xiaokang_mv.txt","/input/xiaokang111.txt");
//        testCopy("/input/xiaokang111.txt","/xiaokang/xiaokang111.txt");
//        testDelete("/input/xiaokang");
//        testPut("D:\\mysql8.sql","/input");
//        testGet(args[0],args[1]);
//        testGet("/input/mysql8.sql","E:\\");
        //IO流方式一定要注意写全文件名
//        testPutWithIO("E:\\PS_AI知识点.txt","/input/PS_AI");
        //IO流方式一定要注意写全文件名
        testGetWithIO(args[0],args[1]);
    }

    //创建目录
    static void testMkdirs(String path) throws Exception{
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                fs.mkdirs(new Path(path));
                System.out.println("创建成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("创建失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }

    //查看目录下文件信息
    static void testFileStatus(String path){
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                FileStatus[] fileStatuses = fs.listStatus(new Path(path));
                for(FileStatus file:fileStatuses){
                    System.out.println(file);
                    System.out.println(file.getOwner()+"--"+file.getPermission());
                }
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("error"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }

    //递归查看目录下所有文件的详细信息
    static void testFileDetails(String path){
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                RemoteIterator<LocatedFileStatus> fileList = fs.listFiles(new Path(path),true);
                while (fileList.hasNext()){
                    LocatedFileStatus file = fileList.next();
                    //每个文件的详细信息
                    System.out.println(file.getOwner()+"--"+file.getPermission());

                    //文件的块信息
                    BlockLocation[] blocks = file.getBlockLocations();
                    int i=0;
                    for(BlockLocation block:blocks){
                        String[] hosts = block.getHosts();
                        System.out.println("块数："+block.getLength());
                        System.out.println("第【"+i+"】块："+ Arrays.toString(hosts));
                        i++;
                    }
                    System.out.println("===================");
                }
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("创建失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }

    }

    //集群文件重命名
    static void testRename(String oldName,String newName){
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                fs.rename(new Path(oldName),new Path(newName));
                System.out.println("重命名成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("重命名失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }

    //集群文件复制
    static void testCopy(String src,String dest){
        FileSystem fileSystem=null;
        FSDataInputStream in=null;
        FSDataOutputStream out=null;
        try{
            fileSystem = HDFSUtils.getFileSystem();
            if (fileSystem!=null){
                in=fileSystem.open(new Path(src));
                out=fileSystem.create(new Path(dest));
                IOUtils.copyBytes(in,out,4096);
                System.out.println("复制完成");
            }else{
                System.out.println("获取FileSystem失败！");
            }
        }catch (Exception e){
            System.out.println("error"+e.getMessage());
        }finally {
            try {
                out.close();
                in.close();
                HDFSUtils.closeFileSystem(fileSystem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //集群文件或目录删除
    static void testDelete(String path){
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                fs.delete(new Path(path),true);
                System.out.println("删除成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("删除失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }

    //本地文件上传至HDFS
    static void testPut(String localSrc,String hdfsDest){
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                fs.copyFromLocalFile(new Path(localSrc),new Path(hdfsDest));
                System.out.println("上传成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("上传失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }

    //本地文件上传至HDFS-IO流方式
    static void testPutWithIO(String localSrc,String hdfsDest){
        FileSystem fs = null;
        FileInputStream in=null;
        FSDataOutputStream out=null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                in=new FileInputStream(new File(localSrc));
                out=fs.create(new Path(hdfsDest));
                IOUtils.copyBytes(in,out,4096);
                System.out.println("IO流-上传成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("IO流-上传失败"+e.getMessage());
        }finally {
            try {
                out.close();
                in.close();
                HDFSUtils.closeFileSystem(fs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //从HDFS下载文件到本地
    static void testGet(String hdfsSrc,String localDest){
        FileSystem fs = null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                fs.copyToLocalFile(new Path(hdfsSrc),new Path(localDest));
                System.out.println("下载成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("下载失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }

    //从HDFS下载文件到本地-IO流方式
    static void testGetWithIO(String hdfsSrc,String localDest){
        FileSystem fs = null;
        FSDataInputStream in=null;
        FileOutputStream out=null;
        try {
            fs=HDFSUtils.getFileSystem();
            if (fs!=null){
                in=fs.open(new Path(hdfsSrc));
                out=new FileOutputStream(new File(localDest));
                IOUtils.copyBytes(in,out,4096);
                System.out.println("IO流-下载成功！！！");
            }else{
                System.out.println("获取FileSystem失败！！");
            }
        }catch (Exception e){
            System.out.println("IO流-下载失败"+e.getMessage());
        }finally {
            HDFSUtils.closeFileSystem(fs);
        }
    }
}
