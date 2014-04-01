package net.xylophones.algo.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple concurrent hash map
 */
public class LockStripedHashMap<K, V> {

    private static final int NOT_FOUND_INDEX = -1;
    private final List<List<KeyValuePair<K,V>>> entries;
    private final List<Lock> locks;

    public LockStripedHashMap(int numPartitions) {
        ArrayList<List<KeyValuePair<K,V>>> entries = new ArrayList<>();
        for (int i=0 ; i<numPartitions ; i++) {
            entries.add(new ArrayList<KeyValuePair<K,V>>());
        }
        this.entries = Collections.unmodifiableList(entries);

        ArrayList<Lock> locks = new ArrayList<>();
        for (int i=0 ; i<numPartitions ; i++) {
            locks.add(new ReentrantLock());
        }
        this.locks = Collections.unmodifiableList(locks);
    }

    public void put(K key, V value) {
        int bucketId = getBucketIdFromKey(key);
        
        lockBucket(bucketId);
        try {
            int index = indexOf(key, entries.get(bucketId));

            KeyValuePair<K,V> entry = new KeyValuePair(key, value);
            if (index != NOT_FOUND_INDEX) {
                entries.get(bucketId).remove(index);
            }
            entries.get(bucketId).add(entry);
        } finally {
            unlockBucket(bucketId);
        }
    }

    public V get(K key) {
        int bucketId = getBucketIdFromKey(key);

        lockBucket(bucketId);
        try {
            int index = indexOf(key, entries.get(bucketId));
            if (index != NOT_FOUND_INDEX) {
                return entries.get(bucketId).get(index).value;
            } else {
                return null;
            }
        } finally {
            unlockBucket(bucketId);
        }
    }

    public void remove(K key) {
        int bucketId = getBucketIdFromKey(key);

        lockBucket(bucketId);
        try {
            int index = indexOf(key, entries.get(bucketId));
            if (index != NOT_FOUND_INDEX) {
                entries.get(bucketId).remove(index);
            }
        } finally {
            unlockBucket(bucketId);
        }
    }
    
    private void unlockBucket(int bucketId) {
        locks.get(bucketId).unlock();
    }

    private void lockBucket(int bucketId) {
        locks.get(bucketId).lock();
    }

    private int indexOf(K key, List<KeyValuePair<K, V>> entries) {
        for (int i=0 ; i<entries.size() ; i++) {
            if (entries.get(i).key.equals(key)) {
                return i;
            }
        }

        return NOT_FOUND_INDEX;
    }

    private int getBucketIdFromKey(K key) {
        return key.hashCode() % entries.size();
    }

    private static class KeyValuePair<K,V> {
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
