package com.heaven7.java.reflecty.gson.test;


import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

@JsonAdapter(Person.Adapter0.class)
public class Person {

    private String name;
    private int age;

    private byte xx1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public static class Adapter0 extends TypeAdapter<Person>{
        @Override
        public void write(JsonWriter out, Person value) throws IOException {
            out.beginObject();
            out.name("NAME").value(value.getName());
            out.name("AGE").value(value.getAge());
            out.endObject();
        }
        @Override
        public Person read(JsonReader in) throws IOException {
            Person p = new Person();
            in.beginObject();
            while (in.hasNext()){
                switch (in.nextName()){
                    case "NAME":
                        p.setName(in.nextString());
                        break;
                    case "AGE":
                        p.setAge(in.nextInt());
                        break;
                }
            }
            in.endObject();
            return p;
        }
    }

}
