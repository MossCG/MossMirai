package org.mossmc.mosscg.Bot;

import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mossmc.mosscg.MossMirai.*;

public class BotListener {
    public static void botListenerThread() {
        sendInfo("正在启动机器人监听线程");
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Thread botListener = new Thread(BotListener::listenerThread);
        botListener.setName("botListenerThread");
        singleThreadExecutor.execute(botListener);
    }
    public static Listener<GroupMessageEvent> groupMessageListener;
    public static Listener<GroupTempMessageEvent> groupTempMessageListener;
    public static Listener<FriendMessageEvent> friendMessageListener;
    public static Listener<StrangerMessageEvent> strangerMessageListener;

    public static void listenerThread () {
        groupMessageListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            MessageChain chain = event.getMessage();
            BotMessage.readMessage(event.getGroup(), event.getSender(), chain , BotMessage.messageType.group);
        });
        groupMessageListener.start();
        groupTempMessageListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessageEvent.class, event -> {
            MessageChain chain = event.getMessage();
            BotMessage.readMessage(event.getGroup(), event.getSender(), chain , BotMessage.messageType.groupTemp);
        });
        groupTempMessageListener.start();
        friendMessageListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event -> {
            MessageChain chain = event.getMessage();
            BotMessage.readMessage(null, event.getSender(), chain , BotMessage.messageType.friend);
        });
        friendMessageListener.start();
        strangerMessageListener = GlobalEventChannel.INSTANCE.subscribeAlways(StrangerMessageEvent.class, event -> {
            MessageChain chain = event.getMessage();
            BotMessage.readMessage(null, event.getSender(), chain , BotMessage.messageType.stranger);
        });
        strangerMessageListener.start();
        sendInfo( "机器人监听线程已启动");
    }
}
