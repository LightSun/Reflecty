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

import java.io.IOException;

/**
 * the type adapter which used to write and read message members.
 * @param <Out> the output type, used to write
 * @param <In> the input type ,used to read
 * @author heaven7
 */
public abstract class TypeAdapter<Out,In> {

    /**
     * create type adapter from {@linkplain TypeToken}
     * @param <Out> the output type
     * @param <In> the input type
     * @param tt the type token
     * @param tam the type adapter manager
     * @param applyVersion the version
     * @return the type adapter
     */
    public static <Out,In> TypeAdapter<Out,In> ofTypeToken(TypeToken<?> tt, ITypeAdapterManager<Out,In> tam, float applyVersion){
        return $ReflectyTypes.getTypeNode(tt.getType()).getTypeAdapter(tam, applyVersion);
    }

    /**
     * write the member by the sink.
     * @param sink the out sink
     * @param property the property which is from {@linkplain MemberProxy#getPropertyName()}
     * @param obj the object value
     * @return the write length as bytes count.
     * @throws IOException if an I/O error occurs
     */
    public abstract int write(Out sink, String property, Object obj) throws IOException;
    /**
     * read the value from source and set to the object.
     * @param source the input source
     * @param property the property which is from {@linkplain MemberProxy#getPropertyName()}
     * @throws IOException if an I/O error occurs
     */
    public abstract Object read(In source, String property) throws IOException;

    /**
     * evaluate the size of target member from object
     * @param obj the object
     * @return the size as bytes count
     */
    public abstract int evaluateSize(Object obj);

}
