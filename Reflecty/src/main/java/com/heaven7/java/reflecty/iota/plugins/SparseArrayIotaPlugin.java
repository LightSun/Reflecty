package com.heaven7.java.reflecty.iota.plugins;

import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.reflecty.Wrapper;
import com.heaven7.java.reflecty.iota.BasicTypeAdapterProvider;
import com.heaven7.java.reflecty.iota.MapIotaPlugin;
import com.heaven7.java.reflecty.iota.TypeAdapter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * the sparse array iota-plugin.
 * @param <Out> the output type
 * @param <In> the input type
 * @since 1.0.4
 * @author heaven7
 */
public final class SparseArrayIotaPlugin<Out, In> extends MapIotaPlugin<Out, In>  {

    public SparseArrayIotaPlugin() {
        super(SparseArrayDelegate.class);
    }

    @Override
    public boolean canProcessChild() {
        return true;
    }
    @Override @SuppressWarnings("unchecked")
    public Map<?, ?> createMap(Object obj) {
        return new SparseArrayMap((SparseArrayDelegate) obj);
    }
    @Override
    public Map<?, ?> createMap(Class<?> clazz) {
        return new SparseArrayMap<>(SparseFactory.newSparseArray(10));
    }
    @Override
    public TypeAdapter<Out, In> getKeyAdapter(BasicTypeAdapterProvider<Out, In> provider, Class<?> type) {
        return provider.getBasicTypeAdapter(int.class);
    }
    @Override
    public TypeAdapter<Out, In> getValueAdapter(BasicTypeAdapterProvider<Out, In> provider, Class<?> type) {
        return null;
    }

    private static class SparseArrayMap<V> implements Map<Integer,V>, Wrapper<SparseArrayDelegate<V>> {

        private final SparseArrayDelegate<V> sad ;
        public SparseArrayMap(SparseArrayDelegate<V> sad) {
            this.sad = sad;
        }
        @Override
        public int size() {
            return sad.size();
        }
        @Override
        public boolean isEmpty() {
            return sad.size() == 0;
        }
        @Override
        public boolean containsKey(Object key) {
            return sad.indexOfKey((Integer) key) >= 0;
        }
        @Override
        public boolean containsValue(Object value) {
            return sad.indexOfValue((V) value) >= 0;
        }
        @Override
        public V get(Object key) {
            return sad.get((Integer) key);
        }
        @Override
        public V put(Integer key, V value) {
            return  sad.put(key, value);
        }
        @Override
        public V remove(Object key) {
            return sad.getAndRemove((Integer) key);
        }
        @Override
        public void putAll(Map<? extends Integer, ? extends V> m) {
            for (Entry<? extends Integer, ? extends V> en : m.entrySet()){
                sad.put(en.getKey(), en.getValue());
            }
        }
        @Override
        public void clear() {
            sad.clear();
        }

        @Override
        public Set<Integer> keySet() {
            int size = sad.size();
            Set<Integer> set = new HashSet<>();
            for (int i = 0 ; i < size ; i ++){
                set.add(sad.keyAt(i));
            }
            return set;
        }
        @Override
        public Collection<V> values() {
            return sad.getValues();
        }
        @Override
        public Set<Entry<Integer, V>> entrySet() {
            int size = sad.size();
            Set<Entry<Integer, V>> set = new HashSet<>();
            for (int i = 0 ; i < size ; i ++){
                set.add(new SparseArrayMap.Entry0<>(sad, sad.keyAt(i), sad.valueAt(i)));
            }
            return set;
        }
        @Override
        public SparseArrayDelegate<V> unwrap() {
            return sad;
        }

        private static class Entry0<V> implements Entry<Integer, V>{
            final SparseArrayDelegate<V> sad;
            Integer key;
            V value;
            public Entry0(SparseArrayDelegate<V> sad, Integer key, V value) {
                this.sad = sad;
                this.key = key;
                this.value = value;
            }
            @Override
            public Integer getKey() {
                return key;
            }
            @Override
            public V getValue() {
                return value;
            }
            @Override
            public V setValue(V value) {
                V old = sad.put(this.key, value);
                this.value = value;
                return old;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj == null || !(obj instanceof SparseArrayMap.Entry0)){
                    return false;
                }
                SparseArrayMap.Entry0 e2 = (SparseArrayMap.Entry0) obj;
                return (getKey() == null ? e2.getKey() == null : getKey().equals(e2.getKey()))
                        && (getValue() == null ? e2.getValue() == null : getValue().equals(e2.getValue()));
            }
            @Override
            public int hashCode() {
                return  (getKey()==null  ? 0 : getKey().hashCode()) ^
                        (getValue()==null ? 0 : getValue().hashCode());
            }
        }
    }
}
