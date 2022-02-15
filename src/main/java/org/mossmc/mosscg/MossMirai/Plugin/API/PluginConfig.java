package org.mossmc.mosscg.MossMirai.Plugin.API;

import org.mossmc.mosscg.MossMirai.Plugin.PluginInfo;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.mossmc.mosscg.MossMirai.MossMirai.*;

public class PluginConfig {
    //这是一个简易的获取插件的配置文件的方法
    //适用于大部分简单插件
    //有更多文件需求的请自行加载

    //config缓存
    public static Map<String,Map<String,String>> configCacheMap = new HashMap<>();

    //config的目录路径
    public static String getPluginConfigDir(String plugin) {
        return "./MossMirai/plugins/"+plugin;
    }

    //获取config的方法
    //使用前请先确认是否加载
    public static Map<String,String> getDefaultConfig(String plugin) {
        return configCacheMap.get(plugin);
    }

    //加载config的方法
    //使用前请先确定是否保存
    public static void loadDefaultConfig(String plugin) {
        Yaml yaml = new Yaml();
        FileInputStream input = null;
        try {
            input = new FileInputStream("./MossMirai/plugins/"+plugin+"/config.yml");
        } catch (FileNotFoundException e) {
            sendException(e);
        }
        configCacheMap.put(plugin,yaml.loadAs(input, Map.class));
    }

    //保存config的方法
    //使用前请先确定你有没有做配置文件
    //且配置文件的名称是否为config.yml
    public static void saveDefaultConfig(String plugin) throws Exception{
        ClassLoader classLoader = PluginInfo.pluginClassLoaderOnlyMap.get(plugin);
        File configDir = new File("./MossMirai/plugins/"+plugin);
        File configFile = new File("./MossMirai/plugins/"+plugin+"/config.yml");
        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
                sendWarn("无法创建插件文件夹"+plugin);
            }
        }
        if (!configFile.exists()) {
            InputStream configStream = classLoader.getResourceAsStream("config.yml");
            assert configStream != null;
            Files.copy(configStream, configFile.toPath());
        }
    }
}
