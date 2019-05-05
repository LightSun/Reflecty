package com.heaven7.java.reflecty.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArrayDelegate;

import java.io.IOException;

public class SparseArrayAdapter extends TypeAdapter<SparseArrayDelegate<String>> {

    private final com.heaven7.java.reflecty.TypeAdapter<JsonWriter, JsonReader> mta;

    public SparseArrayAdapter(com.heaven7.java.reflecty.TypeAdapter<JsonWriter, JsonReader> mta) {
        this.mta = mta;
    }
    @Override
    public void write(JsonWriter out, SparseArrayDelegate<String> value) throws IOException {
        mta.write(out, value);
    }
    @Override
    public SparseArrayDelegate<String> read(JsonReader in) throws IOException {
        return (SparseArrayDelegate<String>) mta.read(in);
    }
}
