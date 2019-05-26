package com.heaven7.java.reflecty.iota;

import com.heaven7.java.reflecty.ReflectyContext;

import java.util.Collection;

/**
 * the collection plugin
 * @author heaven7
 * @param <Out> the output type, used to write
 * @param <In>  the input type ,used to read
 *  @since 1.0.3
 */
public abstract class CollectionIotaPlugin<Out, In> extends IotaPlugin<Out, In> {

    /**
     * create collection plugin
     * @param type the collection type.
     */
    public CollectionIotaPlugin(Class<?> type) {
        super(type);
    }

    /**
     * get the element type adapter for collection class type. this often used when you want to use a self-type collection.
     * can non-extend {@linkplain java.util.Collection}. see {@linkplain ReflectyContext#isCollection(Class)} and {@linkplain ReflectyContext#createCollection(Class)}.
     * @param type the collection class. can be any similar collection. like {@linkplain java.util.Collection}
     * @param provider the type adapter provider. used to get basic type {@linkplain TypeAdapter}.
     * @return the element type adapter. or null if you haven't use self-type collection.
     */
    public abstract TypeAdapter<Out, In> getElementAdapter(BasicTypeAdapterProvider<Out, In> provider, Class<?> type);

    /**
     * create collection by target object. that means wrap target object to collection.
     * @param obj the object which will be wrap to collection.
     * @return the collection
     */
    public abstract Collection<?> createCollection(Object obj);

    /**
     * create default collection for target collection class.
     * @param clazz the similar collection class
     * @return the collection
     */
    public abstract Collection<?> createCollection(Class<?> clazz);
}
