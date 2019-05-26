package com.heaven7.java.reflecty.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.reflecty.ReflectyContext;
import com.heaven7.java.reflecty.Wrapper;
import com.heaven7.java.reflecty.iota.BasicTypeAdapterProvider;
import com.heaven7.java.reflecty.iota.CollectionIotaPlugin;
import com.heaven7.java.reflecty.iota.TypeAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class IotaPluginTest {

    final GsonATM atm = new GsonATM();

    @Test @SuppressWarnings("unchecked")
    public void test(){
        ReflectyContext context = atm.getReflectyContext();
        Class<AtomicIntegerArray> clazz = AtomicIntegerArray.class;

        atm.addIotaPlugin(new AtomicIntegerArrayIotaPlugin());

        Assert.assertTrue(context.isCollection(clazz));
        Assert.assertTrue(context.createCollection(clazz) instanceof AtomicIntegerArrayCollection);

        AtomicIntegerArray aia = new AtomicIntegerArray(3);
        Collection<Integer> collection = context.getCollection(aia);
        Assert.assertTrue(collection instanceof AtomicIntegerArrayCollection);

        AtomicIntegerArrayCollection aiac = (AtomicIntegerArrayCollection) context.getCollection(aia);
        aiac.add(1);
        aiac.add(2);
        aiac.add(3);
        Assert.assertEquals(aiac.array.toString(), Arrays.asList(1, 2, 3).toString());
    }

    private static class AtomicIntegerArrayIotaPlugin extends CollectionIotaPlugin<JsonWriter, JsonReader>{

        public AtomicIntegerArrayIotaPlugin() {
            super(AtomicIntegerArray.class);
        }

        @Override
        public TypeAdapter<JsonWriter, JsonReader> getElementAdapter(BasicTypeAdapterProvider<JsonWriter, JsonReader> provider, Class<?> type) {
            return provider.getBasicTypeAdapter(int.class);
        }
        @Override
        public Collection<?> createCollection(Object obj) {
            return new AtomicIntegerArrayCollection((AtomicIntegerArray)obj);
        }
        @Override
        public Collection<?> createCollection(Class<?> clazz) {
            return new AtomicIntegerArrayCollection(new AtomicIntegerArray(3));
        }
    }
    private static class AtomicIntegerArrayCollection extends AbstractCollection<Integer> implements Wrapper<AtomicIntegerArray> {

        private final AtomicIntegerArray array;
        private int size = 0;

        public AtomicIntegerArrayCollection(AtomicIntegerArray array) {
            this.array = array;
        }

        @Override
        public boolean add(Integer value) {
            if(size >= array.length()){
                return false;
            }
            array.set(size++, value);
            return true;
        }

        @Override
        public Iterator<Integer> iterator() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0 ; i < size; i ++){
                list.add(array.get(i));
            }
            return list.iterator();
        }
        @Override
        public int size() {
            return size;
        }
        @Override
        public AtomicIntegerArray unwrap() {
            return array;
        }
    }
}
