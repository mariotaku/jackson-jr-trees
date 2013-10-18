package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.core.TreeNode;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;

public abstract class TestBase extends TestCase
{
    protected final JsonFactory _factory = new JsonFactory();
    protected String writeTree(TreeCodec treeCodec, TreeNode treeNode) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = _factory.createGenerator(writer);
        treeCodec.writeTree(jsonGenerator, treeNode);
        return writer.toString();
    }
}
