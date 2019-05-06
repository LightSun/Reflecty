package com.heaven7.java.reflecty.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Type;

public class ReflectyTypeAdapterFactory implements TypeAdapterFactory {

    private final GsonATM atm;
    private final float version;

    public ReflectyTypeAdapterFactory(GsonATM atm, float version) {
        this.atm = atm;
        this.version = version;
    }
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> tt) {
        Type type = tt.getType();
        com.heaven7.java.reflecty.iota.TypeAdapter<JsonWriter, JsonReader> ta;
        if(type instanceof Class){
            Class<?> clazz = (Class<?>) type;
            if(atm.getReflectyContext().isCollection(clazz) || atm.getReflectyContext().isMap(clazz)){
                return null;
            }
            ta = atm.createObjectTypeAdapter(clazz, version);
        }else {
            ta = com.heaven7.java.reflecty.iota.TypeAdapter.ofType(type, atm, version);
        }
        return new ReflectyAdapter2Adapter<T>(ta);
    }
}
