package com.thedripcostore.demo.components;

import java.security.SecureRandom;

public class ProductIdGenerator {
    private static final SecureRandom rnd = new SecureRandom();
    private static final String PREFIX = "Drip_co_";
    public static String generate() {
        long ts = System.currentTimeMillis();
        // 10 digits from random + timestamp fragment
        int rand = rnd.nextInt(9000) + 1000;
        return PREFIX + ts + String.format("%04d", rand);
    }
}