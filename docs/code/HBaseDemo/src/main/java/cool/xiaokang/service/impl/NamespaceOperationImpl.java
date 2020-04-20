package cool.xiaokang.service.impl;

import cool.xiaokang.exception.HBaseException;
import cool.xiaokang.service.NamespaceOperation;
import cool.xiaokang.utils.HBaseUtils;
import org.apache.hadoop.hbase.NamespaceDescriptor;

import java.io.IOException;
import java.util.*;

public class NamespaceOperationImpl implements NamespaceOperation {

    @Override
    public List<String> listNamespace() throws HBaseException {
        List<String> result = null;
        try {
            NamespaceDescriptor[] namespaceDescriptors = HBaseUtils.getAdmin().listNamespaceDescriptors();
            result = new ArrayList<>(namespaceDescriptors.length);
            for (NamespaceDescriptor namespaceDescriptor : namespaceDescriptors) {
                Map<String, String> conf = namespaceDescriptor.getConfiguration();
                Set<String> keys = conf.keySet();
                for (String key : keys) {
                    result.add(key + "---" + conf.get(key));
                    System.out.println(key + "---" + conf.get(key));
                }
                result.add(namespaceDescriptor.getName());
            }
        } catch (Exception e) {
            throw new HBaseException(HBaseException.LIST_NAMESPACE_MSG, e);
        }
        return result;
    }
}
