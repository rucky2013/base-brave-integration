package cn.com.bluemoon.brave;

import com.alibaba.dubbo.rpc.Invocation;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;

import java.util.Collection;
import java.util.HashSet;

import static com.github.kristofa.brave.IdConversion.convertToLong;

/**
 * 对应 SR 阶段
 * <p>
 * Created by leonwong on 2016/12/2.
 */
public class DubboServerRequestAdapter implements ServerRequestAdapter {

    private static final TraceData EMPTY_UNSAMPLED_TRACE = TraceData.builder().sample(false).build();
    private static final TraceData EMPTY_MAYBE_TRACE = TraceData.builder().build();

    private final Invocation invocation;

    private final Metadata metadata;

    public DubboServerRequestAdapter(Invocation invocation, Metadata metadata) {
        this.invocation = invocation;
        this.metadata = metadata;
    }

    public TraceData getTraceData() {
        final String sampled = invocation.getAttachment(BraveDubboHeaders.SAMPLED.name());
        if (sampled != null) {
            if (sampled.equals("0")) {
                return EMPTY_UNSAMPLED_TRACE;
            } else {
                final String parentSpanId = invocation.getAttachment(BraveDubboHeaders.PARENT_SPAN_ID.name());
                final String traceId = invocation.getAttachment(BraveDubboHeaders.TRACE_ID.name());
                final String spanId = invocation.getAttachment(BraveDubboHeaders.SPAN_ID.name());

                if (traceId != null && spanId != null) {
                    SpanId span = getSpanId(traceId, spanId, parentSpanId);
                    return TraceData.builder().sample(true).spanId(span).build();
                }
            }
        }
        return EMPTY_MAYBE_TRACE;
    }

    public String getSpanName() {
        return invocation.getMethodName();
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {
        HashSet<KeyValueAnnotation> hashSet = new HashSet<KeyValueAnnotation>();
        hashSet.add(KeyValueAnnotation.create(
                TraceKeysExt.DUBBO_PROVIDER_URL.getKey(), invocation.getInvoker().getUrl().toFullString()));
        hashSet.add(KeyValueAnnotation.create(
                TraceKeysExt.DUBBO_SERVER_ADDR.getKey(), metadata.getIpv4() + ":" + metadata.getPort() + "(" + metadata.getAppName() + ")"));
        return hashSet;
    }

    private SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder()
                .traceIdHigh(traceId.length() == 32 ? convertToLong(traceId, 0) : 0)
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }

    static class Metadata {

        private String ipv4;
        private int port;
        private String appName;

        public String getAppName() {
            return appName;
        }

        public Metadata setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public String getIpv4() {
            return ipv4;
        }

        public Metadata setIpv4(String ipv4) {
            this.ipv4 = ipv4;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Metadata setPort(int port) {
            this.port = port;
            return this;
        }
    }
}
