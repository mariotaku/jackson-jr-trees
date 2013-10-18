package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.simple.ob.JSON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleWriteTest extends TestBase
{
    public void testSimpleList() throws Exception
    {
        List<TreeNode> stuff = new LinkedList<TreeNode>();
        stuff.add(new JsonString("x"));
        stuff.add(JsonBoolean.TRUE);
        stuff.add(new JsonNumber(123));
        assertEquals("[\"x\",true,123]", writeTree(new SimpleTreeCodec(), new JsonArray(stuff)));
    }

    public void testSimpleMap() throws Exception
    {
        Map<String,TreeNode> stuff = new LinkedHashMap<String,TreeNode>();
        stuff.put("a", new JsonNumber(15));
        stuff.put("b", JsonBoolean.TRUE);
        stuff.put("c", new JsonString("foobar"));
        
        assertEquals("{\"a\":15,\"b\":true,\"c\":\"foobar\"}",
                writeTree(new SimpleTreeCodec(), new JsonObject(stuff)));
    }

    public void testNest() throws Exception
    {
        Map<String,TreeNode> stuff = new LinkedHashMap<String,TreeNode>();
        List<TreeNode> list = new ArrayList<TreeNode>();
        list.add(new JsonNumber(123));
        list.add(new JsonNumber(456));
        stuff.put("first", new JsonArray(list));
        Map<String,TreeNode> second = new LinkedHashMap<String,TreeNode>();
        stuff.put("second", new JsonObject(second));
        second.put("foo", new JsonString("bar"));
        second.put("bar", new JsonArray());

        assertEquals("{\"first\":[123,456],\"second\":{\"foo\":\"bar\",\"bar\":[]}}",
                writeTree(new SimpleTreeCodec(), new JsonObject(stuff)));
    }
}
