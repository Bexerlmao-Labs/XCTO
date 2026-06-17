package cc.bexerlmao.xcto.question.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 等价于 @JsonRawValue 的序列化行为：将 String 值作为 raw JSON 写入，
 * 不会再加一层引号转义。
 * <p>
 * 与 @JsonRawValue 的区别：只负责序列化，不影响反序列化，
 * 避免和 @JsonDeserialize 冲突导致 400。
 */
public class JsonRawStringSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeRawValue(value);
        }
    }
}
