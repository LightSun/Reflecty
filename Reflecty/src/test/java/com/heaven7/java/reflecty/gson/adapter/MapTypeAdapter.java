package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.BasicTypeAdapter;
import com.heaven7.java.reflecty.TypeAdapter;
import com.heaven7.java.reflecty.TypeAdapterContext;
import com.heaven7.java.reflecty.Wrapper;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public final class MapTypeAdapter extends GsonAdapter {

    private final TypeAdapterContext mContext;
    private final Class<?> mMapClass;
    private final TypeAdapter<JsonWriter, JsonReader> mKeyAdapter;
    private final TypeAdapter<JsonWriter, JsonReader> mValueAdapter;

    public MapTypeAdapter(TypeAdapterContext mContext, Class<?> mMapClass, TypeAdapter<JsonWriter, JsonReader> mKeyAdapter, TypeAdapter<JsonWriter, JsonReader> mValueAdapter) {
        this.mContext = mContext;
        this.mMapClass = mMapClass;
        this.mKeyAdapter = mKeyAdapter;
        this.mValueAdapter = mValueAdapter;
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        Map map = mContext.getMap(obj);
        TypeAdapter<JsonWriter, JsonReader> key = mKeyAdapter instanceof BasicTypeAdapter ?
                ((BasicTypeAdapter) mKeyAdapter).getNameTypeAdapter() : mKeyAdapter;
        Set<Map.Entry> set = map.entrySet();
        sink.beginObject();
        for (Map.Entry en : set){
            key.write(sink, en.getKey());
            mValueAdapter.write(sink, en.getValue());
        }
        sink.endObject();
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        Map map = mContext.createMap(mMapClass);
        TypeAdapter<JsonWriter, JsonReader> keyTA = mKeyAdapter instanceof BasicTypeAdapter ?
                ((BasicTypeAdapter) mKeyAdapter).getNameTypeAdapter() : mKeyAdapter;
        source.beginObject();
        while (source.hasNext()){
            Object key = keyTA.read(source);
            Object value = mValueAdapter.read(source);
            map.put(key, value);
        }
        source.endObject();
        return map instanceof Wrapper ? ((Wrapper) map).unwrap() : map;
    }
}
