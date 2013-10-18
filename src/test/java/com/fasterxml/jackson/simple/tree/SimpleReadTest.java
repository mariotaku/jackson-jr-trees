package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.core.TreeNode;

import java.io.StringWriter;

public class SimpleReadTest extends TestBase
{
    public void testSimpleList() throws Exception
    {
        final String INPUT = "[true,\"abc\"]";
        final TreeCodec treeCodec = new SimpleTreeCodec();
        TreeNode node = treeCodec.readTree(_factory.createParser(INPUT));
        assertTrue(node instanceof JsonArray);
        assertEquals(2, node.size());
        // actually, verify with write...
        final StringWriter writer = new StringWriter();
        final JsonGenerator jsonGenerator = _factory.createGenerator(writer);
        treeCodec.writeTree(jsonGenerator, node);
        assertEquals(INPUT, writer.toString());
    }

    public void testSimpleMap() throws Exception
    {
        final String INPUT = "{\"a\":1,\"b\":true,\"c\":3}";
        final TreeCodec treeCodec = new SimpleTreeCodec();
        TreeNode node = treeCodec.readTree(_factory.createParser(INPUT));
        assertTrue(node instanceof JsonObject);
        assertEquals(3, node.size());
        // actually, verify with write...
        final StringWriter writer = new StringWriter();
        final JsonGenerator jsonGenerator = _factory.createGenerator(writer);
        treeCodec.writeTree(jsonGenerator, node);
        assertEquals(INPUT, writer.toString());
    }

    public void testSimpleMixed() throws Exception
    {
        final String INPUT = "{\"a\":[1,2,{\"b\":true},3],\"c\":3}";
        final TreeCodec treeCodec = new SimpleTreeCodec();
        TreeNode node = treeCodec.readTree(_factory.createParser(INPUT));
        assertTrue(node instanceof JsonObject);
        assertEquals(2, node.size());
        TreeNode list = node.get("a");
        assertTrue(list instanceof JsonArray);

        // actually, verify with write...
        final StringWriter writer = new StringWriter();
        final JsonGenerator jsonGenerator = _factory.createGenerator(writer);
        treeCodec.writeTree(jsonGenerator, node);
        assertEquals(INPUT, writer.toString());
    }
}
