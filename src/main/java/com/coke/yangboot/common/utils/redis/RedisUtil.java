package com.coke.yangboot.common.utils.redis;

import com.coke.yangboot.common.constant.CacheTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 系统默认的缓存时间
     */
    private static long DEFAULT_CACHE_TIME = 60 * 10L;

    public  RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //=================================   通用处理  =======================================


    /**
     * 进行key的通用处理，添加 project 前缀，以防多个工程共用一个redis 数据库，导致key值 冲突问题
     *
     * @param key key
     * @return 处理之后的key 值
     * @author limingyang
     * @since 2023/4/23 14:21
     */
    private String getStringKey(String key) {
        try {
            Assert.isTrue(!StringUtils.isEmpty(key), "缓存key 不能为空");
            if (key.startsWith(CacheTimeUtil.PROJECT_NAME)) {
                return key;
            }
            return CacheTimeUtil.PROJECT_NAME + key;
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    /**
     * 设置过期key过期时间
     *
     * @param key  key
     * @param time 时间
     */
    public void setExpireTime(String key, long time) {
        try {
            redisTemplate.expire(getStringKey(key), time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(getStringKey(key), TimeUnit.SECONDS);
    }

    /**
     * 删除缓存key
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            setExpireTime(getStringKey(key), 0);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(getStringKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //============================String=============================
    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object getCache(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    public Object get(String key) {

        try {
            return getCache(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    /**
     * 默认情况下 ，如果不设置过期 时间，则采用系统默认的缓存时间
     *
     * @param key   key
     * @param value value
     * @return 是否保存成功
     */
    public boolean set(String key, Object value) {

        try {
            return setCache(getStringKey(key), value, DEFAULT_CACHE_TIME);
        } catch (Exception e) {
            log.error("",e);
        }
        return false;
    }

    /**
     *
     *
     * @param key   key
     * @param value value
     * @return 是否保存成功
     */
    public boolean set(String key, Object value, long time) {

        try {
            return setCache(getStringKey(key), value, time);
        } catch (Exception e) {
            log.error("",e);
        }
        return false;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setCache(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(getStringKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setCache(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(getStringKey(key), value, time, TimeUnit.SECONDS);
            } else {
                setCache(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return Long
     */
    public Long incr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递增因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(getStringKey(key), delta);
        } catch (RuntimeException e) {
            log.error("",e);
        }
        return null;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return Long
     */
    public Long decr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递减因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(getStringKey(key), -delta);
        } catch (RuntimeException e) {
            log.error("",e);
            return null;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容,从start 到end，从左到右
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(getStringKey(key), start, end);
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 获取list缓存所有的内容
     *
     * @param key 键
     * @return List<T>
     */
    public List<Object> lGetAll(String key) {
        try {
            return redisTemplate.opsForList().range(getStringKey(key), 0, -1);
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 获取List列表的第一个元素
     *
     * @param key 键
     * @return
     */
    public Object lLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 获取List列表右边的的第一个元素，非阻塞模式直接返回
     *
     * @param key 键
     * @return ,100,TimeUnit.MILLISECONDS
     */
    public Object lRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 获取List列表右边的第一个元素，阻塞模式，直接到超时
     *
     * @param key 键
     * @return
     */
    public Object lRightPop(String key,long timeout,TimeUnit timeUnit) {
        try {
            return redisTemplate.opsForList().rightPop(getStringKey(key),timeout,timeUnit);
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(getStringKey(key), index);
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean lRightPush(String key, T value) {
        try {
            redisTemplate.opsForList().rightPush(getStringKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }
    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean lLeftPush(String key, T value) {
        try {
            redisTemplate.opsForList().leftPush(getStringKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lRightPush(String key, T value, long time) {
        try {
            redisTemplate.opsForList().rightPush(getStringKey(key), value);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean lRightPushAll(String key, List<T> value) {
        try {
            redisTemplate.opsForList().rightPushAll(getStringKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lRightPushAll(String key, List<T> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(getStringKey(key), value);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, T value) {
        try {

            redisTemplate.opsForList().set(getStringKey(key), index, value);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 移除N个值为value的对象
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, T value) {
        try {
            Long remove = redisTemplate.opsForList().remove(getStringKey(key), count, value);
            return remove;
        } catch (Exception e) {
            log.error("",e);
            return 0;
        }
    }

    // ===============================  hash cache  =================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {

        try {
            return redisTemplate.opsForHash().get(getStringKey(key), item);
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmGet(String key) {

        try {
            return redisTemplate.opsForHash().entries(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(getStringKey(key), map);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(getStringKey(key), map);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(getStringKey(key), item, value);
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(getStringKey(key), item, value);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */

    public void hDel(String key, Object... item) {

        try {
            redisTemplate.opsForHash().delete(getStringKey(key), item);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(getStringKey(key), item);
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hIncr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(getStringKey(key), item, by);
        } catch (Exception e) {
            log.error("",e);
            return 0.0d;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hDecr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(getStringKey(key), item, -by);
        } catch (Exception e) {
            log.error("",e);
            return  0.0d;
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hIncr(String key, String item, long by) {
        try {
            return redisTemplate.opsForHash().increment(getStringKey(key), item, by);
        } catch (Exception e) {
            log.error("",e);
            return 0.0d;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hDecr(String key, String item, long by) {
        try {
            return redisTemplate.opsForHash().increment(getStringKey(key), item, -by);
        } catch (Exception e) {
            log.error("",e);
            return  0.0d;
        }
    }


    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return 返回所有成员
     */
    public Set<Object> sGetAllMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(getStringKey(key), value);
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */

    public Long sAdd(String key, T... values) {
        try {
            return redisTemplate.opsForSet().add(getStringKey(key), values);
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sAddAndSetExpireTime(String key, long time, T... values) {
        try {
            Long count = redisTemplate.opsForSet().add(getStringKey(key), values);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long sGetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long sRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(getStringKey(key), values);
            return count;
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    // ==========================  zs 有序set操作方法封装 ===========================================

    // ============================set=============================

    /**
     * 在有序列表中添加元素
     *
     * @param key   键
     * @param value 值
     * @param score 权重分值
     * @return 是否成功
     * @author sunxiaofeng
     * @since 2019/7/5 9:58
     */
    public Boolean zsAdd(String key, T value, double score) {
        try {
            return redisTemplate.opsForZSet().add(getStringKey(key), value, score);
        } catch (Exception e) {
            log.error("",e);
            return false;
        }
    }

    /**
     * 对某列表中某个值的 分值进行增加
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Double zsIncrementScore(String key, T value, double score) {
        try {
            return redisTemplate.opsForZSet().incrementScore(getStringKey(key), value, score);
        } catch (Exception e) {
            log.error("",e);
            return 0D;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long zsGetSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(getStringKey(key));
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long zsRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForZSet().remove(getStringKey(key), values);
            return count;
        } catch (Exception e) {
            log.error("",e);
            return 0L;
        }
    }

    /**
     * 将某列表 按从某值到某值排列后，取前{@code count}个 ,按分值从小到大排列
     *
     * @param key    must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return 列表
     */
    public java.util.Set<Object> zsRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            java.util.Set<Object> set = redisTemplate.opsForZSet().rangeByScore(getStringKey(key), min, max, offset, count);
            return set;
        } catch (Exception e) {
            log.error("",e);
            return new HashSet<Object>();
        }
    }

    /**
     * 将某列表 按从某值到某值排列后，取前{@code count}个 ,按分值从大到小排列
     *
     * @param key    must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return 列表
     */
    public java.util.Set<Object> zsReverseRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            java.util.Set<Object> set = redisTemplate.opsForZSet().reverseRangeByScore(getStringKey(key), min, max, offset, count);
            return set;
        } catch (Exception e) {
            log.error("",e);
            return new HashSet<Object>();
        }
    }

    /**
     * 将某列表 按从某值到某值排列,按分值从小到大排列
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return 列表
     */
    public java.util.Set<Object> zsRangeByScore(String key, double min, double max) {
        try {
            java.util.Set<Object> set = redisTemplate.opsForZSet().rangeByScore(getStringKey(key), min, max);
            return set;
        } catch (Exception e) {
            log.error("",e);
            return new HashSet<Object>();
        }
    }

    /**
     * 将某列表 按从某值到某值排列,按分值从大到小排列
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return 列表
     */
    public java.util.Set<Object> zsReverseRangeByScore(String key, double min, double max) {
        try {
            java.util.Set<Object> set = redisTemplate.opsForZSet().reverseRangeByScore(getStringKey(key), min, max);
            return set;
        } catch (Exception e) {
            log.error("",e);
            return new HashSet<Object>();
        }
    }
}
