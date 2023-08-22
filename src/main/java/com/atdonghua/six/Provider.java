package com.atdonghua.six;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class Provider {
    public static final String NORMAL_EXCHANGE="normal_exchange";
    public static void main(String[] args) throws Exception{
        /**
         * 获取信道
         */
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 模拟发送11条消息
         * 通过交换机和绑定的key确定那个队列
         * 交换机名称，绑定的key，AMQP.BasicProperties，消息内容(字节流)
         * 设置发送消息的设置参数由第三个参数决定
         */
//        AMQP.BasicProperties build = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String msg = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,msg.getBytes("UTF-8"));
        }


    }
}
