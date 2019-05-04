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

/**
 * the type node often indicate the parameter types from {@linkplain TypeToken}.
 * this can used well any type of {@linkplain java.lang.reflect.Type}.
 * @author heaven7
 */
public interface TypeNode {

    /**
     * get the type adapter from
     * @param <Out> the output type
     * @param <In> the input type
     * @param delegate the type adapter manager delegate
     * @param applyVersion the expect version
     * @return the type adapter
     */
    <Out, In>TypeAdapter<Out, In> getTypeAdapter(ITypeAdapterManager<Out, In> delegate, float applyVersion);

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
