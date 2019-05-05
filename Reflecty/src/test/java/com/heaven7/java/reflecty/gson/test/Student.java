package com.heaven7.java.reflecty.gson.test;

import com.google.gson.Gson;

public class Student extends Person{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        Student s = new Student();
        s.setName("heaven7");
        s.setId(5353+"");
        String json = new Gson().toJson(s);
        System.out.println(json);
    }
}
