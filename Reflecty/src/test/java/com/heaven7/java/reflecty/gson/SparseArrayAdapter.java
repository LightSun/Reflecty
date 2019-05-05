package com.heaven7.java.reflecty.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArrayDelegate;

public class SparseArrayAdapter extends ReflectyAdapter2Adapter<SparseArrayDelegate<String>> {

    public SparseArrayAdapter(com.heaven7.java.reflecty.iota.TypeAdapter<JsonWriter, JsonReader> mta) {
        super(mta);
    }
}
