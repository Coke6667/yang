package com.coke.yangboot.modules.async;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api("线程池定时任务")
@RestController
@RequestMapping("/threadPoolTask")
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @GetMapping("/test")
    public void test(){
        try {
            asyncService.service1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
