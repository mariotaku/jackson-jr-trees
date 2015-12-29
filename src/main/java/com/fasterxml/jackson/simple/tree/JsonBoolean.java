package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonToken;

public class JsonBoolean extends JacksonJrValue.Scalar
{
    public static JsonBoolean TRUE = new JsonBoolean(JsonToken.VALUE_TRUE);
    public static JsonBoolean FALSE = new JsonBoolean(JsonToken.VALUE_FALSE);

    private final JsonToken _token;
    
    private JsonBoolean(JsonToken t) {
        _token = t;
    }

    @Override
    public JsonToken asToken() {
        return _token;
    }
}
