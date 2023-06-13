//package com.coke.yangboot.modules.rocketMQ;
//
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Component;
//
//@RocketMQMessageListener(topic = "test_topic",
//        consumerGroup = "consumer-group-test",
//        selectorExpression = "*")   //selectorExpression = "tagA || tagB"  过滤
//@Component
//public class DemoConsumer implements RocketMQListener<String> {
//    @Override
//    public void onMessage(String message) {
//        System.out.println("receive message: " + message);
//    }
//}
