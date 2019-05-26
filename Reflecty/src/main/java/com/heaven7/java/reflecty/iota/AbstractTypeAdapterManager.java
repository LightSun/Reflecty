package com.heaven7.java.reflecty.iota;


import com.heaven7.java.base.util.Predicates;
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

    private List<IotaPlugin<Out, In>> mCollectionPlugins;

    public AbstractTypeAdapterManager() {
        this(new SimpleReflectyContext());
    }

    public AbstractTypeAdapterManager(ReflectyContext context) {
        this.context = new GroupReflectyContext(new PluginReflectyContext(), context);
    }

    /**
     * add an 'iota' plugin. which used to judge/create the self collection and map.
     * @param plugin the iota plugin
     * @since 1.0.3
     */
    public void addIotaPlugin(IotaPlugin<Out, In> plugin){
        if(mCollectionPlugins == null){
            mCollectionPlugins = new ArrayList<>(5);
        }
        mCollectionPlugins.add(plugin);
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
        if(Predicates.isEmpty(mCollectionPlugins)){
            return null;
        }
        for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin instanceof MapIotaPlugin){
                if(plugin.type == type || (plugin.canProcessChild() && plugin.type.isAssignableFrom(type))){
                    return ((MapIotaPlugin<Out, In>) plugin).getKeyAdapter(this, type);
                }
            }
        }
        return null;
    }
    @Override
    public TypeAdapter<Out, In> getValueAdapter(Class<?> type) {
        if(Predicates.isEmpty(mCollectionPlugins)){
            return null;
        }
        for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin instanceof MapIotaPlugin){
                if(plugin.type == type || (plugin.canProcessChild() && plugin.type.isAssignableFrom(type))){
                    return ((MapIotaPlugin<Out, In>) plugin).getValueAdapter(this, type);
                }
            }
        }
        return null;
    }
    @Override
    public TypeAdapter<Out, In> getElementAdapter(Class<?> type) {
        if(Predicates.isEmpty(mCollectionPlugins)){
            return null;
        }
        for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin instanceof CollectionIotaPlugin){
                if(plugin.type == type || (plugin.canProcessChild() && plugin.type.isAssignableFrom(type))){
                    return ((CollectionIotaPlugin<Out, In>) plugin).getElementAdapter(this, type);
                }
            }
        }
        return null;
    }

    private class PluginReflectyContext implements ReflectyContext{
        @Override
        public Object newInstance(Class<?> clazz) {
            return null;
        }
        @Override
        public boolean isMap(Class<?> type) {
            if(Predicates.isEmpty(mCollectionPlugins)){
                return false;
            }
            for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
                if(plugin instanceof MapIotaPlugin){
                    if(plugin.type == type || (plugin.canProcessChild() && plugin.type.isAssignableFrom(type))){
                        return true;
                    }
                }
            }
            return false;
        }
        @Override
        public Map createMap(Class<?> clazz) {
            if(Predicates.isEmpty(mCollectionPlugins)){
                return null;
            }
            for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
                if(plugin instanceof MapIotaPlugin){
                    if(plugin.type == clazz || (plugin.canProcessChild() && plugin.type.isAssignableFrom(clazz))){
                        return ((MapIotaPlugin<Out, In>) plugin).createMap(clazz);
                    }
                }
            }
            return null;
        }
        @Override
        public Map getMap(Object obj) {
            if(Predicates.isEmpty(mCollectionPlugins)){
                return null;
            }
            Class<?> clazz = obj.getClass();
            for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
                if(plugin instanceof MapIotaPlugin){
                    if(plugin.type == clazz || (plugin.canProcessChild() && plugin.type.isAssignableFrom(clazz))){
                        return ((MapIotaPlugin<Out, In>) plugin).createMap(obj);
                    }
                }
            }
            return null;
        }
        @Override
        public boolean isCollection(Class<?> clazz) {
            if(Predicates.isEmpty(mCollectionPlugins)){
                return false;
            }
            for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
                if(plugin instanceof CollectionIotaPlugin){
                    if(plugin.type == clazz || (plugin.canProcessChild() && plugin.type.isAssignableFrom(clazz))){
                        return true;
                    }
                }
            }
            return false;
        }
        @Override
        public Collection createCollection(Class<?> clazz) {
            if(Predicates.isEmpty(mCollectionPlugins)){
                return null;
            }
            for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
                if(plugin instanceof CollectionIotaPlugin){
                    if(plugin.type == clazz || (plugin.canProcessChild() && plugin.type.isAssignableFrom(clazz))){
                        return ((CollectionIotaPlugin<Out, In>) plugin).createCollection(clazz);
                    }
                }
            }
            return null;
        }
        @Override
        public Collection getCollection(Object obj) {
            if(Predicates.isEmpty(mCollectionPlugins)){
                return null;
            }
            Class<?> clazz = obj.getClass();
            for (IotaPlugin<Out, In> plugin : mCollectionPlugins){
                if(plugin instanceof CollectionIotaPlugin){
                    if(plugin.type == clazz || (plugin.canProcessChild() && plugin.type.isAssignableFrom(clazz))){
                        return ((CollectionIotaPlugin<Out, In>) plugin).createCollection(obj);
                    }
                }
            }
            return null;
        }
    }

}
