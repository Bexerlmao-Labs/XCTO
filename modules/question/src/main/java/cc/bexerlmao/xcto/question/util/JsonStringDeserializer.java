package cc.bexerlmao.xcto.question.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonStringDeserializer extends JsonDeserializer<String> {

    private static final Logger log = LoggerFactory.getLogger(JsonStringDeserializer.class);

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 处理 null JSON 值（Jackson 通常跳过自定义反序列化器直接返回 null，
        // 但如果被调用，这里做防御性处理）
        JsonToken token = p.currentToken();
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }

        try {
            JsonNode node = p.readValueAsTree();
            if (node == null || node.isNull()) {
                return null;
            }
            return node.toString();
        } catch (IOException e) {
            log.error("JsonStringDeserializer failed to deserialize value at {}: {}",
                    p.getCurrentLocation(), e.getMessage());
            throw e;
        }
    }
}
