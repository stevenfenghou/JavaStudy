package org.example.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ReadZipFileWithQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadZipFileWithQueue.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static BlockingQueue<String> blockQueue = new LinkedBlockingQueue<>(100000);
    private static final String FILE_NAME = "D:\\temp\\jsonData.zip";

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                readZipFile(FILE_NAME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                proccess();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void proccess() throws InterruptedException, IOException {
        int count = 0;
        long totalSize = 0;
        long startTime = System.currentTimeMillis();

        Bean bean = Bean.createWithRadom();
        String data = OBJECT_MAPPER.writeValueAsString(bean);
        while (true) {
            String line = blockQueue.poll(50, TimeUnit.SECONDS);
            if (Strings.isNotEmpty(line)) {
                Bean bean1 = OBJECT_MAPPER.readValue(line, Bean.class);
                totalSize += bean1.getId();

//                Bean bean1 = OBJECT_MAPPER.readValue(data, Bean.class);
//                totalSize += bean1.getAge() + bean1.getTerminalNo();
                count++;
                if (count % 10_000 == 0) {
                    LOGGER.info("处理{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
                    System.gc();
                }
            }
            if (line == null) {
                System.out.println("处理结束!");
                break;
            }
        }
        System.out.println("size=：" + totalSize);;
    }


    //读取zip文件内的文件,返回文件内容列表
    public static void readZipFile(String path) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long count = 0;
        try {
            ZipFile zipFile = new ZipFile(path);
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
                    System.out.println("file - " + ze.getName() + " : "+ ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), Charset.forName("utf-8")));
                        //BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(zipFile.getInputStream(ze), 1024*1024*16), Charset.forName("utf-8")), 1024*1024*16);
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (Strings.isNotEmpty(line)) {
                                blockQueue.put(line);
                            }
                            count ++;
                            if (count % 10_000 == 0) {
                                LOGGER.info("读取{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
                            }
                        }
                        br.close();
                    }
                }
                //处理ddlList,此时ddlList为每个文件的内容,while每循环一次则读取一个文件
            }
            zin.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //此处返回无用,懒得修改了

        LOGGER.info("读取完成，共{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
        System.out.println("Preservation保存数据的大小：" + Preservation.getSize());;
    }
}

