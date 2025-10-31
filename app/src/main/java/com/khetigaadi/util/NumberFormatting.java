package com.khetigaadi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

public class NumberFormatting {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static final HashMap<Integer, Integer> prices = new HashMap<>();

    static {
        suffixes.put(1_000L, "K");
        suffixes.put(1_00_000L, "L");
        suffixes.put(1_00_00_000L, "Cr");
        prices.put(0, 10000);
        prices.put(1, 20000);
        prices.put(2, 30000);
        prices.put(3, 40000);
        prices.put(4, 50000);
        prices.put(5, 60000);
        prices.put(6, 70000);
        prices.put(7, 80000);
        prices.put(8, 90000);
        prices.put(9, 100000);
        prices.put(10, 150000);
        prices.put(11, 200000);
        prices.put(12, 250000);
        prices.put(13, 300000);
        prices.put(14, 350000);
        prices.put(15, 400000);
        prices.put(16, 450000);
        prices.put(17, 500000);
        prices.put(18, 550000);
        prices.put(19, 600000);
        prices.put(20, 650000);
        prices.put(21, 700000);
        prices.put(22, 750000);
        prices.put(23, 800000);
        prices.put(24, 850000);
        prices.put(25, 900000);
        prices.put(26, 950000);
        prices.put(27, 1000000);
        prices.put(28, 1050000);
        prices.put(29, 1100000);
        prices.put(30, 1150000);
        prices.put(31, 1200000);
        prices.put(32, 1250000);
        prices.put(33, 1300000);
        prices.put(34, 1350000);
        prices.put(35, 1400000);
        prices.put(36, 1450000);
        prices.put(37, 1500000);
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String format(String value) {
        try {
            return format(Integer.parseInt(value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String getShortPriceFromIndex(int index) {
        return format(getPriceFormIndex(index));
    }

    public static int getPriceFormIndex(int index) {
        try {
            return prices.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Optional<Integer> getIndexFromPrice(int price) {
        return prices.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), price))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
