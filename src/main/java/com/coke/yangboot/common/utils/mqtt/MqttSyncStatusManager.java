package com.coke.yangboot.common.utils.mqtt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Mqtt 异步仓库
 */
public class MqttSyncStatusManager{
    public static Map<String,Object> store = new HashMap<>();

    public static void addAttr(String key,Object value){
        com.coke.yangboot.common.utils.mqtt.MqttSyncStatusManager.store.put(key,value);
    }

    public static Object getValue(String key){
        Object o = com.coke.yangboot.common.utils.mqtt.MqttSyncStatusManager.store.get(key);
        return o;
    }

    public static boolean isExistsKey(String key){
        return com.coke.yangboot.common.utils.mqtt.MqttSyncStatusManager.store.containsKey(key);
    }

    public static void deletElement(String key){
        for(Iterator<String> iterator = com.coke.yangboot.common.utils.mqtt.MqttSyncStatusManager.store.keySet().iterator(); iterator.hasNext(); ){
            String keyIt = iterator.next();
            if(keyIt == key) {
                iterator.remove();
            }
        }

    }

}
