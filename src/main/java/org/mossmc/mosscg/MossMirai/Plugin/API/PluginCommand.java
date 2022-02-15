package org.mossmc.mosscg.MossMirai.Plugin.API;

import org.mossmc.mosscg.MossMirai.Bot.BotCommand;

import java.lang.reflect.Method;

public class PluginCommand {
    public static void registerCommand(String command, Method method) {
        BotCommand.registerCommand(command,method);
    }
}
