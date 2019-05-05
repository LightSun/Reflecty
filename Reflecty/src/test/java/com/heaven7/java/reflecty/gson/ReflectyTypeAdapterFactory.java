package com.heaven7.java.reflecty.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ReflectyTypeAdapterFactory implements TypeAdapterFactory {

    private final GsonATM atm;
    private final float version;

    public ReflectyTypeAdapterFactory(GsonATM atm, float version) {
        this.atm = atm;
        this.version = version;
    }
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> tt) {
        com.heaven7.java.reflecty.TypeAdapter<JsonWriter, JsonReader> ta = com.heaven7.java.reflecty.TypeAdapter.ofType(
                tt.getType(), atm, version);
        return new ReflectyAdapter2Adapter<T>(ta);
    }
}
