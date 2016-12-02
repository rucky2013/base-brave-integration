package cn.com.bluemoon.brave;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by leonwong on 2016/12/2.
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class BraveDubboFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(BraveDubboFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        /**
         * 监控的 dubbo 服务，不纳入跟踪范围
         */
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return invoker.invoke(invocation);
        }

        return null;
    }
}
