package com.heaven7.java.reflecty.utils;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.reflecty.ReflectyContext;
import com.heaven7.java.reflecty.TypeNode;
import com.heaven7.java.reflecty.iota.ITypeAdapterManager;
import com.heaven7.java.reflecty.iota.TypeAdapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * the input output type adapter utils.
 * @author heaven7
 * @since 1.0.1
 */
public final class IotaUtils {

    /**
     * get the type adapter from target type node and type adapter manager delegate.
     * @param <Out> the output type
     * @param <In> the input type
     * @param node the type node
     * @param delegate the type adapter manager delegate
     * @param applyVersion the expect version
     * @return the type adapter
     */
    public static <Out, In> TypeAdapter<Out, In> getTypeAdapter(TypeNode node, ITypeAdapterManager<Out, In> delegate, float applyVersion){
        if(node.isArray()){
            TypeNode subNode = node.getSubNode(0);
            return delegate.createArrayTypeAdapter(subNode.getTypeClass(0),
                    getTypeAdapter(subNode, delegate, applyVersion));
        }
        ReflectyContext context = delegate.getReflectyContext();
        Class<?> type = node.getRawClass();
        if(type != null){
            //dynamic register
            TypeAdapter<Out, In> typeAdapter = delegate.getTypeAdapter(node, applyVersion);
            if(typeAdapter != null){
                return typeAdapter;
            }
            //base types
            TypeAdapter<Out, In> adapter = delegate.getBasicTypeAdapter(type);
            if(adapter != null){
                return adapter;
            }
            if(Collection.class.isAssignableFrom(type) || context.isCollection(type)){
                TypeAdapter<Out, In> ta;
                if(node.getSubNodeCount() == 0){
                    ta = delegate.getElementAdapter(type);
                }else {
                    ta = getTypeAdapter(node.getSubNode(0), delegate, applyVersion);
                }
                if(ta == null){
                    throw new IllegalStateException("can't find target element adapter for collection class = " + type.getName());
                }
                return delegate.createCollectionTypeAdapter(type, ta);

            }else if(Map.class.isAssignableFrom(type) || context.isMap(type)){
                TypeAdapter<Out, In> key, value;

                int count = node.getSubNodeCount();
                switch (count){
                    case 0:
                        key = delegate.getKeyAdapter(type);
                        value = delegate.getValueAdapter(type);
                        break;
                    //often key type can be fixed. but value type not. like sparse array.
                    case 1:
                        key = delegate.getKeyAdapter(type);
                        value = getTypeAdapter(node.getSubNode(0), delegate, applyVersion);
                        break;

                    case 2:
                        key = getTypeAdapter(node.getSubNode(0), delegate, applyVersion);
                        value = getTypeAdapter(node.getSubNode(1), delegate, applyVersion);
                        break;

                    default:
                        throw new UnsupportedOperationException("sub node count for map must <= 2. but is " + count);
                }
                if(key == null){
                    throw new IllegalStateException("can't find target key adapter for map class = " + type.getName());
                }
                if(value == null){
                    throw new IllegalStateException("can't find target value adapter for map class = " + type.getName());
                }
                return delegate.createMapTypeAdapter(type, key, value);
            }else {
                return delegate.createObjectTypeAdapter(type, applyVersion);
            }
        }else {
            List<TypeNode> nodes = node.getVariableNodes();
            if(!Predicates.isEmpty(nodes)){
                return getTypeAdapter(nodes.get(0), delegate, applyVersion);
            }
            throw new UnsupportedOperationException("un-reach here");
        }
    }
}
