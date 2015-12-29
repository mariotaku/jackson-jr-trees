package com.fasterxml.jackson.simple.tree;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;

public class JsonArray extends JacksonJrValue
{
    private final List<JacksonJrValue> values;

    public JsonArray()
    {
        this(Collections.<JacksonJrValue>emptyList());
    }

    public JsonArray(List<JacksonJrValue> values)
    {
        this.values = Collections.unmodifiableList(values);
    }

    @Override
    public JsonToken asToken()
    {
        return START_ARRAY;
    }

    @Override
    public int size()
    {
        return values.size();
    }

    @Override
    public boolean isValueNode() {
        return false;
    }

    @Override
    public boolean isContainerNode() {
        return true;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public JacksonJrValue get(int i)
    {
        return ((0 <= i) && (i < values.size())) ? values.get(i) : null;
    }

    @Override
    public TreeNode get(String s) {
        return null;
    }

    @Override
    public JacksonJrValue path(int i)
    {
        return ((0 <= i) && (i < values.size())) ? values.get(i) : MISSING;
    }

    @Override
    public TreeNode path(String s) {
        return MISSING;
    }
    
    @Override
    protected JacksonJrValue _at(JsonPointer ptr) {
        return get(ptr.getMatchingIndex());
    }
}
