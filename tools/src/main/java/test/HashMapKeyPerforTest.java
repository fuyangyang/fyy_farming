package test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuyi on 17/4/14.
 */
public class HashMapKeyPerforTest {
    public static void main(String[] args) {
        Map<String, String> mapStr = new HashMap<String, String>();
        mapStr.put("1", "1");
        mapStr.get("1");

        Map<Integer, String> mapInt = new HashMap<Integer, String>();
        mapInt.put(1, "1");
        mapInt.get(1);


    }
}
