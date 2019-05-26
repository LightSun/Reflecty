package com.heaven7.java.reflecty.iota;

import com.heaven7.java.reflecty.ReflectyContext;

import java.util.Map;

/**
 * the map plugin
 * @author heaven7
 * @param <Out> the output type, used to write
 * @param <In>  the input type ,used to read
 *  @since 1.0.3
 */
public abstract class MapIotaPlugin<Out, In> extends IotaPlugin<Out, In> {

     public MapIotaPlugin(Class<?> type) {
         super(type);
     }

    /**
     * create map for target object. eg: sparse array to map. that means wrap target object to map.
     * @param obj the object. can be sparse array and etc.
     * @return the map.
     */
     public abstract Map<?,?> createMap(Object obj);

    /**
     * create default map for class.
     * @param clazz the similar map class.
     * @return the map.
     */
    public abstract Map<?,?> createMap(Class<?> clazz);

    /**
     * get the key adapter for target map class type. this often used when you want to use a self-type map.
     * can non-extend {@linkplain Map}. see {@linkplain ReflectyContext#isMap(Class)} and {@linkplain ReflectyContext#createMap(Class)}.
     * @param provider the type adapter provider. used to get basic type {@linkplain TypeAdapter}.
     * @param type the class. can be {@linkplain com.heaven7.java.base.util.SparseArrayDelegate}.
     * @return the key type adapter
     */
    public abstract TypeAdapter<Out,In> getKeyAdapter(BasicTypeAdapterProvider<Out, In> provider, Class<?> type);
    /**
     * get the value adapter for target map class type. this often used when you want to use a self-type map.
     * can non-extend {@linkplain Map}. see {@linkplain ReflectyContext#isMap(Class)} and {@linkplain ReflectyContext#createMap(Class)}.
     * @param provider the type adapter provider. used to get basic type {@linkplain TypeAdapter}.
     * @param type the class . can be any class like {@linkplain Map}.
     * @return the value type adapter. or null if you haven't use self-type map.
     */
    public abstract TypeAdapter<Out,In> getValueAdapter(BasicTypeAdapterProvider<Out, In> provider, Class<?> type);
 }
