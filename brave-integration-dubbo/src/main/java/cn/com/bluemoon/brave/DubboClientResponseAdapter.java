package cn.com.bluemoon.brave;

import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;

import java.util.Collection;

/**
 * 对应 CR 阶段
 *
 * Created by leonwong on 2016/12/2.
 */
public class DubboClientResponseAdapter implements ClientResponseAdapter{
    public Collection<KeyValueAnnotation> responseAnnotations() {
        return null;
    }
}
