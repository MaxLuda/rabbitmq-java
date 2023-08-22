package com.atdonghua.six;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

public class Consumer2 {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("等待接受消息。。。");
        channel.basicConsume("dead_queue",true,(tag,msg)->{
            System.out.println("键是："+msg.getEnvelope().getRoutingKey()+"  消息是"+new String(msg.getBody(),"UTF-8"));
        },(tag,msg)->{});

    }
}
