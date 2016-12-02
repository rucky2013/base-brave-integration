package cn.com.bluemoon.services;

import cn.bluemoon.api.MessageBService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * Created by leonwong on 2016/12/1.
 */
@Service
public class TestDService {

    @Reference(version = "1.0.0")
    private MessageBService messageBService;

    public void invokeD() {
        messageBService.invokeService();
        System.out.println("A-->D 调用成功！");
    }
}
