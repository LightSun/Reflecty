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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author heaven7
 */
public class SimpleReflectyContext implements ReflectyContext {

    @Override
    public Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public Map createMap(Class<?> clazz) {
        if(ConcurrentHashMap.class.isAssignableFrom(clazz)){
            return new ConcurrentHashMap();
        }
        else if(WeakHashMap.class.isAssignableFrom(clazz)){
            return new WeakHashMap();
        }
        else if(LinkedHashMap.class.isAssignableFrom(clazz)){
            return new LinkedHashMap();
        }
        else if(SortedMap.class.isAssignableFrom(clazz)){
            return new TreeMap();
        }
        else if(Map.class.isAssignableFrom(clazz)){
            return new HashMap();
        }
        return null;
    }
    @Override
    public Map getMap(Object obj) {
        if(obj instanceof Map){
            return (Map) obj;
        }
        return null;
    }
    @Override
    public Collection createCollection(Class<?> clazz) {
        if(LinkedList.class.isAssignableFrom(clazz)){
            return new LinkedList();
        }else if(Vector.class.isAssignableFrom(clazz)){
            return new Vector();
        }else if(CopyOnWriteArrayList.class.isAssignableFrom(clazz)){
            return new CopyOnWriteArrayList();
        }
        else if(List.class.isAssignableFrom(clazz)){
            return new ArrayList();
        }else if(CopyOnWriteArraySet.class.isAssignableFrom(clazz)){
            return new CopyOnWriteArraySet();
        }
        else if(SortedSet.class.isAssignableFrom(clazz)){
            return new TreeSet();
        }else if(Set.class.isAssignableFrom(clazz)){
            return new HashSet();
        }
        return null;
    }

    @Override
    public Collection getCollection(Object obj) {
        if(obj instanceof Collection){
            return (Collection) obj;
        }
        return null;
    }

    @Override
    public boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean isMap(Class<?> rawType) {
        return Map.class.isAssignableFrom(rawType);
    }
}
