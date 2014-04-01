package net.xylophones.algo.collections;

import java.util.ArrayList;
import java.util.List;

public class SimpleHashMap<K, V> {

    private final int numPartitions;

    private final List<List<K>> keys;

    private final List<List<V>> values;

    public SimpleHashMap(int numPartitions) {
        this.numPartitions = numPartitions;

        keys = new ArrayList<List<K>>();
        for (int i=0 ; i<numPartitions ; i++) {
            keys.add(new ArrayList<K>());
        }
        
        values = new ArrayList<List<V>>();
        for (int i=0 ; i<numPartitions ; i++) {
            values.add(new ArrayList<V>());
        }
    }

    public void put(K key, V value) {
        int bucketId = getBucketIdFromKey(key);
        int index = keys.get(bucketId).indexOf(key);
        if (index != -1) {
            keys.get(bucketId).remove(index);
            keys.get(bucketId).add(key);

            values.get(bucketId).remove(index);
            values.get(bucketId).add(value);
        } else {
            keys.get(bucketId).add(key);
            values.get(bucketId).add(value);
        }
    }

    public V get(K key) {
        int bucketId = getBucketIdFromKey(key);
        int index = keys.get(bucketId).indexOf(key);
        if (index != -1) {
            return values.get(bucketId).get(index);
        } else {
            return null;
        }
    }

    public void remove(K key) {
        int bucketId = getBucketIdFromKey(key);
        int index = keys.get(bucketId).indexOf(key);
        if (index != -1) {
            keys.get(bucketId).remove(index);
            values.get(bucketId).remove(index);
        }
    }
    
    private int getBucketIdFromKey(K key) {
        return key.hashCode() % numPartitions;
    }
}
