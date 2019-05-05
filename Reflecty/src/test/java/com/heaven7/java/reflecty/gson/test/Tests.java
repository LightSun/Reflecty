package com.heaven7.java.reflecty.gson.test;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.reflecty.TypeToken;
import com.heaven7.java.reflecty.gson.JsonObjectAdapter;
import com.heaven7.java.reflecty.gson.ReflectyTypeAdapterFactory;
import com.heaven7.java.reflecty.gson.SparseArrayAdapter;
import com.heaven7.java.reflecty.iota.TypeAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tests extends BaseTest {

    @Test
    public void testSinceUntil()throws Exception  {
        Student s = new Student();
        s.setName("heaven7");
        s.setId(5353+"");
        testBase(s ,0.5f);
        testBase(s ,1.0f);
        testBase(s ,2.0f);
        testBase(s ,3.0f);
    }

    @Test
    public void testJsonAdapter() throws Exception {
        Person p = new Person();
        p.setAge(18);
        p.setName("heaven7");

        testBase(p);
    }

    @Test
    public void testDynamicRegistry() throws Exception {
        Student s = new Student();
        s.setName("heaven7");
        s.setId(5353+"");

        mAtm.registerTypeAdapter(Student.class, 1.0f, new JsonObjectAdapter(new Person.Adapter0()));
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Student.class, new Person.Adapter0());
        testBase(builder, s, 1.0f);
    }

    @Test
    public void testCollection1() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        //for collection .we need assign the type token
        testTypeToken(new TypeToken<List<Integer>>(){}, list , 1.0f);
    }

    @Test
    public void testCollection2() throws Exception {
        List<Person> list = new ArrayList<>();
        for(int i = 0 ; i < 3 ; i ++){
            Person p = new Person();
            p.setAge(10 * i);
            p.setName("heaven7__" + i);
            list.add(p);
        }
        //for collection .we need assign the type token
        testTypeToken(new TypeToken<List<Person>>(){}, list , 1.0f);
    }

    @Test
    public void testCollection3() throws Exception{
        List<List<String>> list = new ArrayList<>();
        list.add(createList());
        list.add(createList());
        list.add(createList());
        testTypeToken(new TypeToken<List<List<String>>>(){}, list , 1.0f);
    }

    @Test
    public void testMap1() throws Exception{
        Map<Integer, String> map = createMap();
        testTypeToken(new TypeToken<Map<Integer, String>>(){}, map , 1.0f);
    }

    @Test
    public void testMap2() throws Exception{
        SparseArrayDelegate<String> sa = createSparseArray();
        TypeAdapter<JsonWriter, JsonReader> mta = mAtm.createMapTypeAdapter(SparseArrayDelegate.class,
                mAtm.getBasicTypeAdapter(int.class), mAtm.getBasicTypeAdapter(String.class));
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(sa.getClass(), new SparseArrayAdapter(mta));

        testTypeToken(builder, new TypeToken<SparseArrayDelegate<String>>(){}, sa , 1.0f);
    }

    @Test
    public void testMap3() throws Exception{
        Map<Person, String> map = createPersonMap();
        TypeToken<Map<Person, String>> tt = new TypeToken<Map<Person, String>>() {
        };
        TypeAdapter<JsonWriter, JsonReader> ta = TypeAdapter.ofTypeToken(tt, mAtm, 1.0f);

        StringWriter sw = new StringWriter();
        ta.write(new JsonWriter(sw), map);
        //read it as map.
        Object obj = ta.read(new JsonReader(new StringReader(sw.toString())));
        Assert.assertEquals(obj, map);
        System.out.println(obj);
    }

    @Test
    public void testTypeAdapterFactory() throws Exception{
        SparseArrayDelegate<String> sa = createSparseArray();
        String s = new GsonBuilder()
                .registerTypeAdapterFactory(new ReflectyTypeAdapterFactory(mAtm, 1.0f))
                .setVersion(1.0f)
                .create()
                .toJson(sa, new TypeToken<SparseArrayDelegate<String>>(){}.getType()); // must assign type.
        System.out.println(s);
    }
}
