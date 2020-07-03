package com.imooc.miaosha.redis;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix, String key, Class<T> cls) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            String value = jedis.get(realKey);
            T t = strToBean(value, cls);
            return t;
        }
        finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeyPrefix prefix, String key, T value) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = beanToString(value);
            if(str == null || str.length() <= 0)
                return false;
            int seconds = prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }

            return true;
        }
        finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean exists(KeyPrefix prefix, String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }
        finally {
            returnToPool(jedis);
        }
    }

    public <T> Long incr(KeyPrefix prefix, String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }
        finally {
            returnToPool(jedis);
        }
    }

    public <T> Long decr(KeyPrefix prefix, String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }
        finally {
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {

        if(value == null) return null;
        Class<?> cls = value.getClass();
        if(cls == int.class || cls == Integer.class) {
            return "" + value;
        }
        else if(cls == String.class) {
            return (String) value;
        }
        else if(cls == long.class || cls == Long.class) {
            return "" + value;
        }
        else
            return JSON.toJSONString(value);
    }

    /**
     * 将字符串转化为Bean对象
     * @param value
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    private <T> T strToBean(String value, Class<T> cls) {

        if(value == null || value.length() <= 0 || cls == null)
            return null;

        if(cls == int.class || cls == Integer.class) {
            return (T)Integer.valueOf(value);
        }
        else if(cls == String.class) {
            return (T)value;
        }
        else if(cls == long.class || cls == Long.class) {
            return (T)Long.valueOf(value);
        }
        else
            return JSON.toJavaObject(JSON.parseObject(value), cls);
    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null)
            jedis.close();
    }

}
