package com.atdonghua.three;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class EmitLog {

    public static final String EXCHANGE_NAME = "los";
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String s = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,s.getBytes("UTF-8"));
            System.out.println("生产者发出消息"+ s);
        }



    }
}
