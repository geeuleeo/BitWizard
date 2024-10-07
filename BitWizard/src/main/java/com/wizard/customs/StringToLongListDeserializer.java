package com.wizard.customs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StringToLongListDeserializer extends JsonDeserializer<List<Long>> {

    @Override
    public List<Long> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return Arrays.stream(value.split(","))
                     .map(Long::parseLong)
                     .collect(Collectors.toList());
    }
}

