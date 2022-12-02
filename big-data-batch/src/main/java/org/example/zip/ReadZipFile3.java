package org.example.zip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ReadZipFile3 {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadZipFile3.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String FILE_NAME = "D:\\temp\\jsonData.zip";
    private static final Executor excuter = Executors.newFixedThreadPool(10);
    private static BlockingQueue<BufferedReader> readQueue = new LinkedBlockingQueue<>(5);
    private static BlockingQueue<BufferedReader> closeQueue = new LinkedBlockingQueue<>(10);

    private static final AtomicLong count = new AtomicLong(0);
    private static final AtomicLong totalSize = new AtomicLong(0);
    private static long startTime = System.currentTimeMillis();
    private static Bean bean = Bean.createWithRadom();
    private static String data;

    static {
        try {
            data = OBJECT_MAPPER.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        excuter.execute(() -> {
            while (true) {
                try {
                    BufferedReader br = null;
                    br = readQueue.take();
                    String line;
                    while ((line = br.readLine()) != null) {
                            if (Strings.isNotEmpty(line)) {
                                //Preservation.save(OBJECT_MAPPER.readValue(line, Bean.class));
                                //Bean bean = OBJECT_MAPPER.readValue(data, Bean.class);
                                int index = line.indexOf("\"age\":");
                                index = index + 6;
                                int value = 0;
                                char c = line.charAt(index);
                                while (c >= '0' && c <= '9' ) {
                                    value = value * 10 + (c - '0');
                                    index++;
                                    c = line.charAt(index);
                                }
                                totalSize.addAndGet(value);
                            }
                        count.incrementAndGet();
                        if (count.get() % 100_000 == 0) {
                            LOGGER.info("读取{}行,总耗时间:{} ms", count.get(), (System.currentTimeMillis() - startTime));
                            System.gc();
                        }
                    }
                    closeQueue.put(br);


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        excuter.execute(() -> {
            try {
                while (true) {
                    BufferedReader br = closeQueue.take();
                    br.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        readZipFile(FILE_NAME);
    }

    //读取zip文件内的文件,返回文件内容列表
    public static void readZipFile(String path) {
        long startTime = System.currentTimeMillis();
        long count = 0;
        long totalSize = 0;
        try {
            ZipFile zipFile = new ZipFile(path);
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
                    System.out.println("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), Charset.forName("utf-8")), 1024 * 1024 * 32);
                        readQueue.put(br);
                    }
                }
                //处理ddlList,此时ddlList为每个文件的内容,while每循环一次则读取一个文件
            }
            //zin.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //此处返回无用,懒得修改了

        LOGGER.info("读取完成，共{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
        System.out.println("Preservation保存数据的大小：" + Preservation.getSize());
        ;
        System.out.println("size=：" + totalSize);
        ;
    }
}

