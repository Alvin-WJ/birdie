package com.talkingdata.birdie.test.flink.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * Created by xiaoqiang on 2017/5/20.
 */
public class ExtractOperatorTest {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("source_data.json");
        JsonNode src_obj = objectMapper.readTree(inputStream);
        JsonNode dest_obj = new ExtractOperator0().extract(src_obj);
        System.out.println(dest_obj.toString());
    }
}
