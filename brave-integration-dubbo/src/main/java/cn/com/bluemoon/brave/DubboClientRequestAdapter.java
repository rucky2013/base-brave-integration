package cn.com.bluemoon.brave;

import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.twitter.zipkin.gen.Endpoint;

import java.util.Collection;

/**
 * 对应 CS 阶段
 *
 * Created by leonwong on 2016/12/2.
 */
public class DubboClientRequestAdapter implements ClientRequestAdapter {

    public String getSpanName() {
        return null;
    }

    public void addSpanIdToRequest(SpanId spanId) {

    }

    public Collection<KeyValueAnnotation> requestAnnotations() {
        return null;
    }

    public Endpoint serverAddress() {
        return null;
    }
}
