package com.heaven7.java.reflecty.gson.member;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.heaven7.java.reflecty.FieldProxy;

import java.lang.reflect.Field;

public class GsonFieldProxy extends FieldProxy implements StartEndMemberProxy {

    private final float since;
    private final float until;

    public GsonFieldProxy(Class<?> ownerClass, Field field, String property) {
        super(ownerClass, field, property);
        Since u = field.getAnnotation(Since.class);
        Until n = field.getAnnotation(Until.class);
        this.since = u != null ? (float) u.value() : -1f;
        this.until = n != null ? (float) n.value() : -1f;
        if(until > 0 && until <= since){
            throw new IllegalStateException("until must larger than since.");
        }
    }
    @Override
    public boolean isVersionMatched(float expectVersion) {
        if(since < 0){
            return true;
        }
        if(expectVersion >= since){
            if(until >= 0){
                return expectVersion < until;
            }else {
                return true;
            }
        }
        return false;
    }
}
