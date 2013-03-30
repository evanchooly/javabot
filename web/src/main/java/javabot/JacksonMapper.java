package javabot;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

public class JacksonMapper extends ObjectMapper {
    public JacksonMapper() {
        configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule("jackson", Version.unknownVersion());
        module.addSerializer(new ObjectIdSerializer());
        registerModule(module);
    }

    private static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public Class<ObjectId> handledType() {
            return ObjectId.class;
        }

        @Override
        public void serialize(ObjectId id, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonProcessingException {
            generator.writeString(id.toString());
        }
    }
}
