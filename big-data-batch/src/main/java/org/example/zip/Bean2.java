package org.example.zip;

import java.io.Serializable;

public class Bean2 implements Serializable {
    private long id;
    private int age;
    private int total;

    public Bean2() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
