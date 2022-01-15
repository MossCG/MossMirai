package org.mossmc.mosscg.Bot;

import net.mamoe.mirai.message.data.*;
import org.mossmc.mosscg.BasicInfo;

import static org.mossmc.mosscg.MossMirai.*;
import static org.mossmc.mosscg.Bot.BotManager.*;

public class BotCommand {
    public static MessageChainBuilder readCommand(String command,long number) {
        MessageChainBuilder chain = new MessageChainBuilder();
        String[] cut = command.substring(getConfig("botCommandPrefix").length()).split("\\s+");
        switch (cut[0]) {
            case "帮助":
                chain.append("MossMirai Bot帮助");
                chain.append("\r\n重启机器人：").append(commandPrefix).append("重载");
                chain.append("\r\n机器人信息：").append(commandPrefix).append("状态");
                break;
            case "状态":
                if (!adminList.contains(number)) {
                    if (getConfig("botUnknownCommand").equals("false")) {
                        return null;
                    } else {
                        chain.append("你没有权限！");
                    }
                    break;
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
                chain.append("\r\n内存：");
                chain.append(String.format("%.2f", usage)).append("/");
                chain.append(String.format("%.2f", total)).append("MB");
                chain.append("\r\n核心版本：").append(BasicInfo.miraiVersion);
                chain.append("\r\n分支版本：").append(BasicInfo.mossMiraiVersion);
                chain.append("\r\n分支作者：").append(BasicInfo.author);
                chain.append("\r\n插件列表：");
                break;
            case "重载":
                if (!adminList.contains(number)) {
                    if (getConfig("botUnknownCommand").equals("false")) {
                        return null;
                    } else {
                        chain.append("你没有权限！");
                    }
                    break;
                }
                memoryUsage = Runtime.getRuntime().freeMemory();
                BotManager.bot.close();
                BotListener.groupMessageListener.complete();
                BotListener.groupTempMessageListener.complete();
                BotListener.friendMessageListener.complete();
                BotListener.strangerMessageListener.complete();
                System.gc();
                BotManager.startBot();
                BotListener.botListenerThread();
                long newMemoryUsage = Runtime.getRuntime().freeMemory();
                free = (double) (newMemoryUsage-memoryUsage);
                free = free/1024;
                free = free/1024;
                chain.append("重启完成！回收了内存");
                chain.append(String.format("%.2f", free));
                chain.append("MB");
                break;
            default:
                if (getConfig("botUnknownCommand").equals("false")) {
                    return null;
                } else {
                    chain.append("未知指令");
                }
                break;
        }
        return chain;
    }
}
