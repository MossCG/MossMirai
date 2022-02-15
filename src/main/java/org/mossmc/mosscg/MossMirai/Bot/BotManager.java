package org.mossmc.mosscg.MossMirai.Bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mossmc.mosscg.MossMirai.MossMirai.*;

public class BotManager {
    public static Bot bot;
    public static String commandPrefix;
    public static Map<Long,Integer> permissionMap;
    public static Map<String,Long> coolDownMap;
    public static List<Long> groupList;
    public static List<Long> adminList;
    public static Map<String,String> confirmMap;
    public static void loadBotSettings() {
        File dataFile = new File(getConfig("botFile"));
        String groups = getConfig("botGroups");
        String admins = getConfig("botAdmins");
        permissionMap = new HashMap<>();
        coolDownMap = new HashMap<>();
        groupList = new ArrayList<>();
        adminList = new ArrayList<>();
        confirmMap = new HashMap<>();
        commandPrefix = getConfig("botCommandPrefix");
        String[] cut = groups.split(",");
        for (String s : cut) {
            groupList.add(Long.valueOf(s));
            sendInfo("开启消息群：" + s);
        }
        cut = admins.split(",");
        for (String s : cut) {
            adminList.add(Long.valueOf(s));
            sendInfo("管理用户：" + s);
        }
        if (!dataFile.exists()) {
            sendInfo("机器人运行文件目录不存在！正在创建目录");
            if (dataFile.mkdir()) {
                sendInfo("成功创建机器人运行文件目录");
            } else {
                sendWarn("机器人运行文件目录创建失败！已取消机器人启动！");
                System.exit(1);
            }
        }
        BotReply.loadReply();
        BotKeyword.loadKeyword();
    }
    public static void startBot() {
        sendInfo("正在初始化机器人模块");
        loadBotSettings();
        File dataFile = new File(getConfig("botFile"));
        String qq = getConfig("botNumber");
        String password = getConfig("botPassword");
        sendInfo("机器人模块初始化完成");
        sendInfo("正在启动机器人模块");
        sendInfo("机器人账号："+qq);
        bot = BotFactory.INSTANCE.newBot(Long.parseLong(qq), password, new BotConfiguration() {{
            if (getConfig("botMainLog").equals("false")) {
                noBotLog();
                sendInfo("已关闭机器人信息日志显示");
            }if (getConfig("botNetworkLog").equals("false")) {
                noNetworkLog();
                sendInfo("已关闭机器人网络日志显示");
            }
            if (getConfig("botHeartbeat").equals("1")) {
                setHeartbeatStrategy(HeartbeatStrategy.STAT_HB);
                sendInfo("机器人心跳策略：STAT_HB");
            } else {
                if (getConfig("botHeartbeat").equals("2")) {
                    setHeartbeatStrategy(HeartbeatStrategy.REGISTER);
                    sendInfo("机器人心跳策略：REGISTER");
                } else {
                    setHeartbeatStrategy(HeartbeatStrategy.NONE);
                    sendInfo("机器人心跳策略：NONE");
                }
            }
            if (getConfig("botProtocol").equals("1")) {
                setProtocol(MiraiProtocol.ANDROID_PHONE);
                sendInfo("机器人登陆协议：ANDROID_PHONE");
            } else {
                if (getConfig("botProtocol").equals("2")) {
                    setProtocol(MiraiProtocol.ANDROID_PAD);
                    sendInfo("机器人登陆协议：ANDROID_PAD");
                } else {
                    setProtocol(MiraiProtocol.ANDROID_WATCH);
                    sendInfo("机器人登陆协议：ANDROID_WATCH");
                }
            }
            sendInfo("机器人工作目录："+dataFile);
            setWorkingDir(dataFile);
            fileBasedDeviceInfo();
        }});
        sendInfo("正在登陆机器人账号");
        try {
            bot.login();
        } catch (Exception e) {
            sendException(e);
        }
        sendInfo("机器人模块启动完成");
    }
}
