package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.BasicTypeAdapter;
import com.heaven7.java.reflecty.TypeAdapter;
import com.heaven7.java.reflecty.ReflectyContext;
import com.heaven7.java.reflecty.Wrapper;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class MapTypeAdapter extends GsonAdapter {

    private final ReflectyContext mContext;
    private final Class<?> mMapClass;
    private final TypeAdapter<JsonWriter, JsonReader> mKeyAdapter;
    private final TypeAdapter<JsonWriter, JsonReader> mValueAdapter;

    public MapTypeAdapter(ReflectyContext mContext, Class<?> mMapClass, TypeAdapter<JsonWriter, JsonReader> mKeyAdapter, TypeAdapter<JsonWriter, JsonReader> mValueAdapter) {
        this.mContext = mContext;
        this.mMapClass = mMapClass;
        this.mKeyAdapter = mKeyAdapter;
        this.mValueAdapter = mValueAdapter;
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        Map map = mContext.getMap(obj);
        if(mKeyAdapter instanceof BasicTypeAdapter){
            TypeAdapter<JsonWriter, JsonReader> key = ((BasicTypeAdapter) mKeyAdapter).getNameTypeAdapter();
            Set<Map.Entry> set = map.entrySet();
            //if mKeyAdapter is not base type. it doesn't support nested object. (Gson limit)
            sink.beginObject();
            for (Map.Entry en : set){
                key.write(sink, en.getKey());
                mValueAdapter.write(sink, en.getValue());
            }
            sink.endObject();
        }else {
            TypeAdapter<JsonWriter, JsonReader> key = mKeyAdapter;
            Set<Map.Entry> set = map.entrySet();
            sink.beginArray();
            for (Map.Entry en : set){
                key.write(sink, en.getKey());
                mValueAdapter.write(sink, en.getValue());
            }
            sink.endArray();
        }
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        Map map = mContext.createMap(mMapClass);
        if(mKeyAdapter instanceof BasicTypeAdapter){
            TypeAdapter<JsonWriter, JsonReader> keyTA = ((BasicTypeAdapter) mKeyAdapter).getNameTypeAdapter();
            source.beginObject();
            while (source.hasNext()){
                Object key = keyTA.read(source);
                Object value = mValueAdapter.read(source);
                map.put(key, value);
            }
            source.endObject();
        }else {
            TypeAdapter<JsonWriter, JsonReader> keyTA = mKeyAdapter;
            source.beginArray();
            while (source.hasNext()){
                Object key = keyTA.read(source);
                Object value = mValueAdapter.read(source);
                map.put(key, value);
            }
            source.endArray();
        }
        return map instanceof Wrapper ? ((Wrapper) map).unwrap() : map;
    }
}
