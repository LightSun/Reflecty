/*
 * Copyright 2019
 * heaven7(donshine723@gmail.com)

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.java.reflecty;

import java.util.List;

/**
 * the type node often indicate the parameter types from {@linkplain TypeToken}.
 * this can used well any type of {@linkplain java.lang.reflect.Type}.
 * @author heaven7
 */
public interface TypeNode {

    /**
     * get the type class for target index node
     * @param index the index
     * @return the represent class
     * @since 1.0.1
     */
    Class<?> getTypeClass(int index);

    /**
     * get the raw class
     * @return the raw class
     * @since 1.0.1
     */
    Class<?> getRawClass();

    /**
     * indicate whether the node is array or not.
     * @return true if this node represent an array.
     * @since 1.0.1
     */
    boolean isArray();

    /**
     * get variable nodes for target index
     * @return the variable nodes if exist. can be null
     * @since 1.0.1
     */
    List<TypeNode> getVariableNodes();

    /**
     * get sub node for target index. sub node can be var node
     * @param index the index
     * @return the sub node
     * @since 1.0.1
     */
    TypeNode getSubNode(int index);

    /**
     * get sub node count.
     * @return the sub node count
     * @since 1.0.1
     */
    int getSubNodeCount();

    /**
     * the hash code of this node
     * @return the hash code
     */
    int hashCode();

    /**
     * equals or not.
     * @param o the other node
     * @return true if equals.
     */
    boolean equals(Object o);
}
