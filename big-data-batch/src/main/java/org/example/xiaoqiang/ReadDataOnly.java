package org.example.xiaoqiang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadDataOnly {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadDataOnly.class);
    private static final String FILE_NAME = "D:\\temp\\User.dat";

    private static void readData() throws IOException {
        long start = System.currentTimeMillis();
        int count = 1;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_NAME), StandardCharsets.UTF_8), 1024 * 1024 * 32)) {
            String line = null;
            while ((line = br.readLine()) != null) {
                // 按行读取
//            SplitData.splitLine(line);
                if (count % 100 == 0) {
                    LOGGER.info("读取{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - start));
                }
                count++;
            }
        }
        LOGGER.info("读取完成，共：{}行，总耗时间:{} ms", count, (System.currentTimeMillis() - start));
    }

    public static void main(String[] args) {
        try {
            readData();
        } catch (IOException e) {
            LOGGER.error("读取文件时发生异常。文件名：{}，详细：{}", FILE_NAME, e.getMessage(), e);
        }
    }
}
