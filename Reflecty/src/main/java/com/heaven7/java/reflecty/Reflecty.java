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
 * @param <FS> the fields annotation which is set on class
 * @param <F> the field annotation which is set on field
 * @param <M> the method annotation which is set on method
 * @param <I> the inherit annotation which can be set on field or method.
 * @author heaven7
 */
public abstract class Reflecty<FS extends Annotation,F extends Annotation,
        M extends Annotation, I extends Annotation> {

    private final WeakHashMap<Class<?>, List<MemberProxy>> sCache;
    // share cache for sub class .
    private final WeakHashMap<Class<?>, List<MemberProxy>> sShareCache;
    private final ReflectyDelegate<F, M, I> mDelegate;
    private final Class<FS> mClazzFields;
    private final Class<F> mClazzField;
    private final Class<M> mClazzMethod;
    private final Class<I> mClazzIherit;

    @SuppressWarnings("unchecked")
    public Reflecty(ReflectyDelegate<F, M, I> mDelegate) {
        this.mDelegate = mDelegate;
        List<Class<? extends Annotation>> list = TypeToken.getSuperclassTypeParameters(getClass());
        this.mClazzFields = (Class<FS>) list.get(0);
        this.mClazzField = (Class<F>) list.get(1);
        this.mClazzMethod = (Class<M>) list.get(2);
        this.mClazzIherit = (Class<I>) list.get(3);

        sCache = new WeakHashMap<>();
        sShareCache = new WeakHashMap<>();
    }

    /**
     * init the members to the cache.
     * @param classes the classes to cache members.
     * @see MemberProxy
     */
    public void initializeMembers(List<Class<?>> classes){
        for (Class<?> clazz : classes){
            getMemberProxies(clazz);
        }
    }
    public List<MemberProxy> getMemberProxies(Class<?> clazz) {
        List<MemberProxy> memberProxies = sCache.get(clazz);
        if(memberProxies == null){
            memberProxies = getMemberProxies1(clazz);
            sCache.put(clazz, memberProxies);
        }
        return memberProxies;
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
         * 1, has @FieldMembers. isInherit ? judge if has @Inherit.
         */
        while (clazz != Object.class) {
            final boolean isInherit = clazz != rawClass;
            // from super. check super's allow members.
            if (isInherit) {
                List<MemberProxy> proxies = sShareCache.get(clazz);
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
            FS fieldMembers = clazz.getAnnotation(mClazzFields);
            if (fieldMembers != null) {
                if (isInherit) {
                    // has FieldMembers and in 'inherit'
                    for (Field f : fields) {
                        f.setAccessible(true);
                        I fieldInherit = f.getAnnotation(mClazzIherit);
                        // allow inherit
                        if (mDelegate.isAllowInherit(fieldInherit)) {
                           // FieldProxy proxy = new FieldProxy(clazz, f, f.getAnnotation(FieldMember.class));
                            F ff = f.getAnnotation(mClazzField);
                            FieldProxy proxy = mDelegate.createFieldProxy(clazz, f, mDelegate.getPropertyFromField(f, ff), ff);
                            out.add(proxy);
                            allowInherits.add(proxy);
                        }
                    }
                } else {
                    // has FieldMembers, and fields is self.
                    for (Field f : fields) {
                        f.setAccessible(true);
                       // FieldProxy proxy = new FieldProxy(clazz, f, f.getAnnotation(FieldMember.class));
                        F ff = f.getAnnotation(mClazzField);
                        FieldProxy proxy = mDelegate.createFieldProxy(clazz, f, mDelegate.getPropertyFromField(f, ff), ff);
                        out.add(proxy);
                        // for share cache
                        I fieldInherit = f.getAnnotation(mClazzIherit);
                        if (mDelegate.isAllowInherit(fieldInherit)) {
                            allowInherits.add(proxy);
                        }
                    }
                }
            } else {
                // no assign include all fields. need FieldMember.
                if (isInherit) {
                    // in 'inherit'
                    for (Field f : fields) {
                        f.setAccessible(true);
                        F mm = f.getAnnotation(mClazzField);
                        // allow inherit
                        if (mm != null) {
                            I fieldInherit = f.getAnnotation(mClazzIherit);
                            if (mDelegate.isAllowInherit(fieldInherit)) {
                                //FieldProxy proxy = new FieldProxy(clazz, f, mm);
                                FieldProxy proxy = mDelegate.createFieldProxy(clazz, f, mDelegate.getPropertyFromField(f, mm), mm);
                                out.add(proxy);
                                allowInherits.add(proxy);
                            }
                        }
                    }
                } else {
                    // self fields
                    for (Field f : fields) {
                        f.setAccessible(true);
                        F mm = f.getAnnotation(mClazzField);
                        if (mm == null) {
                            continue;
                        }
                        FieldProxy proxy = mDelegate.createFieldProxy(clazz, f, mDelegate.getPropertyFromField(f, mm), mm);
                        out.add(proxy);
                        // for share cache
                        I fieldInherit = f.getAnnotation(mClazzIherit);
                        if (mDelegate.isAllowInherit(fieldInherit)) {
                            allowInherits.add(proxy);
                        }
                    }
                }
            }
            //handle methods
            List<Method> gets = new ArrayList<>();
            List<Method> sets = new ArrayList<>();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);

                M mm = method.getAnnotation(mClazzMethod);
                if (mm == null) {
                    continue;
                }
                if (mDelegate.isGetMethod(mm)) {
                    //get must be no arguments
                    if(method.getParameterTypes().length > 0){
                        throw new IllegalStateException("as 'get' method for @MethodMember must have no arguments.");
                    }
                    //for better use. only check get method.
                    if(isInherit){
                        if(!mDelegate.isAllowInherit(method.getAnnotation(mClazzIherit))){
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
                    makePairMethods(clazz, gets, sets, true, out, allowInherits);
                }else {
                    makePairMethods(clazz, sets, gets, false, out, allowInherits);
                }
            }
            //put share cache
            sShareCache.put(clazz, allowInherits);

            clazz = clazz.getSuperclass();
            if (clazz == null) {
                break;
            }
        }
    }

    // src.size() > other.size(). for methods. method name should not be proguard.
    private void makePairMethods(Class<?> owner,List<Method> src, List<Method> others, boolean srcIsGet,
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
                proxy = mDelegate.createMethodProxy(owner, method, other, property, mn);
                main = method;
            }else {
               // proxy = new MethodProxy(owner, other, method);
                proxy = mDelegate.createMethodProxy(owner, other, method, property, mn);
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
