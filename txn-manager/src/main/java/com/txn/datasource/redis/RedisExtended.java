package com.txn.datasource.redis;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisExtended<K, V> extends RedisTemplate<K, V>{
    
    public void destroyAndCloseConnection(){
        this.getConnectionFactory().getConnection().close();
    }
    
}
