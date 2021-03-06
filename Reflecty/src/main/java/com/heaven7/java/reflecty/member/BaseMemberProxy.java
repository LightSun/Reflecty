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


import com.heaven7.java.reflecty.$ReflectyTypes;
import com.heaven7.java.reflecty.MemberProxy;
import com.heaven7.java.reflecty.TypeNode;

import java.lang.reflect.Type;

/**
 * @author heaven7
 */
public abstract class BaseMemberProxy implements MemberProxy {

    private static final int TYPE_MASK = 0xff;
    private static final int TYPE_FLAG_MASK = 0xff00;

    private final Class<?> mOwnerClass;
    private final TypeNode mNode;

    public BaseMemberProxy(Class<?> ownerClass, Type genericType) {
        this.mNode = $ReflectyTypes.getTypeNode(ownerClass, genericType);
        this.mOwnerClass = ownerClass;
    }

    @Override
    public final TypeNode getTypeNode() {
        return mNode;
    }

    @Override
    public final Class<?> getOwnerClass() {
        return mOwnerClass;
    }
    /**
     * get the base types
     * @param value the value which is from {@linkplain #parseType(Class)}.
     * @return the core type . see {@linkplain #TYPE_STRING} and etc.
     */
    public static int getType(int value){
        return value & TYPE_MASK;
    }
    /**
     * get the flag
     * @param value the value which is from {@linkplain #parseType(Class)}.
     * @return the extra flag
     */
    public static int getFlags(int value){
        return (value & TYPE_FLAG_MASK ) >> 8;
    }

    /**
     * parse the type for base class and string.
     * @param clazz the class
     * @return the type
     */
    public static int parseType(Class<?> clazz){
        if(clazz == byte.class){
            return MemberProxy.TYPE_BYTE;
        }else if(clazz == Byte.class){
            return MemberProxy.TYPE_BYTE + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == short.class){
            return MemberProxy.TYPE_SHORT ;
        }
        else if(clazz == Short.class){
            return MemberProxy.TYPE_SHORT + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == int.class){
            return MemberProxy.TYPE_INT ;
        } else if(clazz == Integer.class){
            return MemberProxy.TYPE_INT  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == long.class){
            return MemberProxy.TYPE_LONG  ;
        }else if(clazz == Long.class){
            return MemberProxy.TYPE_LONG  + (MemberProxy.FLAG_PACKED << 8);
        }

        else if(clazz == float.class){
            return MemberProxy.TYPE_FLOAT ;
        }else if(clazz == Float.class){
            return MemberProxy.TYPE_FLOAT  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == double.class){
            return MemberProxy.TYPE_DOUBLE ;
        }else if(clazz == Double.class){
            return MemberProxy.TYPE_DOUBLE  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == boolean.class){
            return MemberProxy.TYPE_BOOLEAN  ;
        }else if(clazz == Boolean.class){
            return MemberProxy.TYPE_BOOLEAN  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == char.class){
            return MemberProxy.TYPE_CHAR;
        }else if(clazz == Character.class){
            return MemberProxy.TYPE_CHAR  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == String.class){
            return MemberProxy.TYPE_STRING;
        }else {
            return -1;
        }
    }
}
