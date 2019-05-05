package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class ArrayTypeAdapter extends GsonAdapter {

    private final TypeAdapter<JsonWriter, JsonReader> mComponentAdapter;
    private final Class<?> mComponentClass;

    public ArrayTypeAdapter(Class<?> componentClass, TypeAdapter<JsonWriter, JsonReader> mComponentAdapter) {
        this.mComponentClass = componentClass;
        this.mComponentAdapter = mComponentAdapter;
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        int length = Array.getLength(obj);
        sink.beginArray();
        for(int i = 0 ; i < length ; i ++){
            Object o = Array.get(obj, i);
            mComponentAdapter.write(sink, o);
        }
        sink.endArray();
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        List list = new ArrayList();
        source.beginArray();
        while (source.hasNext()){
            Object ele = mComponentAdapter.read(source);
            list.add(ele);
        }
        source.endArray();
        return list.toArray((Object[]) Array.newInstance(mComponentClass, list.size()));
    }
}
