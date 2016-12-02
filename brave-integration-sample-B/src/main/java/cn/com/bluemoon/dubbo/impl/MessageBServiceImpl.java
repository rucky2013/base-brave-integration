package cn.com.bluemoon.dubbo.impl;

import cn.bluemoon.api.MessageBService;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * Created by leonwong on 2016/12/1.
 */
@Service(version = "1.0.0")
public class MessageBServiceImpl implements MessageBService {
    @Override
    public void invokeService() {
        System.out.println("MessageBServiceImpl 调用成功");
    }
}
