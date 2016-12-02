package cn.com.bluemoon.services;

import com.github.kristofa.brave.LocalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leonwong on 2016/11/28.
 */
@Service
public class TestAService {

    @Autowired
    private TestBService testBService;

    @Autowired
    private LocalTracer localTracer;

    public void invokeA() {
        try {
            localTracer.startNewSpan("testBService", "invokeB");
            testBService.invokeB();
        } finally {
            localTracer.finishSpan();
        }
    }
}
