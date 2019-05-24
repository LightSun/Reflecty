package com.heaven7.java.reflecty.iota;


/**
 * the collection plugin
 * @author heaven7
 * @param <Out> the output type, used to write
 * @param <In>  the input type ,used to read
 *  @since 1.0.3
 */
public abstract class Plugin<Out, In> {

    public final Class<?> type;
    public final TypeAdapter<Out, In> adapter1;
    public final TypeAdapter<Out, In> adapter2;

    protected Plugin(Class<?> type, TypeAdapter<Out, In> adapter1, TypeAdapter<Out, In> adapter2) {
        this.type = type;
        this.adapter1 = adapter1;
        this.adapter2 = adapter2;
    }
}