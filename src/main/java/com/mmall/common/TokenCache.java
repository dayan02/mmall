package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * Created by 大燕 on 2018/11/18.
 */

public class TokenCache {
    //日志声明
    public  static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public  static final String TOKEN_PREFIX = "token_";
    //静态内存块
    //当超过内存存储最大值的时候，就会采用LRU算法清除，及最小使用算法清楚，有效期是12，单位是小时
    public  static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                //默认的数据加载实现，如果没有对应的key值的话，默认调用这个方法进行实现
                public String load(String s) throws Exception {
                    return "null";
                }
            });


    public  static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String  getKey(String key){
        String value = null;
try{
    value = localCache.get(key);
        if ("null".equals(value)){
        return null;
        }
return  value;
}catch (Exception e){
logger.error("logger error is",e);
}
        return  null;
    }
}
