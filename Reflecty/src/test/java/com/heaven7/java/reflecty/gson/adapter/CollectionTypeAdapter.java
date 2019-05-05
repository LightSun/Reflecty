package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.TypeAdapter;
import com.heaven7.java.reflecty.TypeAdapterContext;

import java.io.IOException;
import java.util.Collection;

public final class CollectionTypeAdapter extends GsonAdapter {

    private final TypeAdapter<JsonWriter, JsonReader> mComponentAdapter;
    private final TypeAdapterContext mContext;
    private final Class<?> mCollClazz;

    public CollectionTypeAdapter(TypeAdapterContext mContext, Class<?> collClazz, TypeAdapter<JsonWriter, JsonReader> mComponentAdapter) {
        this.mContext = mContext;
        this.mCollClazz = collClazz;
        this.mComponentAdapter = mComponentAdapter;
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        Collection coll = (Collection) obj;
        sink.beginArray();
        for (Object element : coll){
            mComponentAdapter.write(sink, element);
        }
        sink.endArray();
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        Collection coll = mContext.createCollection(mCollClazz);
        source.beginArray();
        while (source.hasNext()){
            Object ele = mComponentAdapter.read(source);
            coll.add(ele);
        }
        source.endArray();
        return coll;
    }
}
