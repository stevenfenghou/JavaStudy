package org.example.zip;

import java.util.LinkedList;
import java.util.List;

public class Preservation {
    private static List<Bean> beanList = new LinkedList<>();

    public static void save(Bean bean) {
        beanList.add(bean);
    }

    public static int getSize() {
        return beanList.size();
    }
}
