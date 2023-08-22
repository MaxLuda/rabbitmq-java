package com.atdonghua.one;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class MessageProvider {

    private static final String QUEUE_NAME = "msg_ack";

    public void sendMessage(Channel channel, String msg) throws Exception {
        //队列声明
        channel.queueDeclare(QUEUE_NAME,false, false,false,null);
        //消息发送
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        System.out.println("消息发送完毕");
    }

    public static void main(String[] args) throws Exception {

        MessageProvider provider = new MessageProvider();
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String s = scanner.next();
            provider.sendMessage(channel,s);
        }
    }
}
