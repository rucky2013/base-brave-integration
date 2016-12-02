package cn.com.bluemoon.services;

import com.github.kristofa.brave.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leonwong on 2016/11/28.
 */
@Service
public class TestBService {

    @Autowired
    private TestCService testCService;

    @Autowired
    private TestDService testDService;

    @Autowired
    private LocalTracer localTracer;

    public void invokeB() {
        localTracer.startNewSpan("testCService", "invokeC");
        testCService.invokeC();
        localTracer.finishSpan();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        localTracer.startNewSpan("testDService", "invokeD");
        testDService.invokeD();
        localTracer.finishSpan();
    }
}
