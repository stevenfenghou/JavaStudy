package org.example.bingbing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadDataOnly {
    private static final String FILE_NAME = "D:\\temp\\User.dat";

    private static void readData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "utf-8"));
        String line;
        long start = System.currentTimeMillis();
        int count = 1;
        while ((line = br.readLine()) != null) {
            // 按行读取
//            SplitData.splitLine(line);
            if (count % 100 == 0) {
                System.out.println("读取100行,总耗时间: " + (System.currentTimeMillis() - start) / 1000 + " s");
                System.gc();
            }
            count++;
        }
        br.close();
    }

    public static void main(String[] args) {
        try {
            ReadDataOnly.readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
