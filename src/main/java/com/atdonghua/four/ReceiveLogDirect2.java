package com.atdonghua.four;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class ReceiveLogDirect2 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 声明一个直接直接交换机
         * 交换机名称，交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        /**
         * 声明队列
         * 队列名称，是否持久化，是否共享，是否自动删除，其它参数map
         */
        channel.queueDeclare("disk",false,false,false,null);

        /**
         *绑定交换机与队列
         * 队列名称，交换机名称，绑定的key
         */
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        /**
         * 接受消息
         * 队列名称，是否自动接收,接受消息的回调，拒绝消息的回调
         */
        channel.basicConsume("disk",true,(tag,msg)->{
            System.out.println("2接收到的消息：" + new String(msg.getBody(),"UTF-8"));
        },(tag,msg)->{});
    }
}
