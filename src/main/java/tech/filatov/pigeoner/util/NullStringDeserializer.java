package tech.filatov.pigeoner.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class NullStringDeserializer extends StdDeserializer<String> {

    public NullStringDeserializer() {
        this(null);
    }

    public NullStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String result = p.getValueAsString();
        return result.isEmpty() ? null : result;
    }
}
