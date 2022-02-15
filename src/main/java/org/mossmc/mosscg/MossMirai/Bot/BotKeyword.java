package org.mossmc.mosscg.MossMirai.Bot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mossmc.mosscg.MossMirai.MossMirai.*;

public class BotKeyword {
    public static Map<String, List<String>> keywordList;
    public static List<String> keyword;

    public static void loadKeyword() {
        sendInfo("正在加载关键词信息");
        keywordList = new HashMap<>();
        keyword = new ArrayList<>();
        File file = new File("./MossMirai/keyword");
        File[] tempList = file.listFiles();
        if (tempList == null) {
            return;
        }
        for (File replyFile : tempList) {
            String read = readFileToString(replyFile);
            JSONObject jsonObject = JSON.parseObject(read);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                List<String> cacheList = new ArrayList<>();
                if (entry.getKey().startsWith("//")) {
                    continue;
                }
                Object object = jsonObject.get(entry.getKey());
                for (Object o : (List<?>) object) {
                    if (o.toString().startsWith("//")) {
                        continue;
                    }
                    cacheList.add((String) o);
                }
                keywordList.put(entry.getKey(),cacheList);
                keyword.add(entry.getKey());
            }
        }
        sendInfo(keywordList.toString());
        sendInfo("关键词信息加载完成");
    }

    public static MessageChainBuilder getReply(String receive, Group group, User user) {
        MessageChainBuilder chain;
        int i = 0;
        String key = null;
        while (keyword.size() > i) {
            if (receive.contains(keyword.get(i))) {
                key = keyword.get(i);
                break;
            }
            i++;
        }
        if (key == null) {
            return null;
        }
        if (keywordList.get(key).size() == 1) {
            String message = keywordList.get(key).get(0);
            if (message.contains("##<") && message.contains(">##")) {
                return loadArgs(message,group,user);
            } else {
                chain = new MessageChainBuilder();
                chain.append(message);
            }
        } else {
            int random = getRandomInt(keywordList.get(key).size()-1,0);
            String message = keywordList.get(key).get(random);
            if (message.contains("##<") && message.contains(">##")) {
                return loadArgs(message,group,user);
            } else {
                chain = new MessageChainBuilder();
                chain.append(message);
            }
        }
        return chain;
    }

    public static Image loadImage(String key, Group group, User user) {
        Image image;
        if (group != null) {
            image = group.uploadImage(ExternalResource.create(new File("./MossMirai/image/"+key)));
        } else {
            image = user.uploadImage(ExternalResource.create(new File("./MossMirai/image/"+key)));
        }
        return image;
    }

    public static MessageChainBuilder loadArgs(String message, Group group, User user) {
        MessageChainBuilder chain = new MessageChainBuilder();
        try {
            String[] cut = message.split(">##");
            int i = 0;
            while (cut.length > i) {
                String[] spilt = cut[i].split("##<");
                String arg;
                if (spilt.length == 1) {
                    arg = spilt[0];
                } else {
                    chain.append(spilt[0]);
                    arg = spilt[1];
                }
                spilt = arg.split("=");
                switch (spilt[0]) {
                    case "image":
                        chain.append(loadImage(spilt[1],group,user));
                    default:
                        break;
                }
                i++;
            }
        } catch (Exception e) {
            sendException(e);
        }
        return chain;
    }

    public static String readFileToString(File replyFile) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            FileInputStream fileInputStream = new FileInputStream(replyFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            String tempString;
            while((tempString = reader.readLine()) != null){
                stringBuilder.append(tempString);
            }
            reader.close();
        }catch(IOException e){
            sendException(e);
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    sendException(e);
                }
            }
        }
        return stringBuilder.toString();
    }
}
