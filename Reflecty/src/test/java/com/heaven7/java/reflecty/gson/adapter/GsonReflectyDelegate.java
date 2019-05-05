package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.*;
import com.heaven7.java.reflecty.gson.JsonObjectAdapter;
import com.heaven7.java.reflecty.gson.member.GsonFieldProxy;
import com.heaven7.java.reflecty.iota.TypeAdapter;
import com.heaven7.java.reflecty.member.FieldProxy;
import com.heaven7.java.reflecty.member.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class GsonReflectyDelegate implements ReflectyDelegate<TypeAdapter<JsonWriter, JsonReader>,
        JsonAdapter, SerializedName, Deprecated, Expose> {

    @Override
    public void sort(List<MemberProxy> out) {

    }
    @Override
    public boolean isAllowInherit(Class<JsonAdapter> clazz) {
        return false;
    }
    @Override
    public boolean isAllowInherit(Expose fieldInherit) {
        return fieldInherit == null || fieldInherit.serialize();
    }

    @Override
    public boolean isGetMethod(Deprecated mn) {
        return false;
    }
    @Override
    public String getPropertyFromMethod(Method method, Deprecated mn) {
        return null;
    }
    @Override
    public String getPropertyFromField(Field field, SerializedName fn) {
        return fn != null ? fn.value() : field.getName();
    }

    @Override
    public MethodProxy createMethodProxy(Class<?> owner, JsonAdapter classDesc, Method get, Method set, String property, Deprecated mn) {
        return null;
    }

    @Override
    public FieldProxy createFieldProxy(Class<?> owner, JsonAdapter classDesc, Field field, String property, SerializedName fn) {
        return new GsonFieldProxy(owner, field, property);
    }

    @Override
    public boolean shouldIncludeField(Field field, SerializedName fieldAnno, boolean isInherit) {
        Expose expose = field.getAnnotation(Expose.class);
        return expose == null || expose.serialize();
    }

    @Override
    public boolean shouldIncludeMethod(Method method, Deprecated methodAnno, boolean isInherit) {
        return false;
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> performReflectClass(Class<?> clazz) {
        JsonAdapter ja = clazz.getAnnotation(JsonAdapter.class);
        if(ja != null){
            try {
                return new JsonObjectAdapter((com.google.gson.TypeAdapter<Object>) ja.value().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
