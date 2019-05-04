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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * the reflecty deleagte
 * @param <CD> the annotation which is set on class , often is the class description. can be used for field or method annotation.
 * @param <F> the field annotation
 * @param <M> the method annotation
 * @param <I> the inherit annotation
 * @author heaven7
 */
public interface ReflectyDelegate<CD extends Annotation,
        F extends Annotation, M extends Annotation, I extends Annotation> {

    /**
     * sort the members
     * @param out the members to sort
     */
    void sort(List<MemberProxy> out);

    /**
     * indicate the annotation which is applied on the class is allow inherit or not.
     * if allow and current annotation is null,  it will find annotation from super classes until found.
     * @param clazz the annotation class  which is applied on the class
     * @return true if the class description allow inherit
     */
    boolean isAllowInherit(Class<CD> clazz);

    /**
     * indicate the annotation is allow inherit or not.
     * @param fieldInherit the inherit annotation object, can be null
     * @return true if allow inherit
     */
    boolean isAllowInherit(I fieldInherit);

    /**
     * indicate the method annotation is get method or not
     * @param mn the method annotation object
     * @return true if is get. false otherwise
     */
    boolean isGetMethod(M mn);
    /**
     * get the property from method
     * @param method the method object
     * @param mn the method annotation object
     * @return the property, never should be null
     */
    String getPropertyFromMethod(Method method, M mn);

    /**
     * get the property from field
     * @param field the field object
     * @param fn the field annotation object
     * @return the property
     */
    String getPropertyFromField(Field field, F fn);

    /**
     * create the method proxy
     * @param owner the owner clas
     * @param classDesc the annotation class desc object which is applied on class. can be null.
     * @param get the get method
     * @param set the set method
     * @param property the property
     * @param mn the method annotation object
     * @return the method proxy. see {@linkplain MethodProxy}
     */
    MethodProxy createMethodProxy(Class<?> owner, CD classDesc, Method get, Method set, String property, M mn);

    /**
     * create field proxy
     * @param owner the owner class
     * @param classDesc the annotation class desc object which is applied on class. can be null.
     * @param field the field
     * @param property the property from field
     * @param fn the field annotation object
     * @return the field proxy. see {@linkplain FieldProxy}
     */
    FieldProxy createFieldProxy(Class<?> owner, CD classDesc, Field field, String property, F fn);

    /**
     * indicate the field should be include or not.
     * @param field the field
     * @param fieldAnno the field annotation object. can be null
     * @param isInherit indicate the field is from inherit class or not.
     * @return true if should include field
     */
    boolean shouldIncludeField(Field field, F fieldAnno, boolean isInherit);

    /**
     * indicate the method should be include or not
     * @param method the method
     * @param methodAnno the method annotation object , can be null
     * @param isInherit the method is from inherit class or not
     * @return true if the method should be include
     */
    boolean shouldIncludeMethod(Method method, M methodAnno, boolean isInherit);
}
