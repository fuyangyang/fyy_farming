package algo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LRUCache {

    private int capacity;
    private List<String> list;
    private Map<String, Integer> map;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.list = new LinkedList<>();
        map = new HashMap<>();
    }


    /**
     *
     * @param key
     * @param value equals key
     */
    public void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key required");
        }
        if (map.containsKey(key)) {
            Integer index = map.get(key);
            swap(index.intValue());
            return;
        }

        if (map.size() < capacity) {
            map.put(key, 0);
            list.add(0, key);
        } else {
            list.set(list.size() - 1, key);
            map.put(key, list.size() - 1);
        }

    }

    private String get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key required");
        }
        if (map.containsKey(key)) {
            Integer integer = map.get(key);
            swap(integer);
            return key;
        }

        return null;
    }

    private void swap(int index) {
        String valueToHead = list.remove(index);
        list.add(0, valueToHead);
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3);
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("2", "2");
        cache.put("0", "0");
        cache.put("1", "1");

        System.out.println(cache.get("0"));
        System.out.println(cache.get("1"));
        System.out.println(cache.get("2"));
        System.out.println(cache.get("3"));
    }
}
