package com;

public class tttt {
    public static void main(String[] args) {
        Float f1 = 2.32f;
        Float f2 = 22.32f;
        Double d1 = 23.32d;
        Double d2 = 2.32d;
        long s = System.currentTimeMillis();
        System.out.println("start float 100 times:" + s);
        for (int i = 0; i < 1000000; i++) {
            int i1 = f1.compareTo(f2 + i);
        }
        System.out.println("stop  float 100 times:" + (System.currentTimeMillis() -s ));

        long startdoul = System.currentTimeMillis();
        System.out.println("start double 100 times:" + startdoul);
        for (int i = 0; i < 1000000; i++) {
            int i1 = d2.compareTo(d1 + i);
        }
        System.out.println("stop  double 100 times:" + (System.currentTimeMillis() - startdoul));
    }
}
