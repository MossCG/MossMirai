package org.mossmc.mosscg.MossMirai.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginInfo {
    //一个插件必须有的一些参数
    //status 状态 loading/enable/failed，不需要写在配置文件里，由核心管理
    //version 版本 如V1.0.0.0.1234，格式不强制要求
    //author 作者 如MossCG，格式不强制要求，不建议有中文，怕乱码
    //pluginName 插件名称，不带版本，如MossLuck，不建议有中文，怕乱码

    public static Map<String,Map<String,String>> pluginInfo = new HashMap<>();

    public static Map<String,Class<?>> pluginClassMap = new HashMap<>();

    public static Map<String,ClassLoader> pluginClassLoaderMap = new HashMap<>();

    public static Map<String,ClassLoader> pluginClassLoaderOnlyMap = new HashMap<>();

    public static List<String> pluginList = new ArrayList<>();

    public static void setPluginInfo(String plugin,String infoType,String info) {
        if (!pluginInfo.containsKey(plugin)) {
            pluginInfo.put(plugin,new HashMap<>());
        }
        pluginInfo.get(plugin).put(infoType,info);
    }

    public static String getPluginInfo(String plugin,String infoType) {
        return pluginInfo.get(plugin).get(infoType);
    }
}
