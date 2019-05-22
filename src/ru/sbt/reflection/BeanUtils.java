package ru.sbt.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class BeanUtils {
    public static void assign(Object to, Object from) {
        if (to == null || from == null) {
            throw new IllegalArgumentException("no arguments");
        }
        List<Method> methods_to = searchSettersTo(to);
        List<Method> methods_from = searchGettersFrom(from);
        for (Method methodTo : methods_to) {
            for (Method methodFrom : methods_from) {
                if (equalsParams(methodFrom, methodTo)) {
                    try {
                        methodTo.invoke(to, methodFrom.invoke(from));
                    } catch (InvocationTargetException ex) {
                        System.out.println("Exception:" + ex.getMessage());

                    } catch (IllegalAccessException ex) {
                        System.out.println("Exception:" + ex.getMessage());

                    }

                }
            }
        }

    }

    private static List<Method> searchGettersFrom(Object o) {

        List<Method> our_methods = new ArrayList<>();
        Class<?> clazz = o.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if ((method.getReturnType() != void.class) && (method.getParameterTypes().length == 0) && (method.getName().startsWith("get"))) {
                    our_methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return our_methods;
    }

    private static List<Method> searchSettersTo(Object o) {

        List<Method> our_methods = new ArrayList<>();
        Class<?> clazz = o.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if ((method.getReturnType() == void.class) & (method.getParameterTypes().length == 1) & (method.getName().startsWith("set"))) {
                    our_methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return our_methods;
    }

    private static boolean equalsParams(Method from, Method to) {
        return ((to.getParameterTypes()[0].isAssignableFrom(from.getReturnType())) && (to.getName().substring(3).equals(from.getName().substring(3))));
    }

}
