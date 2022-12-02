package org.example.xiaoqiang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @Desc:
 * @Author: bingbing
 * @Date: 2022/5/4 0004 19:05
 */
public class GenerateDataNew {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateDataNew.class);
    private static Random random = new Random();
    private static final String FILE_NAME = "D:\\temp\\User.dat";
    private static final int AGE_START = 18;
    private static final int AGE_END = 70;

    private static final long MAX_COUNT = (long) Integer.MAX_VALUE * 17 / 100;

    private static final String[] STRING_VALUES = new String[100];

    static {
        for (int i = 0; i < STRING_VALUES.length; i++) {
            STRING_VALUES[i] = i + ",";
        }
    }

    public static String generateRandomData(int start, int end) {
        return STRING_VALUES[random.nextInt(end - start + 1) + start];
    }

    /**
     * 产生10G的 18-70的数据在D盘
     */
    public void generateData() throws IOException {
        File file = new File(FILE_NAME);
        long startTime = System.currentTimeMillis();
        try (BufferedWriter bos = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8),1024*1024*32)) {
            for (long i = 1; i < MAX_COUNT; i++) {
                String data = generateRandomData(AGE_START, AGE_END);
                bos.write(data);
                // 每100万条记录成一行，100万条数据大概4M
                if (i % 1_000_000 == 0) {
                    bos.write("\n");
                }
            }
            LOGGER.info("写入完成! 共花费时间：{} ms", (System.currentTimeMillis() - startTime));
        }
    }

    public static void main(String[] args) {
        GenerateDataNew generateData = new GenerateDataNew();
        try {
            generateData.generateData();
        } catch (IOException e) {
            LOGGER.error("生成文件时发生异常。文件名：{}，详细：{}", FILE_NAME, e.getMessage(), e);
        }
    }
}