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

/**
 * the reflecty builder which used to build {@linkplain Reflecty}.
 * @param <CD> the class annotation which is set on class , often is the class description. can be used for field or method annotation.
 * @param <F> the field annotation
 * @param <M> the method annotation
 * @param <I> the inherit annotation
 * @author heaven7
 */
public final class ReflectyBuilder<PR, CD extends Annotation,F extends Annotation,
        M extends Annotation, I extends Annotation>  {

    Class<CD> clazzObject;
    Class<F> clazzField;
    Class<M> clazzMethod;
    Class<I> clazzInherit;

    ReflectyDelegate<PR, CD, F, M, I> mDelegate;

    public ReflectyBuilder<PR, CD, F, M, I> classAnnotation(Class<CD> clazz){
        this.clazzObject = clazz;
        return this;
    }
    public ReflectyBuilder<PR, CD, F, M, I> fieldAnnotation(Class<F> clazz){
        this.clazzField = clazz;
        return this;
    }
    public ReflectyBuilder<PR, CD, F, M, I> methodAnnotation(Class<M> clazz){
        this.clazzMethod = clazz;
        return this;
    }
    public ReflectyBuilder<PR, CD, F, M, I> inheritAnnotation(Class<I> clazz){
        this.clazzInherit = clazz;
        return this;
    }
    public ReflectyBuilder<PR, CD, F, M, I> delegate(ReflectyDelegate<PR,CD, F, M, I> delegate){
        this.mDelegate = delegate;
        return this;
    }
    public Reflecty<PR, CD, F, M, I> build(){
        if(clazzField == null){
            throw new IllegalStateException("the field annotation class can't be null");
        }
        return new Reflecty<PR, CD, F, M, I>(this);
    }
}
