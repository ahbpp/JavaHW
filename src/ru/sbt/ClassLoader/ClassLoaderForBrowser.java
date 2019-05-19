package ru.sbt.ClassLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderForBrowser extends URLClassLoader {

    public ClassLoaderForBrowser(URL[] url) {
        super(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.contains("Plugin") || name.startsWith("java")) {
            return super.loadClass(name);
        }
        return findClass(name);
    }
}
