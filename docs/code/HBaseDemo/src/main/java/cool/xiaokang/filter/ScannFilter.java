package cool.xiaokang.filter;

import cool.xiaokang.pojo.User;
import cool.xiaokang.utils.HBaseORM;
import cool.xiaokang.utils.HBaseUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Iterator;
import java.util.List;

/**
 * 过滤器
 *
 * @author xiaokang
 */
public class ScannFilter {
    public static void main(String[] args) throws Exception {
        HBaseAdmin admin = HBaseUtils.getAdmin();
        Table table = admin.getConnection().getTable("family:user");
        Scan scan = new Scan();
        RegexStringComparator regexStringComparator = new RegexStringComparator("202003.");
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, regexStringComparator);
        ColumnPrefixFilter columnPrefixFilter = new ColumnPrefixFilter("user".getBytes());
        scan.setFilter(rowFilter);
        scan.setFilter(columnPrefixFilter);
        ResultScanner scanner = table.getScanner(scan);
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()) {
            Result next = iterator.next();
            User user = new User();
            HBaseORM.resultToBean(next, user);
            System.out.println(user);
            List<Cell> cells = next.listCells();
            for (Cell cell : cells) {
                String value = Bytes.toString(CellUtil.cloneRow(cell)) + ":" + Bytes.toString(CellUtil.cloneFamily(cell)) + ":" + Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t" + Bytes.toString(CellUtil.cloneValue(cell));
                System.out.println(value);
            }
        }
    }
}
