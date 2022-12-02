package org.example.zip;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ReadZipFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadZipFile.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String FILE_NAME = "D:\\temp\\jsonData.zip";

    public static void main(String[] args) {

        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println(readZipFileName(FILE_NAME));
        readZipFile(FILE_NAME);
    }

    //读取zip文件内的文件,返回文件名称列表
    public static List<String> readZipFileName(String path){
        List<String> list = new ArrayList<>();
        try {
            ZipFile zipFile = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                list.add(entries.nextElement().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //读取zip文件内的文件,返回文件内容列表
    public static void readZipFile(String path){
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
                    System.out.println("file - " + ze.getName() + " : "+ ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), Charset.forName("utf-8")), 1024*1024*8);
                        //BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(zipFile.getInputStream(ze), 1024*1024*16), Charset.forName("utf-8")), 1024*1024*16);
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (Strings.isNotEmpty(line)) {
                                //Preservation.save(OBJECT_MAPPER.readValue(line, Bean.class));
                                Bean2 bean = OBJECT_MAPPER.readValue(line, Bean2.class);
                                totalSize += bean.getId();
                            }
                            count ++;
                            if (count % 100_000 == 0) {
                                LOGGER.info("读取{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
                                System.gc();
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
        System.out.println("size=：" + totalSize);;
    }
}

