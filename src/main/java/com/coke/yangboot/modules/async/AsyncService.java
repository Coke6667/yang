package com.coke.yangboot.modules.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {
    @Async("taskExecutor")
    @Scheduled(cron = "*/10 * * * * ?")
    public void service1() throws InterruptedException {
        log.info("--------start-service1------------");
        Thread.sleep(5000); // 模拟耗时
        log.info("--------end-service1------------");
    }

    @Async("taskExecutor")
    @Scheduled(cron = "*/5 * * * * ?")
    public void service2() throws InterruptedException {

        log.info("--------start-service2------------");
        Thread.sleep(2000); // 模拟耗时
        log.info("--------end-service2------------");

    }
}
