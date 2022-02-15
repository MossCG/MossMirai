package org.mossmc.mosscg.MossMirai;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Sender {
    //信息发送模式
    public enum sendMode {
        info,warn,error,exception,bot
    }

    //信息发送方法
    public static void send(String message,sendMode mode) {
        String prefix;
        switch (mode) {
            case info:
                prefix = "[Info] ";
                break;
            case warn:
                prefix = "[Warn] ";
                break;
            case error:
                prefix = "[Error] ";
                break;
            case exception:
                prefix = "[Exception] ";
                break;
            case bot:
                prefix = "[Bot] ";
                break;
            default:
                prefix = "[Unknown] ";
                break;
        }
        System.out.println(prefix+message);
        Logger.fileInput(prefix+message);
    }

    //报错管理方法
    public static void exceptionRead(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter= new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        send(stringWriter.toString(),sendMode.exception);
        try {
            printWriter.close();
            stringWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
