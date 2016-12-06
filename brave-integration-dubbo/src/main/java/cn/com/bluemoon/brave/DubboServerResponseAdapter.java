package cn.com.bluemoon.brave;

import com.alibaba.dubbo.rpc.RpcException;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;

import java.util.Collection;
import java.util.Collections;

/**
 * 对应 SS 阶段
 * <p>
 * Created by leonwong on 2016/12/2.
 */
public class DubboServerResponseAdapter implements ServerResponseAdapter {

    private RpcException rpcException;

    public DubboServerResponseAdapter setRpcException(RpcException rpcException) {
        this.rpcException = rpcException;
        return this;
    }

    public Collection<KeyValueAnnotation> responseAnnotations() {
        return rpcException == null ? Collections.<KeyValueAnnotation>emptyList() : Collections.singleton(KeyValueAnnotation.create(
                TraceKeysExt.PROVIDER_RPC_EXCEPTION.getKey(), rpcException.getMessage()));
    }
}
