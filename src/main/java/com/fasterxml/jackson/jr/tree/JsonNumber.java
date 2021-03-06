package com.fasterxml.jackson.jr.tree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;

public class JsonNumber extends JacksonJrValue.Scalar
{
    private static final Map<Class<? extends Number>, JsonParser.NumberType> NUMBER_TYPES;
    static
    {
        final Map<Class<? extends Number>, JsonParser.NumberType> numberTypes = new HashMap<Class<? extends Number>, JsonParser.NumberType>();

        numberTypes.put(Byte.class, JsonParser.NumberType.INT);
        numberTypes.put(Short.class, JsonParser.NumberType.INT);
        numberTypes.put(Integer.class, JsonParser.NumberType.INT);
        numberTypes.put(Long.class, JsonParser.NumberType.LONG);
        numberTypes.put(BigInteger.class, JsonParser.NumberType.BIG_INTEGER);
        numberTypes.put(Float.class, JsonParser.NumberType.FLOAT);
        numberTypes.put(Double.class, JsonParser.NumberType.DOUBLE);
        numberTypes.put(BigDecimal.class, JsonParser.NumberType.BIG_DECIMAL);

        NUMBER_TYPES = Collections.unmodifiableMap(numberTypes);
    }

    private final Number _value;
    private final JsonParser.NumberType _numberType;

    public JsonNumber(Number value)
    {
        _value = value;
        _numberType = NUMBER_TYPES.get(value.getClass());
        if (_numberType == null) {
            throw new IllegalArgumentException("Unsupported Number type: "+value.getClass().getName());
        }
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    public Number getValue() {
        return _value;
    }

    @Override
    public JsonToken asToken() {
        switch (numberType())
        {
            case BIG_DECIMAL:
            case DOUBLE:
            case FLOAT:
                return VALUE_NUMBER_FLOAT;
            default:
                return VALUE_NUMBER_INT;
        }
    }

    @Override
    public String asText() {
        return String.valueOf(_value);
    }
    
    @Override
    public JsonParser.NumberType numberType() {
        return _numberType;
    }

    /*
    /**********************************************************************
    /* Extended API
    /**********************************************************************
     */
    
    public BigInteger asBigInteger() throws IOException {
        if (_value instanceof BigInteger) {
            return (BigInteger) _value;
        }
        if (_value instanceof BigDecimal) {
            BigDecimal dec = (BigDecimal) _value;
            return dec.toBigInteger();
        }
        return BigInteger.valueOf(_value.longValue());
    }

    public BigDecimal asBigDecimal() throws IOException {
        if (_value instanceof BigDecimal) {
            return (BigDecimal) _value;
        }
        if (_value instanceof BigInteger) {
            return new BigDecimal((BigInteger) _value);
        }
        if ((_value instanceof Double) || (_value instanceof Float)) {
            return new BigDecimal(_value.doubleValue());
        }
        return new BigDecimal(_value.longValue());
    }
}
