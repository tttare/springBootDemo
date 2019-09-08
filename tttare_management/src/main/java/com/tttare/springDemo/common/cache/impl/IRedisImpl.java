package com.tttare.springDemo.common.cache.impl;

import com.alibaba.fastjson.JSONObject;
import com.tttare.springDemo.common.cache.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("redisUtil")
public class IRedisImpl implements IRedis {

    private static final long defaultexpires = 24*60*60l ; // 默认过期时间

    @Autowired
    private  RedisTemplate<String, String> redisTemplate;


    public String get(String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    public void set(String key, String value, long validTime) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                connection.expire(serializer.serialize(key), validTime);
                return true;
            }
        });
    }

    // Java Object Operate
    public <T> T getObject(String key,Class<T> clazz){
        return JSONObject.parseObject(get(key),clazz);
    }

    public <T> void setObject(String key,T value,Long times) {
        times = times == null ? defaultexpires : times;
        set(key,JSONObject.toJSONString(value),times);
    }

}
