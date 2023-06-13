package com.coke.yangboot.common.utils.mqtt;


import java.util.Map;

public interface Callback {
	public void run(String topic, String content, int qos) throws InterruptedException;
	public void error(Exception e);
	public default void dosome(Map<String,Object>... args){};

}
