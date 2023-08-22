package com.atdonghua.three;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class ReceiveLog1 {

    public static final String EXCHANGE_NAME = "los";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        /**
         * 生成一个临时队列
         * 队列的名称是随机的，当消费者断开与队列的连接的时候，队列就自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("等待接收消息。。。。");
        /**
         * 消费消息
         */
        channel.basicConsume(queue,true,(tag,msg)-> {
            System.out.println("1接受到的消息：" + new String(msg.getBody(),"UTF-8"));
        },(tag,msg) -> {});
    }
}
