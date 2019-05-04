package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Collection;

public class CollectionTypeAdapter extends GsonAdapter {

    @Override
    public int write(JsonWriter sink, String property, Object obj) throws IOException {
        Collection coll = (Collection) obj;
        sink.beginArray();

        sink.endArray();
        return 0;
    }

    @Override
    public Object read(JsonReader source, String property) throws IOException {
        return null;
    }
}
