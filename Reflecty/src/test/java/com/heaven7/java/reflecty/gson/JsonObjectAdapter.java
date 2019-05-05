package com.heaven7.java.reflecty.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.gson.adapter.GsonAdapter;

import java.io.IOException;

public class JsonObjectAdapter extends GsonAdapter {

    private final TypeAdapter<Object> ta;

    @SuppressWarnings("unchecked")
    public JsonObjectAdapter(TypeAdapter<?> ta) {
        this.ta = (TypeAdapter<Object>) ta;
    }

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        ta.write(sink, obj);
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        return ta.read(source);
    }
}
