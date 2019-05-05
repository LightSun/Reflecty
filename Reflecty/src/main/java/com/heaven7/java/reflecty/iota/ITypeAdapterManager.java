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
 package com.heaven7.java.reflecty.iota;

 import com.heaven7.java.reflecty.ReflectyContext;
 import com.heaven7.java.reflecty.TypeNode;

 import java.lang.reflect.Type;
 import java.util.Map;

 /**
  * the type adapter manager
  *
  * @param <Out> the output type, used to write
  * @param <In>  the input type ,used to read
  * @author heaven7
  */
 public interface ITypeAdapterManager<Out, In> {

     /**
      * get the type adapter context
      *
      * @return the type adapter context
      */
     ReflectyContext getReflectyContext();

     /**
      * get the type adapter for type node.
      *
      * @param genericNode  the type node
      * @param applyVersion the expect version
      * @return the type adapter
      */
     TypeAdapter<Out, In> getTypeAdapter(TypeNode genericNode, float applyVersion);

     /*
      * get the type adapter. this method is similar as {@linkplain #getTypeAdapter(TypeNode, float)}.
      * @param type the type
      * @param applyVersion the version to apply
      * @return the type adapter
      */
     // TypeAdapter<Out, In> getTypeAdapter(Type type, float applyVersion);

     /**
      * register type adapter
      *
      * @param type    the type
      * @param version the start version of type
      * @param adapter the type adapter
      */
     void registerTypeAdapter(Type type, float version, TypeAdapter<Out, In> adapter);

     /**
      * register basic type adapter. for basic types. can include un-change type.
      *
      * @param baseType the basic type
      * @param adapter  the type adapter
      */
     void registerBasicTypeAdapter(Class<?> baseType, TypeAdapter<Out, In> adapter);

     /**
      * get the base type adapter
      *
      * @param baseType the base type class
      * @return the base {@linkplain TypeAdapter}.
      */
     TypeAdapter<Out, In> getBasicTypeAdapter(Class<?> baseType);

     //================================================================================================
     /**
      * get the key adapter for target map class type. this often used when you want to use a self-type map.
      * can non-extend {@linkplain Map}. see {@linkplain ReflectyContext#isMap(Class)} and {@linkplain ReflectyContext#createMap(Class)}.
      * @param type the class. can be {@linkplain com.heaven7.java.base.util.SparseArrayDelegate}.
      * @return the key type adapter
      */
     TypeAdapter<Out,In> getKeyAdapter(Class<?> type);
     /**
      * get the value adapter for target map class type. this often used when you want to use a self-type map.
      * can non-extend {@linkplain Map}. see {@linkplain ReflectyContext#isMap(Class)} and {@linkplain ReflectyContext#createMap(Class)}.
      * @param type the class . can be any class like {@linkplain Map}.
      * @return the value type adapter. or null if you haven't use self-type map.
      */
     TypeAdapter<Out,In> getValueAdapter(Class<?> type);

     /**
      * get the element type adapter for collection class type. this often used when you want to use a self-type collection.
      * can non-extend {@linkplain java.util.Collection}. see {@linkplain ReflectyContext#isCollection(Class)} and {@linkplain ReflectyContext#createCollection(Class)}.
      * @param type the collection class. can be any similar collection. like {@linkplain java.util.Collection}
      * @return the element type adapter. or null if you haven't use self-type collection.
      */
     TypeAdapter<Out, In> getElementAdapter(Class<?> type);
     //========================================================================

     /**
      * create collection type adapter
      *
      * @param collectionClass  the expect collection class
      * @param componentAdapter the component adapter
      * @return the type adapter
      */
     TypeAdapter<Out, In> createCollectionTypeAdapter(Class<?> collectionClass, TypeAdapter<Out, In> componentAdapter);

     /**
      * create array type adapter
      *
      * @param componentClass   the component class
      * @param componentAdapter the component adapter
      * @return the array type adapter
      */
     TypeAdapter<Out, In> createArrayTypeAdapter(Class<?> componentClass, TypeAdapter<Out, In> componentAdapter);

     /**
      * create map type adapter
      *
      * @param mapClazz     the map class. can be sparse array
      * @param keyAdapter   the key adapter
      * @param valueAdapter the value adapter
      * @return the map type adapter
      */
     TypeAdapter<Out, In> createMapTypeAdapter(Class<?> mapClazz, TypeAdapter<Out, In> keyAdapter, TypeAdapter<Out, In> valueAdapter);

     /**
      * create the object type adapter
      *
      * @param objectClazz  the object class. not collection or map.
      * @param applyVersion the apply version
      * @return the type adapter
      */
     TypeAdapter<Out, In> createObjectTypeAdapter(Class<?> objectClazz, float applyVersion);

 }
