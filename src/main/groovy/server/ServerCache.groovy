package server

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder

import java.util.concurrent.TimeUnit

class ServerCache {

    private final Cache<Object, Object> context

    ServerCache() {
        this.context = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(6, TimeUnit.SECONDS)
                .build() as Cache<Object, Object>
    }

    void put(String ipClient, value) {
        context.put(ipClient, value)
    }

    Integer get(String ipClient) {
        Object value = context.getIfPresent(ipClient)

        return value ?
                value as Integer :
                0
    }
}