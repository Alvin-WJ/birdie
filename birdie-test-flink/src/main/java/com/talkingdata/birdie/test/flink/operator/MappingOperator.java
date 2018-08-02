package com.talkingdata.birdie.test.flink.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jian.wang on 2017/5/20.
 */
public class MappingOperator extends RichFlatMapFunction<JsonNode, JsonNode> implements Operatorable {

    private static Map<String, String> map = createMapping();
    private static String srcField = "app.platformid";
    private static String distField = "app.platformid";

    private static Map<String, String> createMapping() {
        List<KV> kvList = Arrays.asList(new KV("1", "Android"), new KV("2", "IOS"));
        return KV.toMap(kvList);
    }

    @Override
    public void flatMap(JsonNode value, Collector<JsonNode> out) throws Exception {
        if (value == null)
            return;
        out.collect(mapping(value));
    }

    public JsonNode mapping(JsonNode value) {
        JsonNode srcNode = value;

        try {
            String[] srcPath = srcField.split("\\.");
            String[] distPath = distField.split("\\.");
            for (int i=0; i < srcPath.length; i++) {
                srcNode = srcNode.get(srcPath[i]);
            }

            String distValue = map.get(srcNode.asText()) ;
            if (distValue == null)
                distValue = "default";

            JsonNode distNode = value;
            for (int i=0; i < distPath.length-1; i++) {
                distNode = distNode.get(distPath[i]);
            }
            ((ObjectNode)distNode).put(distPath[distPath.length-1], distValue);
        } catch (Exception e) {

        }
        return value;
    }

    private static class KV {
        public String key;
        public String value;

        public KV(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static Map<String, String> toMap(List<KV> kvList) {
            Map<String, String> map = new HashMap<>();
            for (KV kv : kvList) {
                map.put(kv.key, kv.value);
            }
            return map;
        }
    }
}
