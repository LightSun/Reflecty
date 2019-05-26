package com.heaven7.java.reflecty.iota;


import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.reflecty.*;
import com.heaven7.java.reflecty.member.BaseMemberProxy;
import com.heaven7.java.reflecty.utils.Pair;

import java.lang.reflect.Type;
import java.util.*;

/**
 * the abstract type adapter manager. sub class must register the base {@linkplain TypeAdapter}
 * for 'byte, short, int, long, float,long, char, boolean ' and their wrapper type, also need string type.
 * @param <Out> the output type, used to write
 * @param <In> the input type ,used to read
 * @author heaven7
 */
public abstract class AbstractTypeAdapterManager<Out, In> implements ITypeAdapterManager<Out, In> {

    private final ReflectyContext context;

    private final Map<TypeNode, Pair<Float, TypeAdapter<Out, In>>> mAdapterMap = new HashMap<>();
    private final SparseArrayDelegate<TypeAdapter<Out, In>> mBaseAdapterMap
            = SparseFactory.newSparseArray(10);

    private final IotaPluginManager<Out, In> mIotaPM = new IotaPluginManager<>(this);

    public AbstractTypeAdapterManager() {
        this(new SimpleReflectyContext());
    }

    public AbstractTypeAdapterManager(ReflectyContext context) {
        this.context = new GroupReflectyContext(mIotaPM, context);
    }

    /**
     * add an 'iota' plugin. which used to judge/create the self collection and map.
     * @param plugin the iota plugin
     * @since 1.0.3
     */
    public void addIotaPlugin(IotaPlugin<Out, In> plugin){
        mIotaPM.addIotaPlugin(plugin);
    }

    @Override
    public final ReflectyContext getReflectyContext() {
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
        return mIotaPM.getKeyAdapter(type);
    }
    @Override
    public TypeAdapter<Out, In> getValueAdapter(Class<?> type) {
        return mIotaPM.getValueAdapter(type);
    }
    @Override
    public TypeAdapter<Out, In> getElementAdapter(Class<?> type) {
        return mIotaPM.getElementAdapter(type);
    }
}
