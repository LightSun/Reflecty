package com.heaven7.java.reflecty.gson.adapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ByteAdapter extends GsonAdapter {
    @Override
    public int write(JsonWriter sink, Object obj) throws IOException {
        sink.value((Byte)obj);
        return -1;
    }
    @Override
    public Object read(JsonReader source) throws IOException {
        String str = source.nextString();
        if(str != null){
            return Byte.valueOf(str);
        }
        return null;
    }
}
