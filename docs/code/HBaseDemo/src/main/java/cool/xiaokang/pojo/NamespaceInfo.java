package cool.xiaokang.pojo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public class NamespaceInfo {
    //nameSpace名称
    private String name;
    //获取namespace的配置信息
    private Map<String, String> config;
    //获取namespace下的表的数据量
    private long tableNum;
    //获取可用的标的数量
    private long enableTableNum;
    //获取不可用表的数量
    private long disableTableNum;
    //获取所有表名
    private List<String> tableNames;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public long getTableNum() {
        return tableNum;
    }

    public void setTableNum(long tableNum) {
        this.tableNum = tableNum;
    }

    public long getEnableTableNum() {
        return enableTableNum;
    }

    public void setEnableTableNum(long enableTableNum) {
        this.enableTableNum = enableTableNum;
    }

    public long getDisableTableNum() {
        return disableTableNum;
    }

    public void setDisableTableNum(long disableTableNum) {
        this.disableTableNum = disableTableNum;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }
}
