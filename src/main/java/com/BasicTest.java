package com;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Calendar.LONG;

public class BasicTest {
    public static void main1(String[] args) {
        Map<String, Float> map = new HashMap<>(2);
        map.put("0", 0f);
        map.put("1", 1f);
        map.put("2", 2f);
        Float aFloat = map.get("1");
        System.out.println(aFloat);
        map.put("1", 11f);
        System.out.println(aFloat);
        System.out.println(map.get("1"));
    }

    public static void main2(String[] args) throws ParseException {
        String s1 = "dsaf";
        String s2 = null;
        System.out.println("case1: " + s1.equals(s2));
        System.out.println("case2: " + s2.equals(s1));
    }

    public static void main(String[] args) {
//        -9223372036854775808L
        System.out.println(Long.MIN_VALUE);
    }
}
