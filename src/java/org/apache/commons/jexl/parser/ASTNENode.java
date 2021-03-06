/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jexl.parser;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.util.Coercion;

/**
 * Not equal to. Use '!=' or 'ne', do not use <>.
 * 
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ASTNENode extends SimpleNode {
    /**
     * Create the node given an id.
     * 
     * @param id node id.
     */
    public ASTNENode(int id) {
        super(id);
    }

    /**
     * Create a node with the given parser and id.
     * 
     * @param p a parser.
     * @param id node id.
     */
    public ASTNENode(Parser p, int id) {
        super(p, id);
    }

    /** {@inheritDoc} */
    public Object jjtAccept(ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /** {@inheritDoc} */
    public Object value(JexlContext pc) throws Exception {
        Object left = ((SimpleNode) jjtGetChild(0)).value(pc);
        Object right = ((SimpleNode) jjtGetChild(1)).value(pc);

        if (left == null && right == null) {
            /*
             * first, the possibility that both *are* null
             */

            return Boolean.FALSE;
        } else if (left == null || right == null) {
            /*
             * otherwise, both aren't, so it's clear L != R
             */
            return Boolean.TRUE;
        } else if (left.getClass().equals(right.getClass())) {
            return (left.equals(right)) ? Boolean.FALSE : Boolean.TRUE;
        } else if (left instanceof Float 
            || left instanceof Double 
            || right instanceof Float 
            || right instanceof Double) {
            return (Coercion.coerceDouble(left).equals(Coercion.coerceDouble(right))) ? Boolean.FALSE : Boolean.TRUE;
        } else if (left instanceof Number || right instanceof Number || left instanceof Character
            || right instanceof Character) {
            return (Coercion.coerceLong(left).equals(Coercion.coerceLong(right))) ? Boolean.FALSE : Boolean.TRUE;
        } else if (left instanceof Boolean || right instanceof Boolean) {
            return (Coercion.coerceBoolean(left).equals(Coercion.coerceBoolean(right))) ? Boolean.FALSE : Boolean.TRUE;
        } else if (left instanceof java.lang.String || right instanceof String) {
            return (left.toString().equals(right.toString())) ? Boolean.FALSE : Boolean.TRUE;
        }

        return (left.equals(right)) ? Boolean.FALSE : Boolean.TRUE;
    }
}
