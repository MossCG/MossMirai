package org.mossmc.mosscg.MossMirai.Bot;

import net.mamoe.mirai.message.data.*;
import org.mossmc.mosscg.MossMirai.BasicInfo;
import org.mossmc.mosscg.MossMirai.MossMirai;
import org.mossmc.mosscg.MossMirai.Plugin.PluginInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mossmc.mosscg.MossMirai.MossMirai.*;
import static org.mossmc.mosscg.MossMirai.Bot.BotManager.*;

public class BotCommand {
    public static Map<String, Method> commandMap = new HashMap<>();
    public static List<String> commandList = new ArrayList<>();
    public static Object object;

    public static void loadCommand() {
        String commandPrefix = getConfig("botCommandPrefix");
        try {
            Class<?> mainClass = Class.forName("org.mossmc.mosscg.MossMirai.MossMirai");
            Class<?> methodClass = Class.forName("org.mossmc.mosscg.MossMirai.Bot.BotCommand");
            object = mainClass.newInstance();
            registerCommand(commandPrefix+"帮助",methodClass.getDeclaredMethod("commandHelp", MessageChainBuilder.class, long.class, MessageChain.class));
            registerCommand(commandPrefix+"状态",methodClass.getDeclaredMethod("botStatus", MessageChainBuilder.class, long.class, MessageChain.class));
            registerCommand(commandPrefix+"重载",methodClass.getDeclaredMethod("botReload", MessageChainBuilder.class, long.class, MessageChain.class));
            registerCommand(commandPrefix+"插件列表",methodClass.getDeclaredMethod("pluginList", MessageChainBuilder.class, long.class, MessageChain.class));
        } catch (Exception e) {
            sendException(e);
            sendWarn("无法注册MossMirai基本指令");
        }
    }

    public static void registerCommand(String command,Method method) {
        commandMap.put(command, method);
        commandList.add(command);
        sendInfo("已注册指令："+command);
    }

    public static void pluginList(MessageChainBuilder chain,long number,MessageChain message) {
        chain.append("MossMirai Bot插件列表");
        int i = 0;
        while (i<PluginInfo.pluginList.size()) {
            String plugin = PluginInfo.pluginList.get(i);
            chain.append("\r\n[").append(plugin).append("]");
            chain.append("\r\n版本：").append(PluginInfo.getPluginInfo(plugin,"version"));
            chain.append("\r\n作者：").append(PluginInfo.getPluginInfo(plugin,"author"));
            i++;
        }
    }

    public static void commandHelp(MessageChainBuilder chain,long number,MessageChain message) {
        chain.append("MossMirai Bot帮助");
        chain.append("\r\n重启机器人：").append(commandPrefix).append("重载");
        chain.append("\r\n机器人信息：").append(commandPrefix).append("状态");
        chain.append("\r\n查看插件：").append(commandPrefix).append("插件列表");
    }

    public static void botStatus(MessageChainBuilder chain,long number,MessageChain message) {
        if (!adminList.contains(number)) {
            if (getConfig("botUnknownCommand").equals("false")) {
                return;
            } else {
                chain.append("你没有权限！");
            }
            return;
        }
        long memoryTotal = Runtime.getRuntime().totalMemory();
        long memoryUsage = Runtime.getRuntime().freeMemory();
        double free = (double) (memoryTotal-memoryUsage);
        free = free/1024;
        free = free/1024;
        double total = (double) (memoryTotal);
        total = total/1024;
        total = total/1024;
        double usage = total - free;
        chain.append("MossMirai Bot状态");
        chain.append("\r\n核心版本：").append(BasicInfo.miraiVersion);
        chain.append("\r\n分支版本：").append(BasicInfo.mossMiraiVersion);
        chain.append("\r\n分支作者：").append(BasicInfo.author);
        chain.append("\r\n内存占用：");
        chain.append(String.format("%.2f", usage)).append("/");
        chain.append(String.format("%.2f", total)).append("MB");
        chain.append("\r\n插件列表：").append("(").append(String.valueOf(PluginInfo.pluginList.size())).append(")");
        chain.append(PluginInfo.pluginList.toString());
    }

    public static void botReload(MessageChainBuilder chain,long number,MessageChain message) {
        if (!adminList.contains(number)) {
            if (getConfig("botUnknownCommand").equals("false")) {
                return;
            } else {
                chain.append("你没有权限！");
            }
            return;
        }
        long memoryUsage = Runtime.getRuntime().freeMemory();
        MossMirai.loadConfig();
        BotKeyword.loadKeyword();
        BotReply.loadReply();
        System.gc();
        long newMemoryUsage = Runtime.getRuntime().freeMemory();
        double free = (double) (newMemoryUsage-memoryUsage);
        free = free/1024;
        free = free/1024;
        chain.append("重载完成！回收了内存");
        chain.append(String.format("%.2f", free));
        chain.append("MB");
    }

    public static MessageChainBuilder readCommand(MessageChain command,long number) {
        MessageChainBuilder chain = new MessageChainBuilder();
        String[] cut = command.contentToString().split("\\s+");
        if (commandList.contains(cut[0])) {
            Method method = commandMap.get(cut[0]);
            try {
                method.invoke(object,chain,number,command);
            } catch (Exception e) {
                sendException(e);
            }
        } else {
            return null;
        }
        if (chain.build().size() <1) {
            return null;
        }
        return chain;
    }
}
