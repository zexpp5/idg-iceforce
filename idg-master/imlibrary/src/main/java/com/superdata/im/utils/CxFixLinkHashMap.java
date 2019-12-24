package com.superdata.im.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-15
 * @desc
 */
public class CxFixLinkHashMap<K, V>{
    private LinkedHashMap<K,V> linkedHashMap;

    private int maxSize;

    public CxFixLinkHashMap(int maxSize){
        linkedHashMap = new LinkedHashMap<>();
        this.maxSize = maxSize;
    }

    public void remove(K key){
        linkedHashMap.remove(key);
    }

    public void put(K key, V value) {
        if(linkedHashMap.containsKey(key)){
            return ;
        }
        if(linkedHashMap.size() > maxSize){
            linkedHashMap.remove(0);
            linkedHashMap.put(key, value);
        }
        linkedHashMap.put(key, value);
    }

    public boolean containsKey(K k){
        return linkedHashMap.containsKey(k);
    }

    public void putAll(Map map) throws Exception {
        if(linkedHashMap.size() + map.size() > maxSize){
            throw new Exception("size beyond "+maxSize);
        }
        linkedHashMap.putAll(map);
    }

    public V get(K k){
        return linkedHashMap.get(k);
    }

    public Set<Map.Entry<K, V>> entrySet(){
        return linkedHashMap.entrySet();
    }

    public int size(){
        return linkedHashMap.size();
    }

    /**
     * 是否小于最大缓存数
     * @return
     */
    public boolean isLtMaxSize(){
        if(linkedHashMap.size() < maxSize){
            return true;
        }else {
            return false;
        }
    }

    public boolean isEmpty(){
        return linkedHashMap.isEmpty();
    }

}
