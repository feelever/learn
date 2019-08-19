package cn.caigd.learn.douyu;

import com.alibaba.fastjson.JSON;
import com.yycdev.douyu.sdk.DouYuClient;
import com.yycdev.douyu.sdk.MessageListener;
import com.yycdev.douyu.sdk.entity.ChatMsg;

import java.io.IOException;
import java.nio.file.*;

public class Danmu {
    public static void main(String[] args) {
        Path path = Paths.get("D:/danmu-20190130.txt");


        DouYuClient client = new DouYuClient("openbarrage.douyutv.com", 8601, "1126960");
        //注册普通弹幕消息处理器
        client.registerMessageListener(new MessageListener<ChatMsg>() {
            @Override
            public void read(ChatMsg message) {
                try {
                    String result = JSON.toJSONString(message) + "\n";
                    Files.write(path, result.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //登录斗鱼服务器
        client.login();
        //开始同步到读取消息
        client.sync();
    }

    public static class DanmuDto {
        public DanmuDto(String text) {

        }
    }
}
