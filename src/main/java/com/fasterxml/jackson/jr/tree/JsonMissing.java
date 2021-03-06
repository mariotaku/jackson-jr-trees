package com.fasterxml.jackson.jr.tree;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;

/**
 * Virtual node used instead of `null`, when an operation does not match an
 * actual existing node; this can significantly simplify handling when no
 * null checks are needed.
 */
public final class JsonMissing extends JacksonJrValue
{
    final static JsonMissing instance = new JsonMissing();

    public final static JsonMissing instance() {
        return instance;
    }

    @Override
    public JsonToken asToken() {
        return JsonToken.NOT_AVAILABLE;
    }

    @Override
    public boolean isValueNode() {
        return false;
    }

    @Override
    public boolean isContainerNode() {
        return false;
    }

    @Override
    public boolean isMissingNode() {
        return true;
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
        return MISSING;
    }

    @Override
    public TreeNode path(int i) {
        return MISSING;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o == this);
    }

    @Override
    public String toString() {
        // toString() should never return null
        return "";
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
