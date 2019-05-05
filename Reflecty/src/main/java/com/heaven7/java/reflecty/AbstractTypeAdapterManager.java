package com.heaven7.java.reflecty;


import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.reflecty.utils.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * the abstract type adapter manager. sub class must register the base {@linkplain TypeAdapter}
 * for 'byte, short, int, long, float,long, char, boolean ' and their wrapper type, also need string type.
 * @param <Out> the output type, used to write
 * @param <In> the input type ,used to read
 * @author heaven7
 */
public abstract class AbstractTypeAdapterManager<Out, In> implements ITypeAdapterManager<Out, In>{

    private final TypeAdapterContext context;

    private final Map<TypeNode, Pair<Float,TypeAdapter<Out, In>>> mAdapterMap = new HashMap<>();
    private final SparseArrayDelegate<TypeAdapter<Out, In>> mBaseAdapterMap
            = SparseFactory.newSparseArray(10);

    public AbstractTypeAdapterManager() {
        this(new SimpleTypeAdapterContext());
    }

    public AbstractTypeAdapterManager(TypeAdapterContext context) {
        this.context = context;
    }

    @Override
    public TypeAdapterContext getTypeAdapterContext() {
        return context;
    }

   /* @Override
    public TypeAdapter<Out, In> getTypeAdapter(Type type, float applyVersion) {
        return getTypeAdapter($ReflectyTypes.getTypeNode(type), applyVersion);
    }*/
    @Override
    public TypeAdapter<Out, In> getTypeAdapter(TypeNode genericNode, float applyVersion) {
        Pair<Float, TypeAdapter<Out, In>> pair = mAdapterMap.get(genericNode);
        if(pair != null && applyVersion >= pair.key){
            return pair.value;
        }
        return null;
    }
    @Override
    public void registerTypeAdapter(Type type, float version, TypeAdapter<Out, In> adapter) {
        mAdapterMap.put($ReflectyTypes.getTypeNode(type), new Pair<>(version, adapter));
    }
    @Override
    public void registerBasicTypeAdapter(Class<?> baseType, TypeAdapter<Out, In> adapter) {
        mBaseAdapterMap.put(BaseMemberProxy.parseType(baseType), adapter);
    }
    @Override
    public TypeAdapter<Out, In> getBasicTypeAdapter(Class<?> baseType) {
        return mBaseAdapterMap.get(BaseMemberProxy.parseType(baseType));
    }

    @Override
    public TypeAdapter<Out, In> getKeyAdapter(Class<?> type) {
        if(SparseArrayDelegate.class.isAssignableFrom(type)){
            return getBasicTypeAdapter(int.class);
        }
        return null;
    }
    @Override
    public TypeAdapter<Out, In> getValueAdapter(Class<?> type) {
        return null;
    }
    @Override
    public TypeAdapter<Out, In> getElementAdapter(Class<?> type) {
        return null;
    }
}
