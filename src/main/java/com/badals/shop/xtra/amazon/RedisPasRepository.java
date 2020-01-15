package com.badals.shop.xtra.amazon;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisPasRepository {

    private HashOperations hashOperations;

    private RedisTemplate redisTemplate;

    public RedisPasRepository(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public HashOperations getHashOperations() {
        return hashOperations;
    }
}
