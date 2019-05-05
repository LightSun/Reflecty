package com.heaven7.java.reflecty.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.AbstractTypeAdapterManager;
import com.heaven7.java.reflecty.TypeAdapter;
import com.heaven7.java.reflecty.gson.adapter.*;

public final class GsonATM extends AbstractTypeAdapterManager<JsonWriter, JsonReader> {

    public GsonATM() {
        registerAdapters(byte.class, Byte.class);
        registerAdapters(short.class, Short.class);
        registerAdapters(int.class, Integer.class);
        registerAdapters(long.class, Long.class);
        registerAdapters(float.class, Float.class);
        registerAdapters(double.class, Double.class);
        registerAdapters(boolean.class, Boolean.class);

        CharAdapter ca = new CharAdapter();
        registerBasicTypeAdapter(char.class, ca);
        registerBasicTypeAdapter(Character.class, ca);

        registerBasicTypeAdapter(String.class, new StringAdapter());
    }

    private <T> void registerAdapters(Class<?> base, Class<T> wrap){
        BaseCommonAdapter<T> byteAdapter = new BaseCommonAdapter<>(wrap);
        registerBasicTypeAdapter(base, byteAdapter);
        registerBasicTypeAdapter(wrap, byteAdapter);
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> createCollectionTypeAdapter(Class<?> collectionClass, TypeAdapter<JsonWriter, JsonReader> componentAdapter) {
        return new CollectionTypeAdapter(getReflectyContext(), collectionClass, componentAdapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> createArrayTypeAdapter(Class<?> componentClass, TypeAdapter<JsonWriter, JsonReader> componentAdapter) {
        return new ArrayTypeAdapter(componentClass, componentAdapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> createMapTypeAdapter(Class<?> mapClazz, TypeAdapter<JsonWriter, JsonReader> keyAdapter,
                                                                    TypeAdapter<JsonWriter, JsonReader> valueAdapter) {
        return new MapTypeAdapter(getReflectyContext(), mapClazz, keyAdapter, valueAdapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> createObjectTypeAdapter(Class<?> objectClazz, float applyVersion) {
        return new ObjectTypeAdapter(this, objectClazz, applyVersion);
    }
}
