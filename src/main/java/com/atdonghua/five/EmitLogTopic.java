package com.atdonghua.five;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.*;

public class EmitLogTopic {

    public static final String EXCHANGE_NAME="topic_logs";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        HashMap<String, Object> map = new HashMap<>();
        map.put("quick.orange.rabbit","被Q1Q2接受");
        map.put("lazy.orange.elephant","被Q1Q2接受");
        map.put("sh.orange.sh","被Q1接受");
        map.put("lazy.sh","被Q2接受");
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry =  iterator.next();
            channel.basicPublish(EXCHANGE_NAME,entry.getKey(),null,entry.getValue().toString().getBytes("UTF-8"));
            System.out.println("key:"+entry.getKey()+",消息是："+entry.getValue());
        }



    }
}
