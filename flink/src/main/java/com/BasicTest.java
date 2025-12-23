package com;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    /**
     * 获取当前进程的pid
     *
     * @return
     */
    public static int getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        // format: "pid@hostname"
        String name = runtime.getName();
        return Integer.parseInt(name.substring(0, name.indexOf('@')));
    }

    public static void main(String[] args) throws Exception {
        while(true) {
            int pid = getPid();
            System.out.println(pid);
            TimeUnit.SECONDS.sleep(111);
        }
    }

}
