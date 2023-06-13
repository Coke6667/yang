package com.coke.yangboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YangbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(YangbootApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  YangBoot启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "__  __                  ____              __ \n" +
                "\\ \\/ /___ _____  ____ _/ __ )____  ____  / /_  \n" +
                " \\  / __ `/ __ \\/ __ `/ __  / __ \\/ __ \\/ __/\n" +
                " / / /_/ / / / / /_/ / /_/ / /_/ / /_/ / /_\n" +
                "/_/\\__,_/_/ /_/\\__, /_____/\\____/\\____/\\__/\n" +
                "              /____/ ");


    }

}
