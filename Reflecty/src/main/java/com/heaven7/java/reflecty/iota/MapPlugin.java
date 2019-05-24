package com.heaven7.java.reflecty.iota;

import java.util.Map;

/**
 * the map plugin
 * @author heaven7
 * @param <Out> the output type, used to write
 * @param <In>  the input type ,used to read
 *  @since 1.0.3
 */
public abstract class MapPlugin<Out, In> extends Plugin<Out, In> {

     public MapPlugin(Class<?> type, TypeAdapter<Out, In> adapter1, TypeAdapter<Out, In> adapter2) {
         super(type, adapter1, adapter2);
     }

    /**
     * create map for target object. eg: sparse array to map
     * @param obj the object. can be sparse array and etc. null means create default map.
     * @return the map.
     */
     public abstract Map<?,?> createMap(Object obj);
 }
