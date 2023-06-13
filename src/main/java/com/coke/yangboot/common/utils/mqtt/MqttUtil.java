package com.coke.yangboot.common.utils.mqtt;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coke.yangboot.common.constant.MqttConstant;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * mqtt 工具类
 *
 */
@Slf4j
public class MqttUtil {

    private static final String HOST = MqttConstant.SERVER_URL;
    private static String CLIENTID = MqttConstant.CLIENTID;
    private static MqttClient client = null;

    /**
     * 初始化连接
     * @param mqttCallback
     * @return
     */
    public static boolean initClient(MqttCallback mqttCallback) {
        try {
            String clientId = CLIENTID + new Date().getTime();
            com.coke.yangboot.common.utils.mqtt.MqttUtil.client = new MqttClient(HOST, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions(); // MQTT的连接设置
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            options.setConnectionTimeout(10);  // 设置超时时间 单位为秒
            options.setKeepAliveInterval(20);//会话心跳时间
            com.coke.yangboot.common.utils.mqtt.MqttUtil.client.setCallback(mqttCallback);
            com.coke.yangboot.common.utils.mqtt.MqttUtil.client.connect(options);
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取客户端
     * @return
     */
    public static MqttClient getClient(){
        return client;
    }

    public static void startInitClient(Callback callback){
        log.info("初始化客户端----");
        initClient(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                log.info(" boolwan---------------" + b);
                log.info("string ---------------" + s);
                Map<String,Object> doParams = new HashMap<>();
                doParams.put("state","connectComplete");
                doParams.put("arg1",b);
                doParams.put("arg2",s);
                callback.dosome(doParams);
            } // 设置回调函数

            public void connectionLost(Throwable cause) {
                log.error("connectionLost" + cause);
                cause.printStackTrace();
            }

            public void messageArrived(String topic, MqttMessage message) throws UnsupportedEncodingException {
                String content = new String(message.getPayload(), "UTF-8");
                log.info(content);
                //String ss = new String(content.getBytes(),"UTF-8");
                //接收到消息时调用回调方法
                if (content != null) {
                    try {
                        callback.run(topic, content, message.getQos());

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                log.info("deliveryComplete----" + token.isComplete());
            }
        });
    }
    /**
     * @param TOPIC    主题
     * @param clientId 客户端唯一标识
     */
    public static void subscribe(String TOPIC, Callback callback) {
        log.info("接收端已启动");
        log.info("订阅主题-"+TOPIC);
        int qos = 0;
        //当有客户端的时候不再重新初始化
        if(client == null) {
            startInitClient(callback);
        }
        try {
            client.unsubscribe(TOPIC);
            IMqttToken iMqttToken = client.subscribeWithResponse(TOPIC, qos);//订阅消息
            boolean complete = iMqttToken.isComplete();Map<String,Object> doParams = new HashMap<>();
            doParams.put("state","subscribe");
            if(complete){
                doParams.put("compelte",true);
            }else{
                doParams.put("compelte",false);
            }
            callback.dosome(doParams);
        } catch (MqttException e) {
            e.printStackTrace();
            callback.error(e);
        }
    }
    public static void subscribe(String[] TOPIC,Callback callback) {
        log.info("接收端已启动");
        log.info("订阅主题-"+TOPIC);
        //当有客户端的时候不再重新初始化
        if(client == null) {
            startInitClient(callback);
        }
        try {
            IMqttToken iMqttToken = client.subscribeWithResponse(TOPIC);//订阅消息
            boolean complete = iMqttToken.isComplete();
            Map<String,Object> doParams = new HashMap<>();
            doParams.put("state","subscribe");
            if(complete){
                doParams.put("compelte",true);
            }else{
                doParams.put("compelte",false);
            }
            callback.dosome(doParams);
        } catch (MqttException e) {
            e.printStackTrace();
            callback.error(e);
        }
    }
    /**
     * @param topic    主题
     * @param clientId 客户端唯一标识
     * @param content  传输信息内容
     * @return
     */
    public static int send(String topic, String clientId, String content) {
        int qos = 1; //保证消息接收者至少会收到一次，可能造成消息重复
        clientId = clientId + new Date().getTime();
        try {
            MqttClient sampleClient = new MqttClient(HOST, clientId, new MemoryPersistence());  // 创建客户端
            MqttConnectOptions connOpts = new MqttConnectOptions(); // 创建链接参数
            connOpts.setCleanSession(false); // 在重新启动和重新连接时记住状态
            sampleClient.connect(connOpts); // 建立连接
            MqttMessage message = new MqttMessage(content.getBytes());  // 创建消息
            message.setQos(qos);   // 设置消息的服务质量
            sampleClient.publish(topic, message);  // 发布消息
            log.info("发送主题：{} 内容：{}", topic, message);
            sampleClient.disconnect(); // 断开连接
            sampleClient.close(); // 关闭客户端
            return 1;
        } catch (MqttException me) {
            log.error("reason " + me.getReasonCode());
            log.error("msg " + me.getMessage());
            log.error("loc " + me.getLocalizedMessage());
            log.error("cause " + me.getCause());
            log.error("excep " + me);
            me.printStackTrace();
        }
        return 0;
    }
    public static void unsubscribe(String TOPIC)  {
        try {
            client.unsubscribe(TOPIC);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}


