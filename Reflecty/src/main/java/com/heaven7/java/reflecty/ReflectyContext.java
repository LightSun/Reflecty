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

import java.util.Collection;
import java.util.Map;

/**
 * the type adapter context
 * @author heaven7
 */
public interface ReflectyContext {

    /**
     * create object for target class
     * @param clazz the class
     * @return the object
     */
    Object newInstance(Class<?> clazz);

    /**
     * indicate the class is map or not
     * @param type the class to check
     * @return true if the class is map
     */
    boolean isMap(Class<?> type);

    /**
     * create map from target 'Map' class. eg: {@linkplain com.heaven7.java.base.util.SparseArray}.
     * @param clazz the class which from 'Map'.
     * @return the map
     */
    Map createMap(Class<?> clazz);

    /**
     * get the map from target object . obj can be normal map or {@linkplain com.heaven7.java.base.util.SparseArrayDelegate}
     * and etc.
     * @param obj the object type of 'map'
     * @return the transformed map
     */
    Map getMap(Object obj);

    /**
     * indicate the class can be used as collection or not.
     * @param clazz the class
     * @return true if regard it as collection.
     */
    boolean isCollection(Class<?> clazz);

    /**
     * create collection from target class name
     * @param clazz the class which often extend collection. like list, set and etc.
     * @return the collection
     */
    Collection createCollection(Class<?> clazz);

    /**
     * make the object used as collection and get it
     * @param obj the object
     * @return the collection
     */
    Collection getCollection(Object obj);
}
