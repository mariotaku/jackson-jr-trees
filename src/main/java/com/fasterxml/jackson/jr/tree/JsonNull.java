package com.fasterxml.jackson.jr.tree;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;

/**
 * Null node for null fields instead of `real` null value
 */
public class JsonNull extends JacksonJrValue {
    final static JsonNull instance = new JsonNull();

    public final static JsonNull instance() {
        return instance;
    }

    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_NULL;
    }

    @Override
    public boolean isValueNode() {
        return true;
    }

    @Override
    public boolean isContainerNode() {
        return false;
    }

    @Override
    public boolean isMissingNode() {
        return false;
    }

    @Override
    protected JacksonJrValue _at(JsonPointer ptr) {
        return this;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public TreeNode get(String s) {
        return null;
    }

    @Override
    public TreeNode get(int i) {
        return null;
    }

    @Override
    public TreeNode path(String s) {
        return NULL;
    }

    @Override
    public TreeNode path(int i) {
        return NULL;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this);
    }

    @Override
    public String toString() {
        // toString() should never return null
        return "null";
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
