package com.coke.yangboot.common.utils.cocurrent;

import java.util.concurrent.*;

/**
 * 线程池定时任务（单例模式）
 *
 * @author limingyang
 * @since 2023/3/14 16:05
 */
public class ScheduledExecutorUtil {
    private volatile static ScheduledExecutorService scheduledExecutorService;

    /**
     * 记录任务标志
     */
    private static final ConcurrentHashMap<String,ScheduledFuture> taskMap=new ConcurrentHashMap();

    /**
     * 核心线程数
     */
    private static final int coreThread=10;


    private ScheduledExecutorUtil(){

    }
    public static ScheduledExecutorService getScheduledExecutorService(){
        if(scheduledExecutorService==null){
            synchronized (ScheduledExecutorUtil.class){
                if(scheduledExecutorService==null){
                    scheduledExecutorService=Executors.newScheduledThreadPool(coreThread);
                }

            }
        }
        return scheduledExecutorService;
    }

    /**
     * 中断任务
     */
    public static boolean interruptTask(String taskId){
        boolean flag=false;
        ScheduledFuture scheduledFuture=taskMap.get(taskId);
        if(scheduledFuture!=null){
            flag=scheduledFuture.cancel(true);
        }
        return flag;
    }

    /**
     * 清除任务id
     */
    public static void removeTask(String taskId){
        taskMap.remove(taskId);
    }

    /**
     * 提交任务
     */
    public static void submitTask(String taskId,int delay,Runnable runnable){
        ScheduledExecutorService executorService=getScheduledExecutorService();
        ScheduledFuture scheduledFuture = executorService.schedule(runnable, delay, TimeUnit.MINUTES);
        taskMap.put(taskId, scheduledFuture);
    }
}
