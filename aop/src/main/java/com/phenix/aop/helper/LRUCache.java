package com.phenix.aop.helper;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {

    int initSize;
    LinkedHashMap<K, V> cache;

    public LRUCache(int size) {
        this.initSize = size;
        this.cache = new LinkedHashMap<K, V>(size, 0.75f, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.initSize;
            }
        };
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V put(K key, V value) {
        return cache.put(key, value);
    }

    public void clear(K key, V value) {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public void getAll() {
        for (Map.Entry<K, V> e : cache.entrySet()) {
            System.out.println(e.getKey() + ";" + e.getValue());
        }
    }

    /**
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {
        LRUCache<Integer, Integer> lruCache = new LRUCache<Integer, Integer>(9);
        for (int i = 1; i <= 20; i++) {
            lruCache.put(i, i);
        }
        lruCache.getAll();
    }
}