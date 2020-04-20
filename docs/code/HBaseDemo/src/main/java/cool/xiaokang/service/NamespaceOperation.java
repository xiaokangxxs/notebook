package cool.xiaokang.service;

import cool.xiaokang.exception.HBaseException;

import java.util.List;

public interface NamespaceOperation {
    //获取所有命名空间
    List<String> listNamespace() throws HBaseException;
}
