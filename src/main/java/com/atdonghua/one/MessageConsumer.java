package com.atdonghua.one;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.concurrent.TimeUnit;

public class MessageConsumer {

    private static final String QUEUE_NAME = "msg_ack";

    public void receiveMessage(Channel channel) throws Exception {
        //消费消息
        System.out.println("消息正在消费");
        //不公平分发
        channel.basicQos(2);
        channel.basicConsume(
                QUEUE_NAME,
                false,
                (tag,msg)-> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(new String(msg.getBody(),"UTF-8"));
                    System.out.println("消息消费完毕");
                    channel.basicAck(msg.getEnvelope().getDeliveryTag(),false);
                },
                tag-> System.out.println("消费者消费被中断")
        );


    }
    public static void main(String[] args) throws Exception {
        MessageConsumer consumer = new MessageConsumer();
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        consumer.receiveMessage(channel);
    }
}
