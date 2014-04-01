package net.xylophones.algo.collections;

import java.util.ArrayList;
import java.util.List;

public class EntryHashMap<K, V> {

    private final List<List<KeyValuePair<K,V>>> entries;

    public EntryHashMap(int numPartitions) {
        entries = new ArrayList<List<KeyValuePair<K,V>>>();
        for (int i=0 ; i<numPartitions ; i++) {
            entries.add(new ArrayList<KeyValuePair<K,V>>());
        }
    }

    public void put(K key, V value) {
        int bucketId = getBucketIdFromKey(key);
        int index = indexOf(key, entries.get(bucketId));

        KeyValuePair<K,V> entry = new KeyValuePair(key, value);
        if (index != -1) {
            entries.get(bucketId).remove(index);
        }
        entries.get(bucketId).add(entry);
    }

    public V get(K key) {
        int bucketId = getBucketIdFromKey(key);
        int index = indexOf(key, entries.get(bucketId));
        if (index != -1) {
            return entries.get(bucketId).get(index).value;
        } else {
            return null;
        }
    }

    public void remove(K key) {
        int bucketId = getBucketIdFromKey(key);
        int index = indexOf(key, entries.get(bucketId));
        if (index != -1) {
            entries.get(bucketId).remove(index);
        }
    }
    
    private int indexOf(K key, List<KeyValuePair<K, V>> entries) {
        for (int i=0 ; i<entries.size() ; i++) {
            if (entries.get(i).key.equals(key)) {
                return i;
            }
        }

        return -1;
    }

    private int getBucketIdFromKey(K key) {
        return key.hashCode() % entries.size();
    }

    static class KeyValuePair<K,V> {
        final K key;
        final V value;

        public KeyValuePair(K key, V value) {
            if (key == null) {
                throw new NullPointerException("key is null");
            }
            if (value == null) {
                throw new NullPointerException("value is null");
            }

            this.key = key;
            this.value = value;
        }
    }

}
