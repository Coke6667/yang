package com.coke.yangboot.common.constant;

public interface CacheTimeUtil {
    /**
     * 缓存当中的 通用工程名称， 以防多个工程 共用一个redis导致缓存key冲突
     * 不同的工程一定要进行修改之，或者不要跟其他项目共用 一个redis数据库。
     *
     * @author limingyang
     * @since 2023/4/23 14:33
     */
    String PROJECT_NAME =  "yang_";

    /**
     * 通用的一般缓存时间 定义
     */
    int COMMON_CACHE_TIME = 60 * 2;
    /**
     * 通用的默认缓存时间为 120秒，第一段为缓存池名称，第二段为缓存时间（单位：秒）
     */
    String COMMON_CACHE_FOR_CACHEABLE = PROJECT_NAME + "common_cache_area#" + COMMON_CACHE_TIME;
    /**
     * 较短的缓存时间 定义
     */
    int SHORT_CACHE_TIME = 60;
    /**
     * 较短的缓存时间 60秒过期
     */
    String SHORT_COMMON_CACHE_FOR_CACHEABLE = PROJECT_NAME +"short_common_cache_area#" + SHORT_CACHE_TIME;
    /**
     * 登录错误锁定时长
     */
    int LOGIN_ERROR_LOCKTIME = 60 * 5;

    /**
     * 通用的一般缓存时间 定义
     */
    int COMMON_LONG_CACHE_TIME = 60 * 10;
}
