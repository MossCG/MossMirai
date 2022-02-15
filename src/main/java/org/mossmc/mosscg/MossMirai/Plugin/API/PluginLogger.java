package org.mossmc.mosscg.MossMirai.Plugin.API;

import org.mossmc.mosscg.MossMirai.MossMirai;

public class PluginLogger {
    //日志发送模块
    //别再用system.out.print了！！
    //这一块都是显示在后台的

    //发送信息日志 参数直接填要发送的日志即可
    public static void sendInfo(String info) {
        MossMirai.sendInfo(info);
    }

    //发送警告日志
    public static void sendWarn(String info) {
        MossMirai.sendWarn(info);
    }

    //发送错误日志
    public static void sendError(String info) {
        MossMirai.sendError(info);
    }

    //发送报错日志，catch到的exception请使用这个来发送
    public static void sendException(Exception exception) {
        MossMirai.sendException(exception);
    }
}
