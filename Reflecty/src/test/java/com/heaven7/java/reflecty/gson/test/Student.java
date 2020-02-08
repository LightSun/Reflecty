package com.heaven7.java.reflecty.gson.test;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Student extends Person{

    public static final ArrayList<String> list1 = new ArrayList<>();
    public static final ArrayList<String> list2 = new ArrayList<>();

    @Since(1.0)
    @Until(2.0)
    @SerializedName("_id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        Student s = new Student();
        s.setName("heaven7");
        s.setId(5353+"");
        String json = new Gson().toJson(s);
        System.out.println(json);

        Field f = Student.class.getDeclaredField("list1");
        Type t1 = f.getGenericType();

        Field f2 = Student.class.getDeclaredField("list2");
        Type t2 = f2.getGenericType();
        System.out.println(t1 == t2);      //false
        System.out.println(t1.equals(t2)); // true
        System.out.println(t1.hashCode() == t2.hashCode()); // true
    }

}
