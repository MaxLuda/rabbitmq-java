package com.atdonghua.two;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {

    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开始发布确认
        channel.confirmSelect();
        //开始时间
        long startTime = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < 1000; i++) {
            String msg = i + "";
            channel.basicPublish("",queueName, null,msg.getBytes());
            boolean b = channel.waitForConfirms();
            if (b){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+ (endTime - startTime));
    }
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开始发布确认
        channel.confirmSelect();
        //开始时间
        long startTime = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < 1000; i++) {
            String msg = i + "";
            channel.basicPublish("",queueName, null,msg.getBytes());
            if (i % 100 == 0){
                boolean b = channel.waitForConfirms();
                if (b){
                    System.out.println("消息发送成功");
                }
            }
        }
        //结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+ (endTime - startTime));
    }

    public static void publishMessageSync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开始发布确认
        channel.confirmSelect();

        //准备一个并发安全有序的map
        //将消息与序号进行关联
        //轻松批量删除条目，只要给到序号
        ConcurrentSkipListMap<Long, Object> map = new ConcurrentSkipListMap<>();


        //开始时间
        long startTime = System.currentTimeMillis();

        //准备消息的监听器 监听那些消息成功或者失败
        channel.addConfirmListener((tag,flag)-> {
            if (flag){
                //headmap删除到已经确认的消息，留下未确认的消息
                ConcurrentNavigableMap<Long, Object> map1 = map.headMap(tag);
                map1.clear();
            }else{
                map.remove(tag);
            }
        },(tag,flag)-> {
            System.out.println("未确认的消息："+tag+"multiple"+flag);
        });


        //批量发送消息
        for (int i = 0; i < 1000; i++) {
            String msg = i + "";
            map.put(channel.getNextPublishSeqNo(),msg);
            channel.basicPublish("", queueName, null, msg.getBytes());
        }
        //结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+ (endTime - startTime));
    }
    public static void main(String[] args) throws Exception {
        //单个确认
//        ConfirmMessage.publishMessageIndividually();
        //批量确认
        //ConfirmMessage.publishMessageBatch();
        //异步确认
//        ConfirmMessage.publishMessageSync();
//        ConcurrentSkipListMap<Long, Object> map = new ConcurrentSkipListMap<>();
//        map.put(1L,"1");
//        map.put(2L,"2");
//        map.put(3L,"3");
//        map.put(4L,"4");
//        ConcurrentNavigableMap<Long, Object> map2 = map.headMap(3L);
//        map2.clear();
//        System.out.println(map);

    }
}
