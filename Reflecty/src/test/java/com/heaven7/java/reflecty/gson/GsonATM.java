package com.heaven7.java.reflecty.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.AbstractTypeAdapterManager;
import com.heaven7.java.reflecty.TypeAdapter;
import com.heaven7.java.reflecty.gson.adapter.ArrayTypeAdapter;
import com.heaven7.java.reflecty.gson.adapter.CollectionTypeAdapter;
import com.heaven7.java.reflecty.gson.adapter.MapTypeAdapter;
import com.heaven7.java.reflecty.gson.adapter.ObjectTypeAdapter;

public final class GsonATM extends AbstractTypeAdapterManager<JsonWriter, JsonReader> {

    @Override
    public TypeAdapter<JsonWriter, JsonReader> createCollectionTypeAdapter(Class<?> collectionClass, TypeAdapter<JsonWriter, JsonReader> componentAdapter) {
        return new CollectionTypeAdapter(getTypeAdapterContext(), collectionClass, componentAdapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> createArrayTypeAdapter(Class<?> componentClass, TypeAdapter<JsonWriter, JsonReader> componentAdapter) {
        return new ArrayTypeAdapter(componentClass, componentAdapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> createMapTypeAdapter(Class<?> mapClazz, TypeAdapter<JsonWriter, JsonReader> keyAdapter,
                                                                    TypeAdapter<JsonWriter, JsonReader> valueAdapter) {
        return new MapTypeAdapter(getTypeAdapterContext(), mapClazz, keyAdapter, valueAdapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> createObjectTypeAdapter(Class<?> objectClazz, float applyVersion) {
        return new ObjectTypeAdapter(this, objectClazz, applyVersion);
    }
}
