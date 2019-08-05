package com.phenix.aop.helper;

import java.util.Hashtable;
import java.util.Map;

public class LRUCacheLinked<K, V> {

    // 缓存容量
    int capacity;
    // 缓存容器
    Hashtable<K, NodeCache> cache;
    // 头节点
    NodeCache first;
    // 尾节点
    NodeCache last;

    public LRUCacheLinked(int capacity) {
        this.capacity = capacity;
        this.cache = new Hashtable<K, NodeCache>(capacity);
    }

    public void put(K k, V v) {
        NodeCache curNode = cache.get(k);
        // 缓存未命中
        if (curNode == null) {
            // 超过容器容量
            if (cache.size() + 1 > capacity) {
                // 容器删除数据
                cache.remove(last.k);
                // 删除链表尾节点
                removeLast();
            }

            curNode = new NodeCache(k, v);
        }

        moveToHead(curNode);
        cache.put(k, curNode);
    }

    void removeLast() {
        if (last != null) {
            NodeCache temp = last.pre;
            // 尾节点的前一个节点next引用移除
            if (last.pre != null) {
                last.pre.next = null;
            }
            // 尾节点移除pre引用
            last.pre = null;
            // 尾指针指向前一个节点
            last = temp;
        }
    }

    /**
     * 把Node位移到头结点
     *
     * @param node
     */
    private void moveToHead(NodeCache node) {
        // node节点有可能是头节点，尾节点，中间节点三种情况，来处理指针删除
        // 情况1,node是头节点
        if (node == first)
            return;
        // 情况2,node的前一个节点的next引用指向node的后一个节点
        if (node.pre != null)
            node.pre.next = node.next;
        // node的后一个节点的pre引用指向node的前一个节点
        if (node.next != null)
            node.next.pre = node.pre;
        // 情况3,node为尾节点
        if (node == last)
            last = node.pre;

        // 位移到头节点
        // 第一次put时（即first==null）不需要处理Node的pre和next两个引用
        if (first != null) {
            node.next = first;
            first.pre = node;
        }
        first = node;
        node.pre = null;
        // 处理第一次put时候last也指向first
        if (last == null)
            last = first;
    }

    public V get(K k) {
        NodeCache nodeCache = cache.get(k);
        if (nodeCache != null) {
            return nodeCache.v;
        }
        return null;
    }

    void getAll() {
        for (Map.Entry<K, NodeCache> e : cache.entrySet()) {
            System.out.println(e.getValue());
        }
    }

    class NodeCache {
        K k;
        V v;
        NodeCache pre;
        NodeCache next;

        public NodeCache(K k, V v) {
            super();
            this.k = k;
            this.v = v;
        }

        @Override
        public String toString() {
            return "NodeCache [k=" + k + ", v=" + v + "]";
        }

    }

    public static void main(String[] args) {
        LRUCacheLinked<Integer, Integer> lru = new LRUCacheLinked<Integer, Integer>(
                10);
        for (int i = 0; i < 17; i++) {
            lru.put(i, i);
        }
        lru.getAll();
    }
}