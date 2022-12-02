package org.example.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonBench {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonBench.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        Bean bean = Bean.createWithRadom();
        String data = OBJECT_MAPPER.writeValueAsString(bean);

        long totalSize = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2_000_000; i++) {
            if (Strings.isNotEmpty(data)) {
                Bean bean1 = OBJECT_MAPPER.readValue(data, Bean.class);
                totalSize += bean1.getAge() + bean1.getTerminalNo();
            }
        }
        LOGGER.info("读取完成,总耗时间:{} ms",  (System.currentTimeMillis() - startTime));
        System.out.println("size=：" + totalSize);;
    }
}
