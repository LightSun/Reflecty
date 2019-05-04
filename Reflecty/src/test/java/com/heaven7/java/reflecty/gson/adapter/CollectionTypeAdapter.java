package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Collection;

public class CollectionTypeAdapter extends GsonAdapter {

    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        Collection coll = (Collection) obj;
        sink.beginArray();

        sink.endArray();
        return 0;
    }

    @Override
    public Object read(JsonReader source) throws IOException {
        return null;
    }
}
