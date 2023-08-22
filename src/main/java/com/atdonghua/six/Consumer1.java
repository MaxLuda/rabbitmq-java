package com.atdonghua.six;

import com.atdonghua.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

public class Consumer1 {

    public static final String NORMAL_EXCHANGE="normal_exchange";
    public static final String DEAD_EXCHANGE="dead_exchange";

    public static void main(String[] args) throws Exception{
        /**
         * 获取信道
         */
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 声明交换机，这里声明两个
         *交换机名称，交换机类型
         */
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        /**
         * 声明队列
         * 队列名称，是否持久化，是否共享，是否自动删除，map(死信设置参数map)
         * 声明死信的map条件  消息被拒绝;消息TTL过期,队列达到最大长度
         * 当达到死信条件，自己将充当生产者，将这些死信发送到死信交换机中的队列中，需在map中设置死信交换机和绑定的key
         */
        Map<String, Object> map = new HashMap<>();
        //设置过期时间,也可在生产放指定过期时间
//        map.put("x-message-ttl",10000);
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        map.put("x-dead-letter-routing-key","lisi");
        //设置队列的最大长度
//        map.put("x-max-length",6);
        channel.queueDeclare("normal_queue",false,false,false,map);

        channel.queueDeclare("dead_queue",false,false,false,null);

        /**
         * 交换机绑定队列
         * 队列名称，交换机名称，绑定的key
         */
        channel.queueBind("normal_queue",NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind("dead_queue",DEAD_EXCHANGE,"lisi");

        System.out.println("等待接受消息。。。。");
        /**
         * 接收信息
         * 队列名称，是否自动接收，接受消息回调，拒绝回调
         * 注意开启拒收消息，ze不应该自动接收，设为false,因为自动应答了不存在拒接的问题了
         */
        channel.basicConsume("normal_queue",false,(tag,msg)-> {
            String mssg = new String(msg.getBody(),"UTF-8");
            if (mssg.equals("info5")){
                /**
                 * 手动拒接消息
                 * 消息的索引标签，是否还重新放入队列，false则放入死信队列，由死信队列的消费
                 */
                channel.basicReject(msg.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("绑定的key"+msg.getEnvelope().getRoutingKey() +"1接受到的消息：" + new String(msg.getBody(),"UTF-8"));
                /**
                 * 手动接受消息
                 * 消息的索引标签，是否批量处理
                 */
                channel.basicAck(msg.getEnvelope().getDeliveryTag(),false);
            }
        },(tag,msg)->{});

    }
}
