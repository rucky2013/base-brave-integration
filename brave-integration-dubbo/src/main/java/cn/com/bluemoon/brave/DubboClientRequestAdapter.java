package cn.com.bluemoon.brave;

import com.alibaba.dubbo.rpc.RpcContext;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.internal.Nullable;
import com.google.common.net.InetAddresses;
import com.twitter.zipkin.gen.Endpoint;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;

/**
 * 对应 CS 阶段
 * <p>
 * Created by leonwong on 2016/12/2.
 */
class DubboClientRequestAdapter implements ClientRequestAdapter {

    private final String methodName;

    private final Metadata metadata;

    DubboClientRequestAdapter(String methodName, Metadata metadata) {
        this.methodName = methodName;
        this.metadata = metadata;
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
        return Collections.singleton(KeyValueAnnotation.create(
                TraceKeysExt.DUBBO_CONSUMER_URL.getKey(), metadata.getRequestUrl()));
    }

    public Endpoint serverAddress() {
        return Endpoint.builder().serviceName(metadata.getAppName())
                .ipv4(metadata.getIpv4())
                .port(metadata.getPort()).build();
    }

    static class Metadata {
        private String ipv4;
        private int port;
        private String requestUrl;
        private String appName;

        public String getAppName() {
            return appName;
        }

        public Metadata setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Metadata setPort(int port) {
            this.port = port;
            return this;
        }

        public int getIpv4() {
            InetAddress inetAddress = InetAddresses.fromInteger(0);
            try {
                inetAddress = InetAddresses.forString(this.ipv4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return InetAddresses.coerceToInteger(inetAddress);
        }

        public Metadata setIpv4(String ipv4) {
            this.ipv4 = ipv4;
            return this;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public Metadata setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }
    }
}
