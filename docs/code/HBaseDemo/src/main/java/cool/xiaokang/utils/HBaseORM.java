package cool.xiaokang.utils;

import cool.xiaokang.annotation.ColumnFamily;
import cool.xiaokang.annotation.PerColumn;
import cool.xiaokang.annotation.RowKey;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 公共转换方法
 *
 * @author xiaokang
 */
public class HBaseORM {

    /**
     * 将Bean对象转为Put
     *
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Put beanToPut(T t) throws Exception {
        //获取RowKey
        byte[] rowkey = getRowKeyFromObj(t);
        //创建Put对象
        Put put = new Put(rowkey);
        //获取全局列族名称
        byte[] family = getGlobalFamily(t);
        //获取每一列的值
        getAllColumnFromObj(put, family, t);
        return put;
    }

    /**
     * 根据Bean获取rowkey
     *
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> byte[] getRowKeyFromObj(T t) throws Exception {
        byte[] rowkey = null;
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(RowKey.class)) {
                //设置允许访问private的值
                field.setAccessible(true);
                Object o = field.get(t);
                String s = o.toString();
                rowkey = Bytes.toBytes(s);
            }
        }
        return rowkey;
    }

    /**
     * 获取全局列族名
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> byte[] getGlobalFamily(T t) {
        byte[] globalFamily = null;
        Class<?> clazz = t.getClass();
        if (clazz.isAnnotationPresent(ColumnFamily.class)) {
            ColumnFamily columnFamily = clazz.getDeclaredAnnotation(ColumnFamily.class);
            String s = columnFamily.familyName();
            globalFamily = Bytes.toBytes(s);
        }
        return globalFamily;
    }

    /**
     * 获取每一列的列族名、列限定符和值
     *
     * @param put
     * @param globalFamily
     * @param t
     * @param <T>
     */
    private static <T> void getAllColumnFromObj(Put put, byte[] globalFamily, T t) throws Exception {
        byte[] family = null;
        byte[] qualifier = null;
        byte[] value = null;
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RowKey.class)) {
                continue;
            }
            if (field.isAnnotationPresent(PerColumn.class)) {
                PerColumn perColumn = field.getAnnotation(PerColumn.class);
                family = Bytes.toBytes(perColumn.name());
                qualifier = Bytes.toBytes(perColumn.qualifier());
            } else {
                family = globalFamily;
                qualifier = Bytes.toBytes(field.getName());
            }
            value = Bytes.toBytes(field.get(t).toString());
            put.addColumn(family, qualifier, value);
        }
    }

    /**
     * 将Result对象转为Bean
     *
     * @param result
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T resultToBean(Result result, T t) throws Exception {
        //第一步：判断result是否为空
        if (null == result) {
            return null;
        }
        //第二步：获取result中数据
        getCellDataToObj(result, t);
        return null;
    }

    /**
     * 将Result中数据转为Bean
     *
     * @param result
     * @param t
     * @param <T>
     */
    private static <T> void getCellDataToObj(Result result, T t) throws Exception {
        byte[] globalFamily = getGlobalFamily(t);
        byte[] family = null;
        byte[] qualifier = null;
        String value = null;
        String setMethodName = null;
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(RowKey.class)) {
                continue;
            }
            if (field.isAnnotationPresent(PerColumn.class)) {
                PerColumn perColumn = field.getAnnotation(PerColumn.class);
                family = Bytes.toBytes(perColumn.name());
                qualifier = Bytes.toBytes(perColumn.qualifier());
            } else {
                family = globalFamily;
                qualifier = Bytes.toBytes(field.getName());
            }
            Cell columnLatestCell = result.getColumnLatestCell(family, qualifier);
            value = Bytes.toString(CellUtil.cloneValue(columnLatestCell));
            //将value值set到Obj中
            setMethodName = getSetMethodName(field.getName());
            Method method = clazz.getMethod(setMethodName, new Class[]{field.getType()});
            method.invoke(t, value);
        }
    }

    //获取set方法名
    private static String getSetMethodName(String fieldName) {
        String firstUpperLetter = fieldName.substring(0, 1).toUpperCase();
        String methodName = "set" + firstUpperLetter + fieldName.substring(1);
        return methodName;
    }
}
