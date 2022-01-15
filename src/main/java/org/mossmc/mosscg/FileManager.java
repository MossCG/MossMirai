package org.mossmc.mosscg;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import static org.mossmc.mosscg.MossMirai.*;

public class FileManager {
    //启动文件检测
    public static void checkStart() {
        sendInfo("正在检查文件");
        try {
            //主文件夹检查
            File basicDir = new File("./MossMirai");
            if (!basicDir.exists()) {
                basicDir.mkdir();
            }
            //回复配置文件夹检查
            File replyDir = new File("./MossMirai/reply");
            if (!replyDir.exists()) {
                replyDir.mkdir();
                InputStream in = MossMirai.class.getClassLoader().getResourceAsStream("reply.json");
                assert in != null;
                Files.copy(in, replyDir.toPath());
            }
            //log文件夹检查
            File logDir = new File("./MossMirai/logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            //copy旧的log
            File latestLogFile = new File("./MossMirai/logs/latest.yml");
            File saveLogFile = new File("./MossMirai/logs/"+getNowTime()+".yml");
            if (latestLogFile.exists()) {
                Files.copy(latestLogFile.toPath(), saveLogFile.toPath());
                Files.delete(latestLogFile.toPath());
            }
            //config文件检查
            File configFile = new File("./MossMirai/config.yml");
            if (!configFile.exists()) {
                InputStream in = MossMirai.class.getClassLoader().getResourceAsStream("config.yml");
                assert in != null;
                Files.copy(in, configFile.toPath());
            }
        } catch (Exception e) {
            sendException(e);
            sendError("文件检查失败！");
            System.exit(1);
        }
        sendInfo("文件检查完成");
    }
}
