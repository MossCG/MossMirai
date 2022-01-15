package org.mossmc.mosscg.Bot;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.mossmc.mosscg.Sender;

import static org.mossmc.mosscg.MossMirai.*;

public class BotMessage {
    public enum messageType {
        group,friend,stranger,groupTemp
    }

    public static void readMessage(Group group, User user, MessageChain chain, messageType type) {
        String message = chain.contentToString();
        printMessage(group,user,message,type);
        MessageChainBuilder reply = null;
        if (message.startsWith(getConfig("botCommandPrefix"))) {
            reply = BotCommand.readCommand(message,user.getId());
        }
        if (reply != null) {
            returnCommandMessage(group,user,reply,type);
            if (getConfig("botOnlyOneReply").equals("true")) {
                return;
            }
        }
        reply = BotReply.getReply(message,group,user);
        if (reply != null) {
            returnReplyMessage(group,user,reply,type);
            if (getConfig("botOnlyOneReply").equals("true")) {
                return;
            }
        }
        reply = BotKeyword.getReply(message,group,user);
        if (reply != null) {
            returnReplyMessage(group,user,reply,type);
        }
    }

    public static void returnReplyMessage(Group group, User user, MessageChainBuilder chain, messageType type) {
        if (chain == null) {
            return;
        }
        if (type == messageType.group) {
            group.sendMessage(chain.build());
            return;
        }
        user.sendMessage(chain.build());
    }

    public static void returnCommandMessage(Group group, User user, MessageChainBuilder chain, messageType type) {
        if (chain == null) {
            return;
        }
        if (type == messageType.group) {
            chain.append("\r\n");
            chain.append("@");
            chain.append(user.getNick());
            group.sendMessage(chain.build());
            return;
        }
        user.sendMessage(chain.build());
    }

    public static void printMessage(Group group, User user, String message, messageType type) {
        String prefix;
        if (group == null) {
            prefix = "["+type.name()+"]["+user.getId()+"] ";
        } else {
            prefix = "["+type.name()+"]["+group.getId()+"]["+user.getId()+"] ";
        }
        Sender.send(prefix+message, Sender.sendMode.bot);
    }
}
