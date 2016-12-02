package cn.com.bluemoon.brave;

import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.internal.Nullable;
import com.twitter.zipkin.gen.Endpoint;

import java.util.Collection;
import java.util.Collections;

/**
 * 对应 CS 阶段
 * <p>
 * Created by leonwong on 2016/12/2.
 */
public class DubboClientRequestAdapter implements ClientRequestAdapter {

    private final String methodName;

    public DubboClientRequestAdapter(String methodName) {
        this.methodName = methodName;
    }

    public String getSpanName() {
        return this.methodName;
    }

    public void addSpanIdToRequest(@Nullable SpanId spanId) {
        if (spanId == null) {
            RpcContext.getContext().setAttachment(BraveDubboHeaders.SAMPLED.name(), "0");
        } else {
            RpcContext.getContext().setAttachment(BraveDubboHeaders.METHOD_NAME.name(), methodName);
            RpcContext.getContext().setAttachment(BraveDubboHeaders.SAMPLED.name(), "1");
            RpcContext.getContext().setAttachment(BraveDubboHeaders.TRACE_ID.name(), spanId.traceIdString());
            RpcContext.getContext().setAttachment(BraveDubboHeaders.SPAN_ID.name(), IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                RpcContext.getContext().setAttachment(BraveDubboHeaders.PARENT_SPAN_ID.name(), IdConversion.convertToString(spanId.parentId));
            }
        }
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {
        return Collections.emptyList();
    }

    public Endpoint serverAddress() {
        return null;
    }
}
