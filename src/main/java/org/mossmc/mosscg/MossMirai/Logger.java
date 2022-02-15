package org.mossmc.mosscg.MossMirai;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mossmc.mosscg.MossMirai.MossMirai.*;

public class Logger {
    //log记录部分
    //和send部分配套使用
    public static List<String> writerCache = new ArrayList<>();
    public static FileWriter getWriter = null;

    public static void loadWriter() {
        sendInfo("正在加载日志模块");
        try {
            getWriter = new FileWriter("MossMirai/logs/latest.yml",true);
            int size = writerCache.size();
            int i = 0;
            while (i < size) {
                fileInput(writerCache.get(i));
                i++;
            }
        } catch (IOException e) {
            sendException(e);
            sendError("日志模块加载失败！");
            System.exit(1);
        }
        sendInfo("日志模块加载完成");
    }

    public static void fileInput(String info) {
        try {
            if (getWriter == null) {
                writerCache.add(info);
                return;
            }
            getWriter.write("["+getNowTime()+"] "+info+"\r\n");
            getWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            sendWarn("日志写入失败！");
        }
    }
}
