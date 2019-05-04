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
 * the type adapter manager
 * @author heaven7
 */
public interface ITypeAdapterManager {

    /**
     * get the type adapter context
     * @return the type adapter context
     */
    TypeAdapterContext getTypeAdapterContext();

    /**
     * get the type adapter for type node.
     * @param genericNode the type node
     * @param applyVersion the expect version
     * @return the type adapter
     */
    TypeAdapter getTypeAdapter(TypeNode genericNode, float applyVersion);

    /**
     * get the base type adapter
     * @param baseType the base type class
     * @return the base {@linkplain TypeAdapter}.
     */
    TypeAdapter getBaseTypeAdapter(Class<?> baseType);

    /**
     * create collection type adapter
     *
     * @param collectionClass the expect collection class
     * @param componentAdapter the component adapter
     * @return the type adapter
     */
    TypeAdapter createCollectionTypeAdapter(Class<?> collectionClass, TypeAdapter componentAdapter);

    /**
     * create array type adapter
     * @param componentClass the component class
     * @param componentAdapter the component adapter
     * @return the array type adapter
     */
    TypeAdapter createArrayTypeAdapter(Class<?> componentClass, TypeAdapter componentAdapter);

    /**
     * create map type adapter
     * @param mapClazz the map class. can be sparse array
     * @param keyAdapter the key adapter
     * @param valueAdapter the value adapter
     * @return the map type adapter
     */
    TypeAdapter createMapTypeAdapter(Class<?> mapClazz, TypeAdapter keyAdapter, TypeAdapter valueAdapter);

    /**
     * create the object type adapter
     *
     * @param objectClazz the object class. not collection or map.
     * @param applyVersion the apply version
     * @return the type adapter
     */
    TypeAdapter createObjectTypeAdapter(Class<?> objectClazz, float applyVersion);
}
