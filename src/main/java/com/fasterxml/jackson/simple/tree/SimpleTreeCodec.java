package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.core.TreeNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class SimpleTreeCodec extends TreeCodec {

    public static final SimpleTreeCodec SINGLETON = new SimpleTreeCodec();

    @Override
    public <T extends TreeNode> T readTree(JsonParser jsonParser) throws IOException {
        return nodeFrom(jsonParser);
    }

    private <T extends TreeNode> T nodeFrom(JsonParser jsonParser) throws IOException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == null) {
            currentToken = jsonParser.nextToken();
        }
        TreeNode node;
        if (currentToken == JsonToken.VALUE_TRUE || currentToken == JsonToken.VALUE_FALSE)
            node = (jsonParser.getValueAsBoolean() ? JsonBoolean.TRUE : JsonBoolean.FALSE);
        else if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT)
            node = new JsonNumber(jsonParser.getNumberValue());
        else if (currentToken == JsonToken.VALUE_STRING)
            node = new JsonString(jsonParser.getValueAsString());
        else if (currentToken == JsonToken.START_ARRAY) {
            List<TreeNode> values = new ArrayList<TreeNode>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                values.add(nodeFrom(jsonParser));
            }
            node = new JsonArray(values);
        } else if (currentToken == JsonToken.START_OBJECT) {
            Map<String, TreeNode> values = new HashMap<String, TreeNode>();
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                final String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                values.put(currentName, nodeFrom(jsonParser));
            }
            node = new JsonObject(values);
        } else if (currentToken == JsonToken.VALUE_NULL) {
            node = null;
        } else {
            throw new UnsupportedOperationException("Unsupported token " + currentToken);
        }

        //noinspection unchecked
        return (T) node;
    }

    @Override
    public void writeTree(JsonGenerator jsonGenerator, TreeNode treeNode) throws IOException {
        writeTreeInternal(jsonGenerator, treeNode);
    }

    private void writeTreeInternal(JsonGenerator jsonGenerator, final TreeNode treeNode) throws IOException {
        if (treeNode == null) {
            jsonGenerator.writeNull();
        } else if (treeNode instanceof JsonBoolean) {
            jsonGenerator.writeBoolean(treeNode == JsonBoolean.TRUE);
        } else if (treeNode instanceof JsonNumber) {
            switch (treeNode.numberType()) {
                case INT:
                    jsonGenerator.writeNumber(((JsonNumber) treeNode).getValue().intValue());
                    break;
                case LONG:
                    jsonGenerator.writeNumber(((JsonNumber) treeNode).getValue().longValue());
                    break;
                case BIG_INTEGER:
                    jsonGenerator.writeNumber(((BigInteger) ((JsonNumber) treeNode).getValue()));
                    break;
                case FLOAT:
                    jsonGenerator.writeNumber(((JsonNumber) treeNode).getValue().floatValue());
                    break;
                case DOUBLE:
                    jsonGenerator.writeNumber(((JsonNumber) treeNode).getValue().doubleValue());
                    break;
                case BIG_DECIMAL:
                    jsonGenerator.writeNumber(((BigDecimal) ((JsonNumber) treeNode).getValue()));
                    break;
            }
        } else if (treeNode instanceof JsonString) {
            jsonGenerator.writeString(((JsonString) treeNode).getValue());
        } else if (treeNode instanceof JsonArray) {
            jsonGenerator.writeStartArray();
            for (int i = 0; i < treeNode.size(); i++) {
                writeTreeInternal(jsonGenerator, treeNode.get(i));
            }
            jsonGenerator.writeEndArray();
        } else if (treeNode instanceof JsonObject) {
            Iterator<String> fieldNames = treeNode.fieldNames();
            jsonGenerator.writeStartObject();
            while (fieldNames.hasNext()) {
                final String fieldName = fieldNames.next();
                jsonGenerator.writeFieldName(fieldName);
                writeTreeInternal(jsonGenerator, treeNode.get(fieldName));
            }
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.flush();
    }

    @Override
    public TreeNode createArrayNode() {
        return new JsonArray();
    }

    @Override
    public TreeNode createObjectNode() {
        return new JsonObject();
    }

    @Override
    public JsonParser treeAsTokens(TreeNode treeNode) {
        final TokenBuffer buffer = new TokenBuffer(null, false);
        try {
            writeTree(buffer, treeNode);
        } catch (IOException e) {

        }
        return buffer.asParser();
    }

}
