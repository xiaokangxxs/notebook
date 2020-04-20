package cool.xiaokang.exception;

public class HBaseException extends Exception {
    public static final String LIST_NAMESPACE_MSG = "获取命名空间列表异常";
    public static final String PARAME_MSG = "参数为空异常";
    private String msg;
    private Exception e;

    public HBaseException(String msg, Exception e) {
        this.msg = msg;
        this.e = e;
    }

    public void printException() {
        System.out.println("异常信息>>>" + msg);
        e.getMessage();
    }
}
