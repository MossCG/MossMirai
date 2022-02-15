package org.mossmc.mosscg.MossMirai;

import org.yaml.snakeyaml.Yaml;

import org.mossmc.mosscg.MossMirai.Bot.*;
import org.mossmc.mosscg.MossMirai.Plugin.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
        //检查启动是否通过
        checkStart(Arrays.toString(args));
        //启动机器人模块
        BotManager.startBot();
        BotCommand.loadCommand();
        //加载插件
        PluginLoader.loadPlugin();
        //启动机器人消息监听线程
        BotListener.botListenerThread();
    }

    //启动检查模块
    //避免windows系统的情况下
    //有人直接点了jar启动结果不会关
    public static void checkStart(String args) {
        if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            return;
        }
        if (getConfig("systemCheckPass").equals("true")) {
            return;
        }
        String pass = "-MossMirai=nb";
        if (!args.toLowerCase().contains(pass.toLowerCase())) {
            try {
                FileWriter fileWriter = new FileWriter("./run.bat");
                fileWriter.write("@echo off \r\n");
                fileWriter.write("title MossMirai By MossCG \r\n");
                fileWriter.write("java -Xms20m -Xmx100m -jar MossMirai.jar -MossMirai=nb \r\n");
                fileWriter.write("pause \r\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendInfo("启动失败！Windows系统请使用命令行启动MossMirai！并添加启动参数：’-MossMirai=nb‘！");
            sendInfo("若系统判断错误，请在config.yml里设置systemCheckPass: true！");
            System.exit(1);
        }
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
