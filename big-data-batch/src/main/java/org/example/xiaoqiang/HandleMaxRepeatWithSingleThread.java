package org.example.xiaoqiang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HandleMaxRepeatWithSingleThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleMaxRepeatWithSingleThread.class);
    private static final String FILE_NAME = "D:\\temp\\User.dat";
    private static long[] statistics = new long[100];
    static {
        for (int i = 0; i < statistics.length; i++) {
            statistics[i] = 0;
        }
    }

    private static void process() throws IOException {
        long start = System.currentTimeMillis();
        int count = 1;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_NAME), StandardCharsets.UTF_8), 1024 * 1024 * 32)) {
            String line = null;
            // 按行读取
            while ((line = br.readLine()) != null) {
                processOneLine(line);
                if (count % 100 == 0) {
                    LOGGER.info("处理完:{}行，总耗时间：{} ms", count, (System.currentTimeMillis() - start));
                }
                count++;
            }
        }
        LOGGER.info("处理完成，共：{}行，总耗时间：{} ms", count, (System.currentTimeMillis() - start));
    }

    private static void processOneLine(String line) {
        int end = line.length();
        int value = 0;
        for (int i = 0; i < end; i++) {
            char c = line.charAt(i);
            if (line.charAt(i) != ',') {
                value = value * 10 + c - '0';
            } else {
                statistics[value]++;
                value = 0;
            }
        }
    }

    private static void printMax() {
        long max = 0;
        int maxIndex  = 0;
        for (int i = 0; i < statistics.length; i++) {
            if (statistics[i] > max) {
                max = statistics[i];
                maxIndex = i;
            }
        }
        System.out.println("数量最多的年龄为:" + maxIndex + "数量为：" + max);
    }

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("HandleMaxRepeatWithSingleThread");
        try {
            process();
            printMax();
        } catch (IOException e) {
            LOGGER.error("读取文件时发生异常。文件名：{}，详细：{}", FILE_NAME, e.getMessage(), e);
        }
        stopWatch.stop();
        System.out.println("任务总耗时：" + stopWatch.getTotalTimeMillis() + "ms");
    }
}
