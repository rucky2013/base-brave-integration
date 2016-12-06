package cn.com.bluemoon.brave;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参考 http://dubbo.io/Developer+Guide-zh.htm#DeveloperGuide-zh-%E6%89%A9%E5%B1%95%E7%82%B9%E5%8A%A0%E8%BD%BD
 * <p>
 * Created by leonwong on 2016/12/2.
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class BraveDubboFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(BraveDubboFilter.class);

    /**
     * 不要用注解的方式扩展
     */
    private ClientRequestInterceptor clientRequestInterceptor;

    private ClientResponseInterceptor clientResponseInterceptor;

    private ServerRequestInterceptor serverRequestInterceptor;

    private ServerResponseInterceptor serverResponseInterceptor;

    public void setClientRequestInterceptor(ClientRequestInterceptor clientRequestInterceptor) {
        this.clientRequestInterceptor = clientRequestInterceptor;
    }

    public BraveDubboFilter setClientResponseInterceptor(ClientResponseInterceptor clientResponseInterceptor) {
        this.clientResponseInterceptor = clientResponseInterceptor;
        return this;
    }

    public BraveDubboFilter setServerRequestInterceptor(ServerRequestInterceptor serverRequestInterceptor) {
        this.serverRequestInterceptor = serverRequestInterceptor;
        return this;
    }

    public BraveDubboFilter setServerResponseInterceptor(ServerResponseInterceptor serverResponseInterceptor) {
        this.serverResponseInterceptor = serverResponseInterceptor;
        return this;
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        /*
          监控的 dubbo 服务，不纳入跟踪范围
         */
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return invoker.invoke(invocation);
        }

        RpcContext context = RpcContext.getContext();
        /*
          调用的方法名
          以此作为 span name
         */
        String methodName = invocation.getMethodName();

        /*
          provider 应用相关信息
         */
        String interfaceName = context.getUrl().getServiceInterface();
        String invokeUrl = context.getUrl().toFullString();
        String ipv4 = context.getUrl().getIp();
        int port = context.getUrl().getPort();

        if ("0".equals(invocation.getAttachment(BraveDubboHeaders.SAMPLED.name()))) {
            return invoker.invoke(invocation);
        }

        RpcException rpcException = null;

        if (context.isConsumerSide()) {
            /*
              Client 端
             */
            DubboClientRequestAdapter.Metadata metadata =
                    new DubboClientRequestAdapter.Metadata()
                            .setIpv4(ipv4)
                            .setPort(port)
                            .setRequestUrl(invokeUrl)
                            // TODO 这里应该用 application name
                            .setAppName(interfaceName);
            final DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(methodName, metadata);
            clientRequestInterceptor.handle(clientRequestAdapter);

            Result result = null;

            try {
                result = invoker.invoke(invocation);
            } catch (RpcException e) {
                rpcException = e;
            } finally {
                final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter();
                clientResponseAdapter.setRpcException(rpcException);
                clientResponseInterceptor.handle(clientResponseAdapter);
            }
            return result;
        } else if (context.isProviderSide()) {
            /*
              Server 端
             */
            final DubboServerRequestAdapter serverRequestAdapter = new DubboServerRequestAdapter(invocation);
            serverRequestInterceptor.handle(serverRequestAdapter);

            Result result = null;

            try {
                result = invoker.invoke(invocation);
            } catch (RpcException e) {
                rpcException = e;
            } finally {
                final DubboServerResponseAdapter serverResponseAdapter = new DubboServerResponseAdapter();
                serverResponseAdapter.setRpcException(rpcException);
                serverResponseInterceptor.handle(serverResponseAdapter);
            }
            return result;
        }
        return invoker.invoke(invocation);
    }
}
