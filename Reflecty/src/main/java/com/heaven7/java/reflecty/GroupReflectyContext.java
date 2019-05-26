package com.heaven7.java.reflecty;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * multi reflecty context
 * @author heaven7
 * @since 1.0.3
 */
public class GroupReflectyContext implements ReflectyContext {

    private final List<ReflectyContext> contexts;

    public GroupReflectyContext(ReflectyContext... contexts) {
        this(Arrays.asList(contexts));
    }
    public GroupReflectyContext(List<ReflectyContext> contexts) {
        this.contexts = contexts;
    }

    @Override
    public Object newInstance(Class<?> clazz) {
        Object obj;
        for(ReflectyContext context : contexts){
            obj = context.newInstance(clazz);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }
    @Override
    public boolean isMap(Class<?> type) {
        for(ReflectyContext context : contexts){
            if(context.isMap(type)){
                return true;
            }
        }
        return false;
    }
    @Override
    public Map createMap(Class<?> clazz) {
        Map obj;
        for(ReflectyContext context : contexts){
            obj = context.createMap(clazz);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }
    @Override
    public Map getMap(Object obj1) {
        Map obj;
        for(ReflectyContext context : contexts){
            obj = context.getMap(obj1);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }
    @Override
    public boolean isCollection(Class<?> clazz) {
        for(ReflectyContext context : contexts){
            if(context.isCollection(clazz)){
                return true;
            }
        }
        return false;
    }
    @Override
    public Collection createCollection(Class<?> clazz) {
        Collection obj;
        for(ReflectyContext context : contexts){
            obj = context.createCollection(clazz);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }

    @Override
    public Collection getCollection(Object obj1) {
        Collection obj;
        for(ReflectyContext context : contexts){
            obj = context.getCollection(obj1);
            if(obj != null){
                return obj;
            }
        }
        return null;
    }
}
