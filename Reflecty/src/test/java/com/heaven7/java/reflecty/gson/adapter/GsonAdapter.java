package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.iota.TypeAdapter;

public abstract class GsonAdapter extends TypeAdapter<JsonWriter, JsonReader> {

    @Override
    public final int evaluateSize(Object obj) {
        return -1;
    }
}
