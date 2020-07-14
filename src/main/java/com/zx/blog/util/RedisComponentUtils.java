package com.zx.blog.util;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponentUtils{
 
	@Resource
    private RedisTemplate<String, Object> redisTemplate;
 
 
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return boolean
     */
    
    public boolean set(String key, Object value) {
        boolean result = false;
        redisTemplate.opsForValue().set(key, value);
 
        result = true;
        return result;
    }
 
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return boolean
     */
    
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        result = true;
        return result;
    }
 
    /**
     * 批量删除对应的value
     * @param keys
     */
    
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
 
    /**
     * 批量删除key
     * @param pattern
     */
    
    public void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }
 
    /**
     * 删除对应的value
     * @param key
     */
    
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
 
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
 
    /**
     * 读取缓存
     * @param key
     * @return
     */
    
    public Object get(String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    
    public void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }
 
    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    
    public Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }
 
    /**
     * 列表添加
     * @param k
     * @param v
     */
    
    public void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }
 
    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    
    public List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }
 
    /**
     * 集合添加
     * @param key
     * @param value
     */
    
    public void setArray(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }
 
    /**
     * 集合获取
     * @param key
     * @return
     */
    
    public Set<Object> getArray(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }
 
    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    
    public void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }
 
    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }
 
}
