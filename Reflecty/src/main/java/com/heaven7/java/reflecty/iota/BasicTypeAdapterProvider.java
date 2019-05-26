package com.heaven7.java.reflecty.iota;

/**
 * the basic type adapter provider
 * @param <Out> the out put
 * @param <In> the input
 * @since 1.0.3
 */
public interface BasicTypeAdapterProvider<Out, In> {

    /**
     * get the base type adapter
     * @param baseType the base type class. see java basic-types.
     * @return the base {@linkplain TypeAdapter}.
     */
    TypeAdapter<Out, In> getBasicTypeAdapter(Class<?> baseType);
}
