package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static Map<String, AtomicInteger> countMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        countMap.put("a", new AtomicInteger(61));
        countMap.put("b", new AtomicInteger(11));
        countMap.put("c", new AtomicInteger(11));
        countMap.put("d", new AtomicInteger(91));

        findMostAge2();
        findMostAge3();
    }


    private static void findMostAge2() {
Integer targetValue = 0;
String targetKey = null;
for (Map.Entry<String, AtomicInteger> entry:countMap.entrySet() ) {
    if (entry.getValue().get() > targetValue) {
        targetValue = entry.getValue().get();
        targetKey = entry.getKey();
    }
}
        System.out.println("数量最多的年龄为:" + targetKey + "数量为：" + targetValue);
    }

    private static void findMostAge3() {
Map.Entry<String, AtomicInteger> entry = countMap.entrySet().stream().max(
        (e1 ,e2) -> e1.getValue().get() - e2.getValue().get()).orElse(null);
        System.out.println("数量最多的年龄为:" + entry.getKey() + "数量为：" + entry.getValue());
    }
}