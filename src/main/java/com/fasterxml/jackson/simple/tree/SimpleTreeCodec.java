package com.fasterxml.jackson.simple.tree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.simple.ob.JSON;

import java.io.IOException;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleTreeCodec extends TreeCodec {

    @Override
    public <T extends TreeNode> T readTree(JsonParser jsonParser) throws IOException {
        return nodeFrom(JSON.std.from(jsonParser));
    }

    private <T extends TreeNode> T nodeFrom(Object value)
    {
        TreeNode node = null;

        if (value instanceof Boolean)
            node = ((Boolean) value ? JsonBoolean.TRUE : JsonBoolean.FALSE);
        else if (value instanceof Number)
            node = new JsonNumber(((Number) value));
        else if (value instanceof String)
            node = new JsonString((String) value);
        else if (value instanceof List) {
            List<TreeNode> values = new ArrayList<TreeNode>();
            for (Object el : (List) value) {
                values.add(nodeFrom(el));
            }
            node = new JsonArray(values);
        }
        else if (value instanceof Object[]) {
            List<TreeNode> values = new ArrayList<TreeNode>();
            for (Object el : (Object[]) value) {
                values.add(nodeFrom(el));
            }
            node = new JsonArray(values);
        }
        else if (value instanceof Map) {
            Map<String,TreeNode> values = new LinkedHashMap<String,TreeNode>();
            for (Map.Entry entry : ((Map<?,?>) value).entrySet()) {
                values.put(entry.getKey().toString(), nodeFrom(entry.getValue()));
            }
            node = new JsonObject(values);
        }

        return (T) node;
    }

    @Override
    public void writeTree(JsonGenerator jsonGenerator, TreeNode treeNode) throws IOException {
        JSON.std.write(valueOf(treeNode), jsonGenerator);
    }

    private Object valueOf(final TreeNode treeNode)
    {
        if (treeNode == null)
            return null;
        else if (treeNode instanceof JsonBoolean)
            return treeNode == JsonBoolean.TRUE;
        else if (treeNode instanceof JsonNumber)
            return ((JsonNumber) treeNode).getValue();
        else if (treeNode instanceof JsonString)
            return ((JsonString) treeNode).getValue();
        else if (treeNode instanceof JsonArray) {
            return new AbstractList<Object>() {
                @Override
                public Object get(int index) {
                    return valueOf(treeNode.get(index));
                }

                @Override
                public int size() {
                    return treeNode.size();
                }
            };
        }
        else if (treeNode instanceof JsonObject) {
            return new AbstractMap<String, Object>() {
                @Override
                public Set<Entry<String, Object>> entrySet() {
                    return new AbstractSet<Entry<String,Object>>() {
                        @Override
                        public Iterator<Entry<String, Object>> iterator() {
                            return new Iterator<Entry<String, Object>>() {
                                final Iterator<String> delegate = treeNode.fieldNames();

                                @Override
                                public boolean hasNext() {
                                    return delegate.hasNext();
                                }

                                @Override
                                public Entry<String, Object> next() {
                                    final String key = delegate.next();
                                    return new Entry<String, Object>() {
                                        @Override
                                        public String getKey() {
                                            return key;
                                        }

                                        @Override
                                        public Object getValue() {
                                            return valueOf(treeNode.get(key));
                                        }

                                        @Override
                                        public Object setValue(Object value) {
                                            throw new UnsupportedOperationException();
                                        }
                                    };
                                }

                                @Override
                                public void remove() {
                                    throw new UnsupportedOperationException();
                                }
                            };
                        }

                        @Override
                        public int size() {
                            return treeNode.size();
                        }
                    };
                }
            };
        }
        else return treeNode;
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
