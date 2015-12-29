package com.fasterxml.jackson.simple.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;

public class JsonObject extends JacksonJrValue
{
    private final Map<String, JacksonJrValue> _values;

    public JsonObject()
    {
        this(Collections.<String, JacksonJrValue>emptyMap());
    }

    public JsonObject(Map<String, JacksonJrValue> values)
    {
        this._values = Collections.unmodifiableMap(values);
    }

    @Override
    public JsonToken asToken()
    {
        return JsonToken.START_OBJECT;
    }

    @Override
    public int size()
    {
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
    public boolean isObject() {
        return true;
    }

    @Override
    public Iterator<String> fieldNames()
    {
        return _values.keySet().iterator();
    }

    @Override
    public TreeNode get(int i) {
        return null;
    }
    
    @Override
    public JacksonJrValue get(String name) {
        return _values.get(name);
    }

    @Override
    public TreeNode path(int i) {
        return MISSING;
    }

    @Override
    public JacksonJrValue path(String name) {
        JacksonJrValue v = _values.get(name);
        return (v == null) ? MISSING : v;
    }

    @Override
    protected JacksonJrValue _at(JsonPointer ptr) {
        return get(ptr.getMatchingProperty());
    }
}
