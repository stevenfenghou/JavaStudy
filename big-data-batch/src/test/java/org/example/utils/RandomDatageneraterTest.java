package org.example.utils;

import static org.junit.Assert.*;

public class RandomDatageneraterTest {

    @org.junit.Test
    public void randomInt() {
    }

    @org.junit.Test
    public void randomAsciiString() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomDatagenerater.randomAsciiString(2,3));
        }

    }

    @org.junit.Test
    public void randomChineseCharString() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomDatagenerater.randomChineseCharString(2,3));
        }
    }
}