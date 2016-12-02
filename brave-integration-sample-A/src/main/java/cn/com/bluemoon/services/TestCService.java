package cn.com.bluemoon.services;

import org.springframework.stereotype.Service;

/**
 * Created by leonwong on 2016/11/28.
 */
@Service
public class TestCService {

    public void invokeC() {
        System.out.println("A-->B-->C 调用成功！");
    }
}
