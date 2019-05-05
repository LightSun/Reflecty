package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.TypeAdapter;

import java.io.IOException;

public class StringAdapter extends GsonBasicAdapter {

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        sink.value((String) obj);
        return -1;
    }
    @Override
    public Object read(JsonReader source) throws IOException {
        return source.nextString();
    }
    @Override
    protected TypeAdapter<JsonWriter, JsonReader> onCreateNameTypeAdapter() {
        return new NameTypeAdapter();
    }

    private static class NameTypeAdapter extends GsonAdapter{
        @Override
        public int write(JsonWriter sink, Object obj) throws IOException {
            if(obj != null){
                sink.name(obj.toString());
            }
            return -1;
        }
        @Override
        public Object read(JsonReader source) throws IOException {
            return source.nextName();
        }
    }
}
