package com.coke.yangboot.common.constant;

/**
 * @author Sk
 * @date 2022/12/7 9:51
 **/
public interface MqttConstant {

    /**
     * 测试主题
     */
    public  String TOPIC_1="yhzbcwd/ndz001/report";
    public  String TOPIC_2="yhzbcwd/ndz001/deviceBasicInfoUp";
    public  String TOPIC_3="yhzbcwd/ndz001/deviceBasicInfoUp";
    public  String TOPIC_4="yhzbcwd/ndz001/deviceBasicInfoGet";
    public  String TOPIC_5="yhzbcwd/ndz001/restart";
    public  String PREFIX="yhzbcwd/";
    public  String SUFFIX="/report";
    public  String RESET_SUFFIX="/restart";
    /**
     * mqtt地址
     */
    public String SERVER_URL="tcp://172.16.30.236:1883";

    /**
     * mqtt clientId
     */
    public String CLIENTID="mqttx_3caef4cb";
}
