package cool.xiaokang.pojo;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 天气序列化实体类
 *
 * @author xiaokang
 */
public class Weather implements Writable {
    //温度
    private double temperature;

    public Weather() {
    }

    public void set(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(temperature);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.temperature = dataInput.readDouble();
    }

    @Override
    public String toString() {
        return temperature + "";
    }


    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
