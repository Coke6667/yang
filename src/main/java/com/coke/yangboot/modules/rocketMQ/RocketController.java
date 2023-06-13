package com.coke.yangboot.modules.rocketMQ;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rocket")
@Api(tags = "消息队列")
public class RocketController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @GetMapping("/noTag")
    public void noTag() {
        rocketMQTemplate.convertAndSend("test_topic", "hello world");
    }

    @GetMapping("/tagA")
    public void tagA() {
        rocketMQTemplate.convertAndSend("test_topic:tagA", "hello world tagA");
    }

    @GetMapping("tagB")
    public void tagB() {
        rocketMQTemplate.convertAndSend("test_topic:tagB", "hello world tagB");
    }

    @ApiOperation(value = "发送同步消息")
    @GetMapping("sync")
    public void sync() {
        String message = "sync消息";
        String id = "sync1";
        Message<String> strMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, id).build();
        SendResult result = rocketMQTemplate.syncSend("test_topic:sync-tags", strMessage);
        System.out.println("发送简单同步消息成功!返回信息为:{}:"+ JSON.toJSONString(result));
    }

    @ApiOperation(value = "发送异步消息")
    @GetMapping("async")
    public void async(){
        String message = "async消息";
        String id = "async1";
        Message<String> strMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, id).build();
        rocketMQTemplate.asyncSend("test_topic:async-tags", strMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                    System.out.println("发送简单异步消息成功!返回信息为:{}"+ JSON.toJSONString(sendResult));
                }
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送简单异步消息失败!异常信息为:{}"+ throwable.getMessage());
            }
        });
    }

    @ApiOperation(value = "发送单向消息")
    @GetMapping("oneway")
    public void oneway(){
        String message = "oneway消息";
        String id = "oneway1";
        Message<String> strMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS,id).build();

        rocketMQTemplate.sendOneWay("test_topic:oneway",strMessage);
    }


}
