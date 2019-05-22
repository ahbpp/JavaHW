package ru.sbt.classLoader;

import java.net.MalformedURLException;
import java.net.URL;

public class PluginManager {
    private final String pluginRootDirectory;

    public PluginManager(String pluginRootDirectory) {

        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin load(String pluginName, String pluginClassName) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL url = new URL("file:/" + pluginRootDirectory + "/" + pluginName);
        ClassLoaderForBrowser classloader = new ClassLoaderForBrowser(new URL[]{url});
        return (Plugin) classloader.loadClass(pluginClassName).newInstance();
    }
}
