package com.fasterxml.jackson.simple.tree;

import java.util.*;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;

public class JsonArray extends JacksonJrValue
{
    private final List<JacksonJrValue> _values;

    public JsonArray()
    {
        _values = Collections.<JacksonJrValue>emptyList();
    }

    public JsonArray(List<JacksonJrValue> v)
    {
        _values = Collections.unmodifiableList(v);
    }

    @Override
    public JsonToken asToken() {
        return START_ARRAY;
    }

    @Override
    public int size() {
        return _values.size();
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
    public JacksonJrValue get(int i) {
        return ((0 <= i) && (i < _values.size())) ? _values.get(i) : null;
    }

    @Override
    public TreeNode get(String s) {
        return null;
    }

    @Override
    public JacksonJrValue path(int i){
        return ((0 <= i) && (i < _values.size())) ? _values.get(i) : MISSING;
    }

    @Override
    public TreeNode path(String s) {
        return MISSING;
    }
    
    @Override
    protected JacksonJrValue _at(JsonPointer ptr) {
        return get(ptr.getMatchingIndex());
    }

    /*
    /**********************************************************************
    /* Extended API
    /**********************************************************************
     */

    public Iterator<JacksonJrValue> elements() {
        return _values.iterator();
    }
}
