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

import com.heaven7.java.base.util.Predicates;

import java.lang.reflect.*;
import java.util.*;

/**
 * the reflecty types
 * @author heaven7
 */
public final class $ReflectyTypes {

    private static WeakHashMap<Class<?>, List<TypeVariablePair>> sTypeVars = new WeakHashMap<>();

    public static TypeNode getTypeNode(Type type){
        $ReflectyTypes.GenericNode node = new $ReflectyTypes.GenericNode();
        $ReflectyTypes.parseNode(null, type, node);
        return node;
    }
    public static TypeNode getTypeNode(Class ownerClass, Type type){
        $ReflectyTypes.GenericNode node = new $ReflectyTypes.GenericNode();
        $ReflectyTypes.parseNode(ownerClass, type, node);
        return node;
    }
    /**
     * parse the class as generic node.
     * @param ownerClass the owner class
     * @param type the generic type of field/method
     * @param parent the node to populate
     */
    private static void parseNode(final Class ownerClass, Type type, GenericNode parent) {
        if(type instanceof ParameterizedType){
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            List<TypeNode> subs = new ArrayList<>();
            GenericNode node;
            for (Type t : types){
                node = new GenericNode();
                parseNode(ownerClass, t,  node);
                subs.add(node);
            }
            parent.type = (Class<?>) ((ParameterizedType) type).getRawType();
            parent.subType = subs;
        }else if(type instanceof GenericArrayType){
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            parent.isArray = true;
            GenericNode node = new GenericNode();
            parseNode(ownerClass, componentType,  node);
            parent.addNode(node);
        }else if(type instanceof WildcardType){
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if(Predicates.isEmpty(lowerBounds)){
                parseNode(ownerClass, upperBounds[0],  parent);
            }else {
                parseNode(ownerClass, lowerBounds[0], parent);
            }
        }else if(type instanceof TypeVariable){
            //indicates  Wildcard from object. that means only can be known as runtime.
            String name = ((TypeVariable) type).getName();
            if(sTypeVars.get(ownerClass) == null){
                ParameterizedType pt = (ParameterizedType) ownerClass.getGenericSuperclass();
                if(pt == null){
                    throw new UnsupportedOperationException("must extend the generic super class");
                }
                TypeVariable<?>[] types = ((TypeVariable) type).getGenericDeclaration().getTypeParameters();
                List<TypeVariablePair> pairs = new ArrayList<>();
                Type[] tts = pt.getActualTypeArguments();
                for (int i = 0 , size = tts.length ; i < size ; i ++){
                    GenericNode node = new GenericNode();
                    parseNode(ownerClass, tts[i], node);
                    pairs.add(new TypeVariablePair(types[i].getName(), node));
                }
                sTypeVars.put(ownerClass, pairs);
            }
            TypeVariablePair pair = getTypeVariablePair(ownerClass, name);
            if(pair != null){
                parent.addVariableNode(pair.node);
            }else {
                throw new UnsupportedOperationException("can't find TypeVariablePair for "
                        + name + " ,from class " + ownerClass.getName());
            }
        } else if(type instanceof Class){
            Class<?> clazz = (Class<?>) type;
            if(clazz.isArray()){
                parent.isArray = true;
                GenericNode node = new GenericNode();
                parseNode(ownerClass, clazz.getComponentType(),  node);
                parent.addNode(node);
            }else {
                parent.type = clazz;
            }
        }else {
            throw new RuntimeException("" + type);
        }
    }

    private static TypeVariablePair getTypeVariablePair(Class<?> clazz, String declareName){
        List<TypeVariablePair> pairs = sTypeVars.get(clazz);
        if(Predicates.isEmpty(pairs)){
            return null;
        }
        for (TypeVariablePair pair : pairs){
            if(pair.declareName.equals(declareName)){
                return pair;
            }
        }
        return null;
    }

    private static class GenericNode implements TypeNode {
        public Class<?> type;
        public List<TypeNode> varNodes; //for variable nodes
        public List<TypeNode> subType;
        public boolean isArray;

        public void addNode(GenericNode node) {
            if(subType == null){
                subType = new ArrayList<>();
            }
            subType.add(node);
        }
        public void addVariableNode(GenericNode varNode){
            if(varNodes == null){
                varNodes = new ArrayList<>();
            }
            varNodes.add(varNode);
        }

        @Override
        public Class<?> getRawClass() {
            return type;
        }

        @Override
        public boolean isArray() {
            return isArray;
        }
        @Override
        public List<TypeNode> getVariableNodes() {
            return varNodes;
        }
        @Override
        public Class<?> getTypeClass(int index) {
            if(type != null){
                return type;
            }
            if(isArray){
                return Array.newInstance(subType.get(0).getTypeClass(0), 0).getClass();
            }
            return varNodes.get(index).getTypeClass(0);
        }
        @Override
        public TypeNode getSubNode(int index){
            if(Predicates.isEmpty(subType)){
                if(!Predicates.isEmpty(varNodes)){
                    return varNodes.get(index);
                }
                return null;
            }else {
                return subType.get(index);
            }
        }
        @Override
        public int getSubNodeCount(){
            if(Predicates.isEmpty(subType)){
                if(!Predicates.isEmpty(varNodes)){
                    return varNodes.size();
                }
                return 0;
            }else {
                return subType.size();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GenericNode node = (GenericNode) o;
            return isArray == node.isArray &&
                    Objects.equals(type, node.type) &&
                    Objects.equals(varNodes, node.varNodes) &&
                    Objects.equals(subType, node.subType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, varNodes, subType, isArray);
        }
    }
    private static class TypeVariablePair{
        public final String declareName;
        public final GenericNode node;
        public TypeVariablePair(String declareName, GenericNode node) {
            this.declareName = declareName;
            this.node = node;
        }
    }
}