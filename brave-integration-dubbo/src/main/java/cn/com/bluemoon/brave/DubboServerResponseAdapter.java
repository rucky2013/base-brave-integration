package cn.com.bluemoon.brave;

import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;

import java.util.Collection;

/**
 * 对应 SS 阶段
 * <p>
 * Created by leonwong on 2016/12/2.
 */
public class DubboServerResponseAdapter implements ServerResponseAdapter {
    public Collection<KeyValueAnnotation> responseAnnotations() {
        return null;
    }
}
