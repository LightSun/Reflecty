package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Constructor;

public final class BaseCommonAdapter<T> extends GsonBasicAdapter {

    private final Constructor<?> constructor;

    public BaseCommonAdapter(Class<T> clazz) {
        try {
            this.constructor = clazz.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        if(obj == null){
            sink.nullValue();
        }else {
            if(obj instanceof Number){ //byte, short, int, long, float, double. Boolean.
                sink.value((Number)obj);
            }else if(obj instanceof Boolean){
                sink.value((Boolean) obj);
            }else {
                throw new UnsupportedOperationException();
            }
        }
        return -1;
    }
    @Override
    public Object read(JsonReader source) throws IOException {
        String str = source.nextString();
        if(str != null){
            try {
                return constructor.newInstance(str);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    @Override
    protected TypeAdapter<JsonWriter, JsonReader> onCreateNameTypeAdapter() {
        return new NameTypeAdapter(constructor);
    }

    private static class NameTypeAdapter extends GsonAdapter{

        private final Constructor<?> constructor;

        NameTypeAdapter(Constructor<?> constructor) {
            this.constructor = constructor;
        }

        @Override
        public int write(JsonWriter sink, Object obj) throws IOException {
            if(obj !=null){
                sink.name(obj.toString());
            }
            return -1;
        }
        @Override
        public Object read(JsonReader source) throws IOException {
            String str = source.nextName();
            try {
                return constructor.newInstance(str);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
