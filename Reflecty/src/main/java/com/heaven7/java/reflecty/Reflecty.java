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
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * the reflecty used to quick reflect field and method members.
 * @param <PR> the perform result for reflect on target {@linkplain Class}.
 * @param <CD> the annotation which is set on class , often is the class description. can be used for field or method annotation.
 * @param <F> the field annotation which is set on field
 * @param <M> the method annotation which is set on method
 * @param <I> the inherit annotation which can be set on field or method.
 * @author heaven7
 */
public final class Reflecty<PR, CD extends Annotation,F extends Annotation,
        M extends Annotation, I extends Annotation> {

    private final WeakHashMap<Class<?>, List<MemberProxy>> mCache;
    // share cache for sub class .
    private final WeakHashMap<Class<?>, List<MemberProxy>> mShareCache;

    private final ReflectyDelegate<PR,CD,F, M, I> mDelegate;
    private final Class<CD> mClazzClass;
    private final Class<F> mClazzField;
    private final Class<M> mClazzMethod;
    private final Class<I> mClazzIherit;

    @SuppressWarnings("unchecked")
    /*public*/ Reflecty(ReflectyBuilder<PR, CD, F, M, I> builder) {
        this.mDelegate = builder.mDelegate;
        this.mClazzClass = builder.clazzObject;
        this.mClazzField = builder.clazzField;
        this.mClazzMethod = builder.clazzMethod;
        this.mClazzIherit = builder.clazzInherit;

        mCache = new WeakHashMap<>();
        mShareCache = new WeakHashMap<>();
    }

    /**
     * initialize the members to the cache.
     * @param classes the classes to cache members.
     * @see MemberProxy
     */
    public void initializeMembers(List<Class<?>> classes){
        for (Class<?> clazz : classes){
            getMemberProxies(clazz);
        }
    }

    /**
     * get the member proxies for target self-object. this is used for self-object.
     * exclude any collection and map class.
     * @param clazz the class
     * @return the member proxy list
     */
    public List<MemberProxy> getMemberProxies(Class<?> clazz) {
        List<MemberProxy> memberProxies = mCache.get(clazz);
        if(memberProxies == null){
            memberProxies = getMemberProxies1(clazz);
            mCache.put(clazz, memberProxies);
        }
        return memberProxies;
    }

    /**
     * perform reflect only on the class without fields and methods
     * @param clazz the class to reflect
     * @return the perform result
     */
    public PR performReflectClass(Class<?> clazz){
        return mDelegate.performReflectClass(clazz);
    }
    //------------------------------------- private ----------------------------------------------

    private List<MemberProxy> getMemberProxies1(Class<?> clazz) {
        // desc
        List<MemberProxy> out = new ArrayList<>();
        getMemberProxies0(clazz, out);
        mDelegate.sort(out);
        return out;
    }

    private void getMemberProxies0(Class<?> clazz, List<MemberProxy> out) {
        final Class<?> rawClass = clazz;
        /*
         */
        while (clazz != Object.class) {
            final boolean isInherit = clazz != rawClass;
            // from super. check super's allow members.
            if (isInherit) {
                List<MemberProxy> proxies = mShareCache.get(clazz);
                if (proxies != null) {
                    out.addAll(0, proxies);
                    clazz = clazz.getSuperclass();
                    if (clazz == null) {
                        break;
                    }else{
                        continue;
                    }
                }
            }
            // a list. which allow subclass inherit. this will share to cache
            List<MemberProxy> allowInherits = new ArrayList<>();
            // handle fields
            Field[] fields = clazz.getDeclaredFields();
            CD clazzClass = null;
            if(mClazzClass != null){
                clazzClass = clazz.getAnnotation(mClazzClass);
                if(clazzClass == null){
                    //check if 'mClazzClass' is allow inherit
                    if(mDelegate.isAllowInherit(mClazzClass)){
                        //find from nearest super class.
                        Class<?> superClass = clazz.getSuperclass();
                        do {
                            clazzClass = superClass.getAnnotation(mClazzClass);
                            superClass = superClass.getSuperclass();
                            if(superClass == Object.class || superClass == null){
                                break;
                            }
                        }while (clazzClass == null);
                    }
                }
            }
            //handle fields
            for (Field f: fields) {
                f.setAccessible(true);
                F fieldDesc = f.getAnnotation(mClazzField);
                // allow inherit
                if (mDelegate.shouldIncludeField(f, fieldDesc, isInherit)) {
                    FieldProxy proxy = mDelegate.createFieldProxy(clazz, clazzClass, f,
                            mDelegate.getPropertyFromField(f, fieldDesc), fieldDesc);
                    out.add(proxy);
                    //
                    I inherit = mClazzIherit != null ? f.getAnnotation(mClazzIherit) : null;
                    if (mDelegate.isAllowInherit(inherit)) {
                        allowInherits.add(proxy);
                    }
                }
            }
            //handle methods
            if(mClazzMethod != null){
                List<Method> gets = new ArrayList<>();
                List<Method> sets = new ArrayList<>();
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    method.setAccessible(true);

                    M mm = method.getAnnotation(mClazzMethod);
                    if(!mDelegate.shouldIncludeMethod(method, mm, isInherit)){
                        continue;
                    }
                    if (mDelegate.isGetMethod(mm)) {
                        //get must be no arguments
                        if(method.getParameterTypes().length > 0){
                            throw new IllegalStateException("as 'get' method for @MethodMember must have no arguments.");
                        }
                        //for better use. only check get method.
                        if(isInherit){
                            I inherit = mClazzIherit != null ? method.getAnnotation(mClazzIherit) : null;
                            if(!mDelegate.isAllowInherit(inherit)){
                                continue;
                            }
                        }
                        gets.add(method);
                    } else {
                        if(method.getParameterTypes().length != 1){
                            throw new IllegalStateException("as 'set' method for @MethodMember can only have one argument.");
                        }
                        sets.add(method);
                    }
                }
                //not all empty
                if(!gets.isEmpty() || !sets.isEmpty()){
                    if(gets.size() >= sets.size()){
                        makePairMethods(clazz, clazzClass, gets, sets, true, out, allowInherits);
                    }else {
                        makePairMethods(clazz, clazzClass, sets, gets, false, out, allowInherits);
                    }
                }
            }
            //put share cache
            mShareCache.put(clazz, allowInherits);

            clazz = clazz.getSuperclass();
            if (clazz == null) {
                break;
            }
        }
    }

    // src.size() > other.size(). for methods. method name should not be proguard.
    private void makePairMethods(Class<?> owner, CD classDesc, List<Method> src, List<Method> others, boolean srcIsGet,
                                 List<MemberProxy> out, List<MemberProxy> shareOut) {
        // assert src.size() >= others.size();
        int bigSize = src.size();
        for (int i = 0; i < bigSize; i++) {
            Method method = src.get(i);
            M mn = method.getAnnotation(mClazzMethod);
            String property = mDelegate.getPropertyFromMethod(method, mn);
            Method other = null;
            for (Method m2 : others){
                if(mDelegate.getPropertyFromMethod(m2, m2.getAnnotation(mClazzMethod)).equals(property)){
                    other = m2;
                    break;
                }
            }
            //can't pair
            if(other == null){
                System.out.println("MessageProtocal: can't make-pair for method. " + method.toString());
                continue;
            }
            MethodProxy proxy;
            Method main;
            if(srcIsGet){
               // proxy = new MethodProxy(owner, method, other);
                proxy = mDelegate.createMethodProxy(owner, classDesc, method, other, property, mn);
                main = method;
            }else {
               // proxy = new MethodProxy(owner, other, method);
                proxy = mDelegate.createMethodProxy(owner, classDesc, other, method, property, mn);
                main = other;
            }
            out.add(proxy);
            //share cache for sub class
            I inherit = main.getAnnotation(mClazzIherit);
            if(mDelegate.isAllowInherit(inherit)){
                shareOut.add(proxy);
            }
        }
    }

}
