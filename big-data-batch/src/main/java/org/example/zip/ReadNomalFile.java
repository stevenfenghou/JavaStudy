package org.example.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

public class ReadNomalFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadNomalFile.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String FOLDER_NAME = "D:\\temp\\jsonData";

    public static void main(String[] args) throws IOException {
        File file = new File(FOLDER_NAME);        //获取其file对象
        readZipFile(file);

    }

    //读取zip文件内的文件,返回文件内容列表
    public static void readZipFile(File file) throws IOException {
        long startTime = System.currentTimeMillis();
        long count = 0;
        long totalSize = 0;
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {
            if (f.isDirectory()) {
            } else {
                System.out.println("file - " + f.getName() + " : " + " bytes");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), Charset.forName("utf-8")), 1024 * 1024 * 16)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (Strings.isNotEmpty(line)) {
                            //Preservation.save(OBJECT_MAPPER.readValue(line, Bean.class));
//                            Bean bean = OBJECT_MAPPER.readValue(line, Bean.class);
//                            totalSize += bean.getId();
                        }
                        count++;
                        if (count % 10_000 == 0) {
                            LOGGER.info("读取{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
                            System.gc();
                        }
                    }
                }
            }
            //处理ddlList,此时ddlList为每个文件的内容,while每循环一次则读取一个文件

            //此处返回无用,懒得修改了

            LOGGER.info("读取完成，共{}行,总耗时间:{} ms", count, (System.currentTimeMillis() - startTime));
            System.out.println("Preservation保存数据的大小：" + Preservation.getSize());
            System.out.println("size=：" + totalSize);
        }
    }
}
