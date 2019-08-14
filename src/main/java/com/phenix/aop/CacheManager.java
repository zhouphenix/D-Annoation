package com.phenix.aop;


import com.phenix.aop.helper.LRUCache;

public class CacheManager {
    private final static int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;

    private static class Holder {
        public static final CacheManager INSTANCE = new CacheManager();

    }

    public static CacheManager getInstance() {
        return Holder.INSTANCE;
    }

    private CacheManager() {

    }

    static LRUCache<String, Object> mMemoryCache = new LRUCache<String, Object>(cacheSize);

    public void add(String key, Object mObject) {
        mMemoryCache.put(key, mObject);
    }

    public Object get(String key) {
        return mMemoryCache.get(key);
    }
}
