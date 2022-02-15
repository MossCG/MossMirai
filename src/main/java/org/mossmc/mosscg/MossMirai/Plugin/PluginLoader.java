package org.mossmc.mosscg.MossMirai.Plugin;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Properties;

import static org.mossmc.mosscg.MossMirai.MossMirai.*;

public class PluginLoader {
    private final static String pluginSettings = "plugin.properties";
    private final static String path_MainClass = "mainClass";

    public static void loadPlugin() {
        sendInfo("开始加载插件模块");
        File file = new File("./MossMirai/plugins");
        File[] tempList = file.listFiles();
        if (tempList == null) {
            return;
        }
        for (File pluginFile : tempList) {
            String pluginName = null;
            String[] cut = pluginFile.getName().split("\\.");
            if (!cut[cut.length-1].equals("jar")) {
                continue;
            }
            sendInfo("正在加载插件："+pluginFile.getName());
            try {
                //这一段注释其实是我自己看的
                //不然我也懒得加
                //主要是不加我自己也容易看不懂
                //加载Class
                URL pluginFileURL = pluginFile.toURI().toURL();
                ClassLoader classLoader = new URLClassLoader(new URL[]{pluginFileURL}, ClassLoader.getSystemClassLoader());
                ClassLoader classLoaderOnly = new URLClassLoader(new URL[]{pluginFileURL}, null);
                //加载插件配置文件
                InputStream propertiesStream = classLoaderOnly.getResourceAsStream(pluginSettings);
                Properties properties = new Properties();
                properties.load(propertiesStream);
                //加载主类
                Class<?> mainClass = classLoader.loadClass(properties.getProperty(path_MainClass));
                Object object = mainClass.newInstance();
                //注册插件
                pluginName = properties.getProperty("pluginName");
                registerPlugin(pluginName,properties,mainClass,classLoader,classLoaderOnly);
                //获取主类的onEnable方法
                Method onEnable = mainClass.getDeclaredMethod("onEnable");
                onEnable.invoke(object);
                PluginInfo.setPluginInfo(pluginName,"status","enabled");
                sendInfo("已加载插件："+pluginFile.getName());
            }catch (Exception e) {
                sendException(e);
                PluginInfo.setPluginInfo(pluginName,"status","failed");
                sendWarn("无法加载插件："+pluginFile.getName());
            }
        }
        sendInfo("已完成插件加载");
    }

    public static void registerPlugin(String plugin,Properties properties,Class<?> mainClass,ClassLoader classLoader,ClassLoader classLoaderOnly){
        PluginInfo.pluginInfo.put(plugin,new HashMap<>());
        PluginInfo.setPluginInfo(plugin,"version",properties.getProperty("version"));
        PluginInfo.setPluginInfo(plugin,"author",properties.getProperty("author"));
        PluginInfo.setPluginInfo(plugin,"status","loading");
        PluginInfo.pluginClassMap.put(plugin,mainClass);
        PluginInfo.pluginClassLoaderMap.put(plugin,classLoader);
        PluginInfo.pluginClassLoaderOnlyMap.put(plugin,classLoaderOnly);
        PluginInfo.pluginList.add(plugin);
        sendInfo("已注册插件："+plugin+" 版本："+properties.getProperty("version")+" 作者："+properties.getProperty("author"));
    }
}
