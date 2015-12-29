package com.fasterxml.jackson.simple.tree;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.fasterxml.jackson.core.*;

public class JacksonJrSimpleTreeCodec extends TreeCodec
{
    public static JsonMissing MISSING = JsonMissing.instance;

    public static final JacksonJrSimpleTreeCodec SINGLETON = new JacksonJrSimpleTreeCodec();

    protected ObjectCodec _objectCodec;

    public JacksonJrSimpleTreeCodec() {
        this(null);
    }

    public JacksonJrSimpleTreeCodec(ObjectCodec codec) {
        _objectCodec = codec;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends TreeNode> T readTree(JsonParser p) throws IOException {
        return (T) nodeFrom(p);
    }

    private JacksonJrValue nodeFrom(JsonParser p) throws IOException {
        JsonToken currentToken = p.getCurrentToken();
        if (currentToken == null) {
            currentToken = p.nextToken();
        }
        JacksonJrValue node;
        if (currentToken == JsonToken.VALUE_TRUE || currentToken == JsonToken.VALUE_FALSE)
            node = (p.getValueAsBoolean() ? JsonBoolean.TRUE : JsonBoolean.FALSE);
        else if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT)
            node = new JsonNumber(p.getNumberValue());
        else if (currentToken == JsonToken.VALUE_STRING)
            node = new JsonString(p.getValueAsString());
        else if (currentToken == JsonToken.START_ARRAY) {
            List<JacksonJrValue> values = _list();
            while (p.nextToken() != JsonToken.END_ARRAY) {
                values.add(nodeFrom(p));
            }
            node = new JsonArray(values);
        } else if (currentToken == JsonToken.START_OBJECT) {
            Map<String, JacksonJrValue> values = _map();
            while (p.nextToken() != JsonToken.END_OBJECT) {
                final String currentName = p.getCurrentName();
                p.nextToken();
                values.put(currentName, nodeFrom(p));
            }
            node = new JsonObject(values);
        } else if (currentToken == JsonToken.VALUE_NULL) {
            node = null;
        } else {
            throw new UnsupportedOperationException("Unsupported token " + currentToken);
        }
        return node;
    }

    @Override
    public void writeTree(JsonGenerator g, TreeNode treeNode) throws IOException {
        writeTreeInternal(g, treeNode);
    }

    private void writeTreeInternal(JsonGenerator g, final TreeNode treeNode) throws IOException {
        if (treeNode == null) {
            g.writeNull();
        } else if (treeNode instanceof JsonBoolean) {
            g.writeBoolean(treeNode == JsonBoolean.TRUE);
        } else if (treeNode instanceof JsonNumber) {
            switch (treeNode.numberType()) {
                case INT:
                    g.writeNumber(((JsonNumber) treeNode).getValue().intValue());
                    break;
                case LONG:
                    g.writeNumber(((JsonNumber) treeNode).getValue().longValue());
                    break;
                case BIG_INTEGER:
                    g.writeNumber(((BigInteger) ((JsonNumber) treeNode).getValue()));
                    break;
                case FLOAT:
                    g.writeNumber(((JsonNumber) treeNode).getValue().floatValue());
                    break;
                case DOUBLE:
                    g.writeNumber(((JsonNumber) treeNode).getValue().doubleValue());
                    break;
                case BIG_DECIMAL:
                    g.writeNumber(((BigDecimal) ((JsonNumber) treeNode).getValue()));
                    break;
            }
        } else if (treeNode instanceof JsonString) {
            g.writeString(((JsonString) treeNode).getValue());
        } else if (treeNode instanceof JsonArray) {
            g.writeStartArray();
            for (int i = 0; i < treeNode.size(); i++) {
                writeTreeInternal(g, treeNode.get(i));
            }
            g.writeEndArray();
        } else if (treeNode instanceof JsonObject) {
            Iterator<String> fieldNames = treeNode.fieldNames();
            g.writeStartObject();
            while (fieldNames.hasNext()) {
                final String fieldName = fieldNames.next();
                g.writeFieldName(fieldName);
                writeTreeInternal(g, treeNode.get(fieldName));
            }
            g.writeEndObject();
        }
        g.flush();
    }

    @Override
    public TreeNode createArrayNode() {
        return new JsonArray(_list());
    }

    @Override
    public TreeNode createObjectNode() {
        return new JsonObject(_map());
    }

    @Override
    public JsonParser treeAsTokens(TreeNode node) {
        return ((JacksonJrValue) node).traverse(_objectCodec);
    }

    /*
    /**********************************************************************
    /* Extended API
    /**********************************************************************
     */

    public TreeNode missingNode() {
        return JsonMissing.instance;
    }

    /*
    /**********************************************************************
    /* Internal methods
    /**********************************************************************
     */
    
    protected List<JacksonJrValue> _list() {
        return new ArrayList<JacksonJrValue>();
    }

    protected Map<String,JacksonJrValue> _map() {
        return new LinkedHashMap<String,JacksonJrValue>();
    }
}
