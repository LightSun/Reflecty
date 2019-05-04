package com.heaven7.java.reflecty.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.reflecty.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class GsonATM implements ITypeAdapterManager<JsonWriter, JsonReader>, TypeAdapterContext {

    private final Map<TypeNode, Pair<Float,TypeAdapter<JsonWriter, JsonReader>>> mAdapterMap = new HashMap<>();
    private final SparseArrayDelegate<TypeAdapter<JsonWriter, JsonReader>> mBaseAdapterMap
            = SparseFactory.newSparseArray(10);

    @Override
    public boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type) || SparseArrayDelegate.class.isAssignableFrom(type);
    }
    @Override
    public TypeAdapterContext getTypeAdapterContext() {
        return this;
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> getTypeAdapter(TypeNode genericNode, float applyVersion) {
        Pair<Float, TypeAdapter<JsonWriter, JsonReader>> pair = mAdapterMap.get(genericNode);
        if(pair != null && applyVersion >= pair.key){
            return pair.value;
        }
        return null;
    }

    @Override
    public void registerTypeAdapter(Type type, float version, TypeAdapter<JsonWriter, JsonReader> adapter) {
        mAdapterMap.put($ReflectyTypes.getTypeNode(type), new Pair<>(version, adapter));
    }

    @Override
    public void registerBasicTypeAdapter(Class<?> baseType, TypeAdapter<JsonWriter, JsonReader> adapter) {
        mBaseAdapterMap.put(BaseMemberProxy.parseType(baseType), adapter);
    }
    @Override
    public TypeAdapter<JsonWriter, JsonReader> getBasicTypeAdapter(Class<?> baseType) {
        return mBaseAdapterMap.get(BaseMemberProxy.parseType(baseType));
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> createCollectionTypeAdapter(Class<?> collectionClass, TypeAdapter<JsonWriter, JsonReader> componentAdapter) {
        return null;
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> createArrayTypeAdapter(Class<?> componentClass, TypeAdapter<JsonWriter, JsonReader> componentAdapter) {
        return null;
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> createMapTypeAdapter(Class<?> mapClazz, TypeAdapter<JsonWriter, JsonReader> keyAdapter,
                                                                    TypeAdapter<JsonWriter, JsonReader> valueAdapter) {
        return null;
    }

    @Override
    public TypeAdapter<JsonWriter, JsonReader> createObjectTypeAdapter(Class<?> objectClazz, float applyVersion) {
        return null;
    }
}
