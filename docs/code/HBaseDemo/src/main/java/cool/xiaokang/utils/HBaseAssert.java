package cool.xiaokang.utils;

import cool.xiaokang.exception.HBaseException;
import org.apache.commons.lang.StringUtils;

public class HBaseAssert {
    public static void isNull(String text) throws HBaseException {
        if (StringUtils.isBlank(text)) {
            throw new HBaseException(HBaseException.PARAME_MSG, null);
        }
    }

    public static void isNull(String text, String... str) throws HBaseException {
        if (StringUtils.isBlank(text)) {
            throw new HBaseException(HBaseException.PARAME_MSG, null);
        }
        if (str.length <= 0) {
            throw new HBaseException(HBaseException.PARAME_MSG, null);
        }
    }
}
