/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 6:21 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 6:21 PM
 */
@Component
@Slf4j
public class RedisClient {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 键值对设值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <K, V> Boolean set(K key, V value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.set(JSON.toJSONBytes(key), JSON.toJSONBytes(value));
            return true;
        });
    }

    /**
     * 键值对设值和有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间(单位：秒)
     * @return
     */
    public <K, V> Boolean setEx(K key, V value, long time) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.setEx(JSON.toJSONBytes(key), time, JSON.toJSONBytes(value));
            return true;
        });
    }

    /**
     * 查询键值对
     *
     * @param key           键
     * @param typeReference 返回类型
     * @param <K>           键类型
     * @param <R>           返回类型
     * @return
     */
    public <K, R> R get(K key, TypeReference<R> typeReference) {
        byte[] redisValue = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(JSON.toJSONBytes(key)));
        if (redisValue == null) return null;
        try {
            return JSONObject.parseObject(new String(redisValue,"UTF-8"), typeReference);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 存储Hash结构数据(批量)
     *
     * @param outerKey 外键
     * @param map      内键-内值
     * @return
     */
    public <O, I, V> Boolean hSetMap(O outerKey, Map<I, V> map) {
        if (map == null || map.isEmpty()) return false;
        Map<byte[], byte[]> byteMap = new HashMap<>(2);
        map.forEach((innerKey, innerValue) -> byteMap.put(JSON.toJSONBytes(innerKey), JSON.toJSONBytes(innerValue)));
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.hMSet(JSON.toJSONBytes(outerKey), byteMap);
            return true;
        });
    }

    /**
     * 存储Hash结构数据
     *
     * @param outerKey   外键
     * @param innerKey   内键
     * @param innerValue 值
     * @return
     */
    public <O, I, V> Boolean hSet(O outerKey, I innerKey, V innerValue) {
        Map<I, V> map = new HashMap<>(1);
        map.put(innerKey, innerValue);
        return this.hSetMap(outerKey, map);
    }

    /**
     * 获取Hash结构Map集合，内键和内值键值对封装成Map集合
     *
     * @param outerKey       外键
     * @param innerKeyType   内键类型
     * @param innerValueType 值类型
     * @return
     */
    public <O, I, V> Map<I, V> hGetMap(O outerKey, TypeReference<I> innerKeyType, TypeReference<V> innerValueType) {
        Map<byte[], byte[]> redisMap = redisTemplate.execute
                ((RedisCallback<Map<byte[], byte[]>>) connection -> connection.hGetAll(JSON.toJSONBytes(outerKey)));
        if (redisMap == null) return null;
        Map<I, V> resultMap = new HashMap<>(2);
        redisMap.forEach((key, value) -> resultMap.put(JSONObject.parseObject
                (new String(key), innerKeyType), JSONObject.parseObject(new String(value), innerValueType)));
        return resultMap;
    }

    /**
     * 查询Hash结构的值
     *
     * @param outerKey      外键
     * @param innerKey      内键
     * @param typeReference 值类型
     * @return
     */
    public <O, I, V> V hGet(O outerKey, I innerKey, TypeReference<V> typeReference) {
        byte[] redisResult = redisTemplate.execute((RedisCallback<byte[]>)
                connection -> connection.hGet(JSON.toJSONBytes(outerKey), JSON.toJSONBytes(innerKey)));
        if (redisResult == null) return null;
        return JSONObject.parseObject(new String(redisResult), typeReference);

    }

    /**
     * 删除键值对
     *
     * @param keys 键
     * @return
     */
    public <K> Long delMore(List<K> keys) {
        if (keys == null || keys.isEmpty()) return 0L;
        byte[][] keyBytes = new byte[keys.size()][];
        int index = 0;
        for (K key : keys) {
            keyBytes[index] = JSON.toJSONBytes(key);
            index++;
        }
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(keyBytes));
    }


    /**
     * 删除键值对
     *
     * @param key 键
     * @return
     */
    public <K> Long del(String key) {
        if (key == null || key.isEmpty()) return 0L;
        byte[] keyBytes = JSON.toJSONBytes(key);
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(keyBytes));
    }

    /**
     * 删除Hash结构内键和值
     *
     * @param outerKey  外键
     * @param innerKeys 内键
     * @return
     */
    public <O, I> Long hDel(O outerKey, List<I> innerKeys) {
        if (innerKeys == null || innerKeys.isEmpty()) return 0L;
        byte[][] innerKeyBytes = new byte[innerKeys.size()][];
        int index = 0;
        for (I key : innerKeys) {
            innerKeyBytes[index] = JSON.toJSONBytes(key);
            index++;
        }
        return redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.hDel(JSON.toJSONBytes(outerKey), innerKeyBytes));
    }
}
