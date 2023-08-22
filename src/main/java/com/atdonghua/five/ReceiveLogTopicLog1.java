package com.atdonghua.five;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class ReceiveLogTopicLog1 {

    public static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 声明交换机
         * 交换机名称，交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        /**
         * 声明队列
         * 队列名称，是否持久化，是否共享，是否自动删除，map
         */
        channel.queueDeclare("Q1",false,false,false,null);

        /**
         * 绑定交换机与队列
         * 队列名称，交换机名称，绑定的key
         */
        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");

        /**
         * 接收消息
         * 队列名称，是否自动接受，接受回调函数，拒绝回调函数
         */
        channel.basicConsume("Q1",true,(tag,msg)-> {
            System.out.println("绑定的key"+msg.getEnvelope().getRoutingKey() +"1接受到的消息：" + new String(msg.getBody(),"UTF-8"));
        },(tag,msg)->{});
    }
}
