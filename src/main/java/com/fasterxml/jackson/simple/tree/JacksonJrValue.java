package com.fasterxml.jackson.simple.tree;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;

/**
 * Shared base class for all "simple" node types of Jackson Jr
 * "simple tree" package; implements {@link TreeNode} and is usable
 * via matching {@link TreeCodec} implementation (see {@link JacksonJrSimpleTreeCodec}).
 */
abstract class JacksonJrValue implements TreeNode
{
    protected static final JacksonJrValue MISSING = JsonMissing.instance();

    @Override
    public JsonParser.NumberType numberType() {
        return null;
    }

    @Override
    public boolean isMissingNode() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public Iterator<String> fieldNames() {
        return null;
    }

    @Override
    public JacksonJrValue at(JsonPointer jsonPointer) {
        if (jsonPointer.matches()) {
            return this;
        }
        JacksonJrValue n = _at(jsonPointer);
        if (n == null) {
            return MISSING;
        }
        return n.at(jsonPointer.tail());
    }

    @Override
    public JacksonJrValue at(String s) {
        return at(JsonPointer.compile(s));
    }

    protected abstract JacksonJrValue _at(JsonPointer ptr);
    
    @Override
    public JsonParser traverse() {
        return JacksonJrSimpleTreeCodec.SINGLETON.treeAsTokens(this);
    }

    @Override
    public JsonParser traverse(ObjectCodec objectCodec) {
        return JacksonJrSimpleTreeCodec.SINGLETON.treeAsTokens(this);
    }

    /**
     * Intermediate base class for non-structured types, other than
     * {@link JsonMissing}.
     */
    public static abstract class Scalar extends JacksonJrValue
    {
        @Override
        public final boolean isValueNode() {
            return true;
        }

        @Override
        public final boolean isContainerNode() {
            return false;
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
        protected JacksonJrValue _at(JsonPointer ptr) {
            // will only allow direct matches, but no traversal through
            // (base class checks for direct match)
            return MISSING;
        }
    }
}
