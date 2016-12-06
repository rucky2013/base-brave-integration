package cn.com.bluemoon.brave;

import com.alibaba.dubbo.rpc.RpcException;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;
import java.util.Collections;

/**
 * 对应 CR 阶段
 * <p>
 * Created by leonwong on 2016/12/2.
 */
public class DubboClientResponseAdapter implements ClientResponseAdapter {

    private RpcException rpcException;

    public DubboClientResponseAdapter setRpcException(RpcException rpcException) {
        this.rpcException = rpcException;
        return this;
    }

    public Collection<KeyValueAnnotation> responseAnnotations() {
        return rpcException == null ? Collections.<KeyValueAnnotation>emptyList() : Collections.singleton(KeyValueAnnotation.create(
                TraceKeysExt.CONSUMER_RPC_EXCEPTION.getKey(), rpcException.getMessage()));
    }
}
