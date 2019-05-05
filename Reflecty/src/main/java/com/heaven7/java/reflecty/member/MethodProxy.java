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
package com.heaven7.java.reflecty.member;

import com.heaven7.java.reflecty.MemberProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * the method proxy
 * @author heaven7
 */
public class MethodProxy extends BaseMemberProxy implements MemberProxy {

    private final Method get;
    private final Method set;
    private final String property;

    public MethodProxy(Class<?> ownerClass, Method get, Method set, String property) {
        super(ownerClass, get.getGenericReturnType());
        this.get = get;
        this.set = set;
        this.property = property;
    }

    public Method getGetMethod() {
        return get;
    }
    public Method getSetMethod() {
        return set;
    }
    @Override
    public String getPropertyName() {
        return property;
    }
    @Override
    public void setValue(Object obj, Object value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }
    @Override
    public Object getValue(Object obj) throws IllegalAccessException, InvocationTargetException {
        return get.invoke(obj);
    }
}
