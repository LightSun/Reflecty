package com.heaven7.java.reflecty.gson.test;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

public class Student extends Person{

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

    public static void main(String[] args) {
        Student s = new Student();
        s.setName("heaven7");
        s.setId(5353+"");
        String json = new Gson().toJson(s);
        System.out.println(json);
    }
}
