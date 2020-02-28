package cool.xiaokang.pojo;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 序列化案例
 *
 * @author xiaokang
 */
//1。实现Writable接口,自定义比较规则的话也要实现WritableComparable接口
public class Transaction implements Writable, WritableComparable<Transaction> {
    //交易金额
    private double tradeMoney;
    //退款金额
    private double refundMoney;
    //评分
    private int mark;
    //平均交易金额
    private double sumTradeMoney;
    //平均退款金额
    private double sumRefundMoney;
    //平均评分
    private double avgMark;
    //提示信息
    private String tips;

    //2.反序列化时需要反射调用空构造方法
    public Transaction() {
    }

    public void setThree(double tradeMoney, double refundMoney, int mark) {
        this.tradeMoney = tradeMoney;
        this.refundMoney = refundMoney;
        this.mark = mark;
    }

    public void setThreeOutput(double sumTradeMoney, double sumRefundMoney, Double avgMark) {
        this.sumTradeMoney = sumTradeMoney;
        this.sumRefundMoney = sumRefundMoney;
        this.avgMark = avgMark;
    }

    //3.重写序列化方法
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(tradeMoney);
        out.writeDouble(refundMoney);
        out.writeInt(mark);
        out.writeDouble(sumTradeMoney);
        out.writeDouble(sumRefundMoney);
        out.writeDouble(avgMark);
    }

    //4.重写反序列化方法
    //5.反序列方法读顺序和写序列方法的写顺序必须一致
    @Override
    public void readFields(DataInput in) throws IOException {
        tradeMoney = in.readDouble();
        refundMoney = in.readDouble();
        mark = in.readInt();
        sumTradeMoney = in.readDouble();
        sumRefundMoney = in.readDouble();
        avgMark = in.readDouble();
    }


    //6.重写toString，方便后续打印到文本
    @Override
    public String toString() {
        return sumTradeMoney + "|" + sumRefundMoney + "|" + avgMark;
    }

    @Override
    public int compareTo(Transaction o) {
        int result;
        if (this.avgMark > o.avgMark) {
            //评分降序
            result = -1;
        } else if (this.avgMark < o.avgMark) {
            result = 1;
        } else {
            //按总退款金额升序
            result = this.sumRefundMoney > o.sumRefundMoney ? 1 : -1;
        }
        return result;
    }

    public double getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(double tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public double getSumTradeMoney() {
        return sumTradeMoney;
    }

    public void setSumTradeMoney(double sumTradeMoney) {
        this.sumTradeMoney = sumTradeMoney;
    }

    public double getSumRefundMoney() {
        return sumRefundMoney;
    }

    public void setSumRefundMoney(double sumRefundMoney) {
        this.sumRefundMoney = sumRefundMoney;
    }

    public double getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(double avgMark) {
        this.avgMark = avgMark;
    }

}
