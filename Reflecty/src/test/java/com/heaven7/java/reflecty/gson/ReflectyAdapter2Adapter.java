package com.heaven7.java.reflecty.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArrayDelegate;

import java.io.IOException;

public class ReflectyAdapter2Adapter<T> extends TypeAdapter<T> {

    private final com.heaven7.java.reflecty.TypeAdapter<JsonWriter, JsonReader> mta;

    public ReflectyAdapter2Adapter(com.heaven7.java.reflecty.TypeAdapter<JsonWriter, JsonReader> mta) {
        this.mta = mta;
    }
    @Override
    public void write(JsonWriter out, T value) throws IOException {
        mta.write(out, value);
    }

    @Override @SuppressWarnings("unchecked")
    public T read(JsonReader in) throws IOException {
        return (T) mta.read(in);
    }
}
