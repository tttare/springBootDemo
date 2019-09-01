package com.tttare.springDemo.common.cache;

/***
 *自定义redis操作类
 */
public interface IRedis {

    <T> T getObject(String key,Class<T> clazz);

    <T> void setObject(String key,T value,Long times);
}
