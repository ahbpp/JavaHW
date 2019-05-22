package ru.sbt.classLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EncryptedClassLoader extends ClassLoader {

    private final int key;
    private final File dir;

    public EncryptedClassLoader(int key, File dir, ClassLoaderForBrowser parent) {
        super(parent);
        this.key = key;
        this.dir = dir;

    }

    @Override
    protected Class<?> findClass(String name){
        byte[] byteRaw = new byte[0];
        try {
            byteRaw = Files.readAllBytes(Paths.get(dir.getPath() + "/" + name.replace('.', '/').concat(".class")));
        } catch (IOException ex) {
            System.err.println("Exception:" + ex.getMessage());
        }

        for (int i = 0; i < byteRaw.length; i++) {
            byteRaw[i] = (byte) (byteRaw[i] + key);
        }
        return defineClass(name, byteRaw, 0, byteRaw.length);
    }
}