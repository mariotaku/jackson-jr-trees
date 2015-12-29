package com.fasterxml.jackson.simple.tree;

import java.util.*;

public class SimpleWriteTest extends TestBase
{
    public void testSimpleList() throws Exception
    {
        List<JacksonJrValue> stuff = new LinkedList<JacksonJrValue>();
        stuff.add(new JsonString("x"));
        stuff.add(JsonBoolean.TRUE);
        stuff.add(new JsonNumber(123));
        assertEquals("[\"x\",true,123]", writeTree(new JacksonJrSimpleTreeCodec(), new JsonArray(stuff)));
    }

    public void testSimpleMap() throws Exception
    {
        Map<String,JacksonJrValue> stuff = new LinkedHashMap<String,JacksonJrValue>();
        stuff.put("a", new JsonNumber(15));
        stuff.put("b", JsonBoolean.TRUE);
        stuff.put("c", new JsonString("foobar"));

        assertEquals("{\"a\":15,\"b\":true,\"c\":\"foobar\"}",
                writeTree(new JacksonJrSimpleTreeCodec(), new JsonObject(stuff)));
    }

    public void testNest() throws Exception
    {
        Map<String,JacksonJrValue> stuff = new LinkedHashMap<String,JacksonJrValue>();
        List<JacksonJrValue> list = new ArrayList<JacksonJrValue>();
        list.add(new JsonNumber(123));
        list.add(new JsonNumber(456));
        stuff.put("first", new JsonArray(list));
        Map<String,JacksonJrValue> second = new LinkedHashMap<String,JacksonJrValue>();
        stuff.put("second", new JsonObject(second));
        second.put("foo", new JsonString("bar"));
        second.put("bar", new JsonArray());

        assertEquals("{\"first\":[123,456],\"second\":{\"foo\":\"bar\",\"bar\":[]}}",
                writeTree(new JacksonJrSimpleTreeCodec(), new JsonObject(stuff)));
    }
}
