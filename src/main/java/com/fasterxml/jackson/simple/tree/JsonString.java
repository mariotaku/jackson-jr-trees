package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonToken;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

public class JsonString extends JacksonJrValue.Scalar
{
    private final String value;

    public JsonString(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    @Override
    public JsonToken asToken() {
        return VALUE_STRING;
    }

    @Override
    public String asText() {
        return value;
    }
}
