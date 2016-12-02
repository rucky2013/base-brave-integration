package cn.com.bluemoon.brave;

import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.TraceData;

import java.util.Collection;

/**
 * 对应 SR 阶段
 *
 * Created by leonwong on 2016/12/2.
 */
public class DubboServerRequestAdapter implements ServerRequestAdapter{
    public TraceData getTraceData() {
        return null;
    }

    public String getSpanName() {
        return null;
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {
        return null;
    }
}
