package com.heaven7.java.reflecty.gson.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.reflecty.TypeToken;
import com.heaven7.java.reflecty.gson.GsonATM;
import com.heaven7.java.reflecty.iota.TypeAdapter;
import org.junit.Assert;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest  {

    protected GsonATM mAtm = new GsonATM();


    protected void testBase(Object obj) throws Exception{
        testBase(obj, 1.0f);
    }

    protected void testBase(Object obj, float version) throws Exception{
        testBase(new GsonBuilder(), obj, version);
    }

    protected void testBase(GsonBuilder builder,Object obj, float version) throws Exception{
        Gson gson = builder.setVersion(version).create();
        StringWriter sw = new StringWriter();
        TypeAdapter.ofClass(obj.getClass(), mAtm, version).write(new JsonWriter(sw), obj);
        Assert.assertEquals(gson.toJson(obj), sw.toString());
    }

    protected void testTypeToken(TypeToken<?> tt, Object obj, float version) throws Exception{
        testTypeToken(new GsonBuilder(), tt, obj, version);
    }
    protected void testTypeToken(GsonBuilder builder,TypeToken<?> tt, Object obj, float version) throws Exception{
        Gson gson = builder.setVersion(version).create();
        StringWriter sw = new StringWriter();
        TypeAdapter.ofTypeToken(tt, mAtm, version).write(new JsonWriter(sw), obj);
        Assert.assertEquals(gson.toJson(obj), sw.toString());
        System.out.println(sw.toString());
    }

    public static List<String> createList(){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < 3 ; i ++){
            list.add("hhh___" + i);
        }
        return list;
    }

    public static Map<Integer,String> createMap(){
        Map<Integer,String> map = new HashMap<>();
        for (int i = 0 ; i < 3 ; i ++){
            map.put(i, "hhh___" + i);
        }
        return map;
    }

    public static Map<Person,String> createPersonMap(){
        Map<Person,String> map = new HashMap<>();
        for (int i = 0 ; i < 3 ; i ++){
            Person p = new Person();
            p.setAge(i * 10);
            p.setName("heaven7__" + i);
            map.put(p, "hhh___" + i);
        }
        return map;
    }

    public static SparseArrayDelegate<String> createSparseArray(){
        SparseArrayDelegate<String> map = SparseFactory.newSparseArray(10);
        for (int i = 0 ; i < 3 ; i ++){
            map.put(i, "hhh___" + i);
        }
        return map;
    }

}
