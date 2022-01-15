package org.mossmc.mosscg;

import org.mossmc.mosscg.Bot.BotListener;
import org.mossmc.mosscg.Bot.BotManager;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;

public class MossMirai {
    public static void main(String[] args) {
        //发送启动信息
        startMessage();
        //运行前自检部分
        //文件检查
        FileManager.checkStart();
        //配置文件读取
        loadConfig();
        //启动日志模块
        Logger.loadWriter();
        //启动机器人模块
        BotManager.startBot();
        //启动机器人消息监听线程
        BotListener.botListenerThread();
    }

    //启动信息模块
    public static void startMessage() {
        sendInfo("欢迎使用MossMirai机器人框架");
        sendInfo("MossMirai版本："+BasicInfo.mossMiraiVersion);
        sendInfo("Mirai版本："+BasicInfo.miraiVersion);
        sendInfo("开发者："+BasicInfo.author);
    }

    //信息发送模块
    public static void sendInfo(String info) {
        Sender.send(info, Sender.sendMode.info);
    }

    public static void sendWarn(String warn) {
        Sender.send(warn, Sender.sendMode.warn);
    }

    public static void sendError(String error) {
        Sender.send(error, Sender.sendMode.error);
    }

    public static void sendMessage(String message) {
        Sender.send(message, Sender.sendMode.bot);
    }

    public static void sendException(Exception e) {
        Sender.exceptionRead(e);
    }

    //简单的获取时间的一个方法
    //输出示例：2021-10-30-21-6-55
    public static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static String getNowTime() {
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }

    //config加载方法以及读取方法
    //使用yaml格式
    public static Map configMap;

    public static String getConfig(String configPath) {
        return configMap.get(configPath).toString();
    }

    public static void loadConfig() {
        sendInfo("正在加载配置文件");
        Yaml yaml = new Yaml();
        FileInputStream input = null;
        try {
            input = new FileInputStream("./MossMirai/config.yml");
        } catch (FileNotFoundException e) {
            sendException(e);
            sendError("配置文件加载失败！");
            System.exit(1);
        }
        configMap = yaml.loadAs(input, Map.class);
        sendInfo("配置文件加载完成");
    }

    //获取一个随机数
    public static Random random = new Random();
    public static Integer getRandomInt(Integer rangeMax,Integer rangeMin) {
        return random.nextInt(rangeMax-rangeMin+1)+rangeMin;
    }
}
