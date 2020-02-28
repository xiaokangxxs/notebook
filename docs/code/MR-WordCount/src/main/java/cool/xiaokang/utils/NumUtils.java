package cool.xiaokang.utils;

import java.text.DecimalFormat;

/**
 * 数字格式转换工具类
 *
 * @author xiaokang
 */
public class NumUtils {
    public static double numFormat(double score) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return Double.parseDouble(decimalFormat.format(score));
    }
}
