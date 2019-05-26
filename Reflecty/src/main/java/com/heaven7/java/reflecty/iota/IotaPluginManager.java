package com.heaven7.java.reflecty.iota;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.reflecty.ReflectyContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * the iota plugin manager
 * @param <Out> the output type
 * @param <In> the input type
 * @since 1.0.3
 * @author heaven7
 */
/*public*/ class IotaPluginManager<Out, In> implements ReflectyContext {

    private final BasicTypeAdapterProvider<Out, In> mProvider;
    private List<CollectionIotaPlugin<Out, In>> mCollectionPlugins;
    private List<MapIotaPlugin<Out, In>> mMapPlugins;

    public IotaPluginManager(BasicTypeAdapterProvider<Out, In> mProvider) {
        this.mProvider = mProvider;
    }

    /**
     * add an 'iota' plugin. which used to judge/create the self collection and map.
     * @param plugin the iota plugin
     */
    public void addIotaPlugin(IotaPlugin<Out, In> plugin){
        if(plugin instanceof CollectionIotaPlugin){
            if(mCollectionPlugins == null){
                mCollectionPlugins = new ArrayList<>(5);
            }
            mCollectionPlugins.add((CollectionIotaPlugin<Out, In>) plugin);
        }else if(plugin instanceof MapIotaPlugin){
            if(mMapPlugins == null){
                mMapPlugins = new ArrayList<>(5);
            }
            mMapPlugins.add((MapIotaPlugin<Out, In>) plugin);
        }else {
            throw new UnsupportedOperationException("only support collection and map iota-plugin.");
        }
    }
    public TypeAdapter<Out, In> getKeyAdapter(Class<?> type) {
        if(Predicates.isEmpty(mMapPlugins)){
            return null;
        }
        //first  ==
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.type == type){
                return plugin.getKeyAdapter(mProvider, type);
            }
        }
        //second. child
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return plugin.getKeyAdapter(mProvider, type);
            }
        }
        return null;
    }
    public TypeAdapter<Out, In> getValueAdapter(Class<?> type) {
        if(Predicates.isEmpty(mMapPlugins)){
            return null;
        }
        //first  ==
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.type == type){
                return plugin.getValueAdapter(mProvider, type);
            }
        }
        //second. child
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return plugin.getValueAdapter(mProvider, type);
            }
        }
        return null;
    }
    public TypeAdapter<Out, In> getElementAdapter(Class<?> type) {
        if(Predicates.isEmpty(mCollectionPlugins)){
            return null;
        }
        //first ==
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.type == type){
                return  plugin.getElementAdapter(mProvider, type);
            }
        }
        //second. child
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return  plugin.getElementAdapter(mProvider, type);
            }
        }
        return null;
    }

    //-----------------------------------------------------------------

    @Override
    public Object newInstance(Class<?> clazz) {
        return null;
    }
    @Override
    public boolean isMap(Class<?> type) {
        if(Predicates.isEmpty(mMapPlugins)){
            return false;
        }
        //first  ==
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.type == type){
                return true;
            }
        }
        //second. child
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return true;
            }
        }
        return false;
    }
    @Override
    public Map createMap(Class<?> type) {
        if(Predicates.isEmpty(mMapPlugins)){
            return null;
        }
        //first  ==
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.type == type){
                return plugin.createMap(type);
            }
        }
        //second. child
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return plugin.createMap(type);
            }
        }
        return null;
    }
    @Override
    public Map getMap(Object obj) {
        if(Predicates.isEmpty(mMapPlugins)){
            return null;
        }
        Class<?> type = obj.getClass();
        //first  ==
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.type == type){
                return plugin.createMap(obj);
            }
        }
        //second. child
        for (MapIotaPlugin<Out, In> plugin : mMapPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return plugin.createMap(obj);
            }
        }
        return null;
    }
    @Override
    public boolean isCollection(Class<?> type) {
        if(Predicates.isEmpty(mCollectionPlugins)){
            return false;
        }
        //first  ==
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.type == type){
                return true;
            }
        }
        //second. child
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return true;
            }
        }
        return false;
    }
    @Override
    public Collection createCollection(Class<?> type) {
        if(Predicates.isEmpty(mCollectionPlugins)){
            return null;
        }
        //first  ==
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.type == type){
                return plugin.createCollection(type);
            }
        }
        //second. child
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return plugin.createCollection(type);
            }
        }
        return null;
    }
    @Override
    public Collection getCollection(Object obj) {
        if(Predicates.isEmpty(mCollectionPlugins)){
            return null;
        }
        Class<?> type = obj.getClass();
        //first  ==
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.type == type){
                return plugin.createCollection(obj);
            }
        }
        //second. child
        for (CollectionIotaPlugin<Out, In> plugin : mCollectionPlugins){
            if(plugin.canProcessChild() && plugin.type.isAssignableFrom(type)){
                return plugin.createCollection(obj);
            }
        }
        return null;
    }
}
