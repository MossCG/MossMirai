package org.mossmc.mosscg.Bot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Builder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mossmc.mosscg.MossMirai.*;

public class BotReply {
    public static Map<String,List<String>> replyList;

    public static void loadReply() {
        sendInfo("正在加载自动回复信息");
        replyList = new HashMap<>();
        File file = new File("./MossMirai/reply");
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
                replyList.put(entry.getKey(),cacheList);
            }
        }
        sendInfo(replyList.toString());
        sendInfo("自动回复信息加载完成");
    }

    public static MessageChainBuilder getReply(String key) {
        if (!replyList.containsKey(key)) {
            return null;
        }
        MessageChainBuilder chain;
        if (replyList.get(key).size() == 1) {
            String message = replyList.get(key).get(0);
            if (message.contains("##<") && message.contains(">##")) {
                return loadImage(message);
            } else {
                chain = new MessageChainBuilder();
                chain.append(message);
            }
        } else {
            int random = getRandomInt(replyList.get(key).size()-1,0);
            String message = replyList.get(key).get(random);
            if (message.contains("##<") && message.contains(">##")) {
                return loadImage(message);
            } else {
                chain = new MessageChainBuilder();
                chain.append(message);
            }
        }
        return chain;
    }

    public static MessageChainBuilder loadImage(String message) {
        MessageChainBuilder chain = new MessageChainBuilder();
        String imagePart = message.substring(message.indexOf("##<")+1,message.indexOf("##>"));
        sendInfo(imagePart);
        chain.append(message);
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
