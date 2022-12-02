package org.example.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CreateData {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateData.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int MAX_COUNT = 50;
    private static final String FILE_NAME = "D:\\temp\\Json\\Json03.dat";

    public static void main(String[] args) throws IOException {
        File file = new File(FILE_NAME);
        long startTime = System.currentTimeMillis();
        try (BufferedWriter bos = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
            for (long i = 0; i < MAX_COUNT; i++) {
                Bean bean = Bean.createWithRadom();
                String data = OBJECT_MAPPER.writeValueAsString(bean);
                bos.write(data);
                bos.newLine();
             }
            LOGGER.info("写入完成! 共花费时间：{} s", (System.currentTimeMillis() - startTime) / 1000);
        }
    }
}
