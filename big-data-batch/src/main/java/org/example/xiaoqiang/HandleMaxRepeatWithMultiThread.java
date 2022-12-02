package org.example.xiaoqiang;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class HandleMaxRepeatWithMultiThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleMaxRepeatWithMultiThread.class);
    private static final int THREAD_COUNT = 4;
    private static final String FILE_NAME = "D:\\temp\\User.dat";
    private static LongAdder[] statistics = new LongAdder[100];

    static {
        for (int i = 0; i < statistics.length; i++) {
            statistics[i] = new LongAdder();
        }
    }
    private static void printMax() {
        long max = 0;
        int maxIndex = 0;
        for (int i = 0; i < statistics.length; i++) {
            if (statistics[i].longValue() > max) {
                max = statistics[i].longValue();
                maxIndex = i;
            }
        }
        System.out.println("数量最多的年龄为:" + maxIndex + "数量为：" + max);
    }

    private static class DataProcesser implements Runnable {
        private BlockingQueue<String> blockQueue;

        public DataProcesser(BlockingQueue<String> aBlockQueue) {
            this.blockQueue = aBlockQueue;
        }

        @Override
        public void run() {
            int count = 0;
            long start = System.currentTimeMillis();
            try {
                while (true) {
                    String line = blockQueue.take();
                    if ("@END".equals(line)) {
                        // 读到结束标志
                        break;
                    }
                    count++;
                    processOneLine(line);
                    if (count % 100 == 0) {
                        LOGGER.info("处理到{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - start));
                    }
                }
                LOGGER.info("处理结束，共处理{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void processOneLine(String line) {
            int end = line.length();
            int value = 0;
            for (int i = 0; i < end; i++) {
                char c = line.charAt(i);
                if (line.charAt(i) != ',') {
                    value = value * 10 + c - '0';
                } else {
                    statistics[value].increment();
                    value = 0;
                }
            }
        }
    }

    private static class DataReader implements Runnable {
        private BlockingQueue<String> blockQueue;
        private String fileName;

        public DataReader(BlockingQueue<String> aBlockQueue, String aFileName) {
            this.blockQueue = aBlockQueue;
            this.fileName = aFileName;
        }

        @Override
        public void run() {
            try {
                readData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void readData() throws IOException, InterruptedException {
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8),
                    1024 * 1024 * 8)) {
                String line;
                long start = System.currentTimeMillis();
                int count = 1;
                while ((line = br.readLine()) != null) {
                    // 按行读取
                    this.blockQueue.put(line);
                    if (count % 100 == 0) {
                        LOGGER.info("读取到{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - start));
                        System.gc();
                    }
                    count++;
                }
                LOGGER.info("读取完成，共：{}行，总耗时间:{} ms", count, (System.currentTimeMillis() - start));
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        BlockingQueue<String> blockQueue = new LinkedBlockingQueue<>(THREAD_COUNT);
        CompletableFuture.runAsync(new DataReader(blockQueue, FILE_NAME),  newReadThreadPool())
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOGGER.error("读取文件发生异常：{}", ex.getMessage(), ex);
                    }
                    for (int i = 0; i < THREAD_COUNT; i++) {
                        try {
                            blockQueue.put("@END");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    };
                });

        List<CompletableFuture<Void>> list = new ArrayList<>(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            CompletableFuture<Void> cp = CompletableFuture.runAsync(new DataProcesser(blockQueue),
                            newPorcessThreadPool())
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            LOGGER.error("读取文件发生异常：{}", ex.getMessage(), ex);
                        }
                    });
            list.add(cp);
        }

        CompletableFuture<Void> allFuture = CompletableFuture
                .allOf(list.toArray(new CompletableFuture[0]));
        allFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("读取文件发生异常：{}", ex.getMessage(), ex);
                return;
            }
            printMax();;
        });
    }

    private static ExecutorService newPorcessThreadPool() {
        return new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(THREAD_COUNT),
                new ThreadFactoryBuilder().setNamePrefix("processData-pool-").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    private static ExecutorService newReadThreadPool() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(16),
                new ThreadFactoryBuilder().setNamePrefix("readData-pool-").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
