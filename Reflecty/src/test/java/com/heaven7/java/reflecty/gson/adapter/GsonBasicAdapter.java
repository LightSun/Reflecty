package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.iota.BasicTypeAdapter;

public abstract class GsonBasicAdapter extends BasicTypeAdapter<JsonWriter, JsonReader> {

    @Override
    public int evaluateSize(Object obj) {
        return -1;
    }
}
