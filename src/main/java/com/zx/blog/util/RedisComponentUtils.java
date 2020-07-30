package com.zx.blog.util;

import com.google.common.base.Preconditions;
import com.zx.blog.entity.Blog;
import com.zx.blog.handler.BloomFilterHelper;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponentUtils {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	/**
	 * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 实现命令：INCR key，增加key一次
	 *
	 * @param key
	 * @return
	 */
	public Long incr(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 写入缓存
	 *
	 * @param key
	 * @param value
	 * @return boolean
	 */

	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 写入缓存设置时效时间
	 *
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
	 *
	 * @param keys
	 */

	public void remove(String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 *
	 * @param pattern
	 */

	public void removePattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0) {
			redisTemplate.delete(keys);
		}
	}

	/**
	 * 删除对应的value
	 *
	 * @param key
	 */

	public void remove(String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 *
	 * @param key
	 * @return
	 */

	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 *
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
	 *
	 * @param key
	 * @param hashKey
	 * @param value
	 */

	public void hmSet(String key, Object hashKey, Object value) {
		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
		hash.put(key, hashKey, value);
	}

	/**
	 * 哈希获取数据
	 *
	 * @param key
	 * @param hashKey
	 * @return
	 */

	public Object hmGet(String key, Object hashKey) {
		HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
		return hash.get(key, hashKey);
	}

	/**
	 * 列表添加
	 *
	 * @param k
	 * @param v
	 */

	public void lPush(String k, Object v) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		list.rightPush(k, v);
	}

	/**
	 * 列表获取
	 *
	 * @param k
	 * @param l
	 * @param l1
	 * @return
	 */

	public List<Object> lRange(String k, long l, long l1) {
		ListOperations<String, Object> list = redisTemplate.opsForList();
		return list.range(k, l, l1);
	}

	/**
	 * 集合添加
	 *
	 * @param key
	 * @param value
	 */

	public void setArray(String key, Object value) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		set.add(key, value);
	}

	/**
	 * 集合获取
	 *
	 * @param key
	 * @return
	 */

	public Set<Object> getArray(String key) {
		SetOperations<String, Object> set = redisTemplate.opsForSet();
		return set.members(key);
	}

	/**
	 * 有序集合添加
	 *
	 * @param key
	 * @param value
	 * @param scoure
	 */

	public void zAdd(String key, Object value, double scoure) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		zset.add(key, value, scoure);
	}

	/**
	 * 有序集合获取
	 *
	 * @param key
	 * @param scoure
	 * @param scoure1
	 * @return
	 */

	public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.rangeByScore(key, scoure, scoure1);
	}

	/**
	 * ZSet获取全部
	 * @param key
	 * @param scoure
	 * @param scoure1
	 * @return
	 */
	public Set<Object> zRange(String key, int scoure, int scoure1) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.range(key, scoure, scoure1);
	}

	/**
	 * 删除集合中数据
	 * @param key
	 * @param o
	 */
	public void remove(String key, Object o) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		zset.remove(key, o);
	}


	/**
	 * 根据给定的布隆过滤器添加值
	 */
	public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
		Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
		int[] offset = bloomFilterHelper.murmurHashOffset(value);
		for (int i : offset) {
			redisTemplate.opsForValue().setBit(key, i, true);
		}
	}

	/**
	 * 根据给定的布隆过滤器判断值是否存在
	 */
	public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
		Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
		int[] offset = bloomFilterHelper.murmurHashOffset(value);
		for (int i : offset) {
			if (!redisTemplate.opsForValue().getBit(key, i)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 存放单个hash缓存
	 *
	 * @param key   键
	 * @param hkey  键
	 * @param value 值
	 * @return
	 */
	public void hput(String key, String hkey, Object value) {
		redisTemplate.opsForHash().put(key, hkey, value);
	}

	/**
	 * 分页存取数据
	 *
	 * @param key   hash存取的key
	 * @param hkey  hash存取的hkey
	 * @param score 指定字段排序
	 * @param value
	 * @return
	 */
	public void setPage(String key, String hkey, double score, Object value) {
		zAdd(key + ":page", hkey, score);
		hput(key, hkey, value);
	}

	/**
	 * 分页取出 hash中hkey值
	 *
	 * @param key
	 * @param offset
	 * @param count
	 * @return
	 */
	public Set<Object> getPage(String key, int offset, int count) {
		ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
		return zset.reverseRangeByScore(key + ":page", 0, 10000, (offset - 1) * count, count);
		//1 100000代表score的排序氛围值，即从1-100000的范围

	}

	/**
	 * 计算key值对应的数量
	 *
	 * @param key
	 * @return
	 */
	public Long getPageSize(String key) {
		return redisTemplate.opsForZSet().zCard(key + ":page");
	}

	/**
	 * 删除分页信息中的数据
	 * @param key
	 * @param hkey
	 */
	public void delPage(String key, String hkey) {
		redisTemplate.opsForHash().delete(key,hkey);
		redisTemplate.opsForZSet().remove(key +":page",hkey);
	}
}
