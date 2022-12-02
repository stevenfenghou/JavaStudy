package org.example.zip;

import org.junit.Test;

import static org.junit.Assert.*;

public class BeanTest {

    @Test
    public void createWithRadom() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Bean.createWithRadom());
        }
    }
}