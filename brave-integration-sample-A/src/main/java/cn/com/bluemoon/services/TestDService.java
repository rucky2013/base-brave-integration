package cn.com.bluemoon.services;

import org.springframework.stereotype.Service;

/**
 * Created by leonwong on 2016/12/1.
 */
@Service
public class TestDService {

    public void invokeD() {
        System.out.println("A-->D 调用成功！");
    }
}
