package com.heaven7.java.reflecty.iota;


/**
 * the collection or map plugin
 * @author heaven7
 * @param <Out> the output type, used to write
 * @param <In>  the input type ,used to read
 *  @since 1.0.3
 */
public abstract class IotaPlugin<Out, In> {

    public final Class<?> type;

    /**
     * create plugin for collection or map.
     * @param type the collection or map class
     *                  for map, this is key adapter
     */
    protected IotaPlugin(Class<?> type) {
        this.type = type;
    }

    /**
     * indicate that this plugin can handle child class or not. for example: if type = List.class. if this
     * method return true. it means. any child class of List. can be handled in this plugin.
     * @return true if can process child type or not. default is false.
     */
    public boolean canProcessChild(){
        return false;
    }
}