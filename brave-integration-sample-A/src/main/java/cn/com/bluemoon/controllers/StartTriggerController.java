package cn.com.bluemoon.controllers;

import cn.com.bluemoon.services.TestAService;
import com.github.kristofa.brave.LocalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by leonwong on 2016/11/28.
 */
@Controller
@RequestMapping(value = "/trigger", method = RequestMethod.GET)
public class StartTriggerController {

    @Autowired
    private TestAService testAService;

    @Autowired
    private LocalTracer localTracer;

    @ResponseBody
    @RequestMapping(value = "/run")
    public String run() throws IOException {
        try {
            localTracer.startNewSpan("testAService", "invokeA");
            testAService.invokeA();
        } finally {
            localTracer.finishSpan();
        }
        return "OK";
    }
}
