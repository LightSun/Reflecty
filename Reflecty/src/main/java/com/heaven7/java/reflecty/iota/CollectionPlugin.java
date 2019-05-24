package com.heaven7.java.reflecty.iota;

import java.util.Collection;

/**
 * the collection plugin
 * @author heaven7
 * @param <Out> the output type, used to write
 * @param <In>  the input type ,used to read
 *  @since 1.0.3
 */
public abstract class CollectionPlugin<Out, In> extends Plugin<Out, In> {

    /**
     * create collection plugin
     * @param type the collection type.
     * @param adapter1 the element adapter of target. if it is not explicit. just make it be null.
     */
    public CollectionPlugin(Class<?> type, TypeAdapter<Out, In> adapter1) {
        super(type, adapter1, null);
    }

    /**
     * create collection by target object.
     * @param obj the object which will be wrap to collection. if null means create default collection.
     * @return the collection
     */
    public abstract Collection<?> createCollection(Object obj);


}
