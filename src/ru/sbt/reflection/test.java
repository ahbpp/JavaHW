package ru.sbt.reflection;

import java.lang.reflect.Field;

public class test {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {
        Person person = new Person();
        Field name = Person.class.getDeclaredField("name");
        name.setAccessible(true);
        name.set(person, "Julia");
        person.get();
    }
}
