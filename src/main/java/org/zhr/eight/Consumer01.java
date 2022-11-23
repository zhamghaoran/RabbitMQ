package org.zhr.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer01 {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明死信交换机和普通交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信队列
        String deadQueue = "dead-queue";
        channel.queueDeclare(deadQueue, false, false, false, null);
        // 死信队列绑定死信交换机与routingKey
        channel.queueBind(deadQueue, DEAD_EXCHANGE, "lisi");
        // 正常队列绑定死信消息
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        params.put("x-dead-letter-routing-key", "lisi");
//        params.put("x-max-length",6);
        String normalQueue = "normal-queue";
        channel.queueDeclare(normalQueue, false, false, false, params);
        channel.queueBind(normalQueue,NORMAL_EXCHANGE,"zhangsan");
        System.out.println("等待接受消息");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            if (message.equals("info5")) {
                System.out.println("Consumer01 接收到消息" + message + "并拒绝签收该消息");
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(),false);
            } else {
                System.out.println("Consumer01 接收到消息"+message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
            System.out.println("Consumer01 接受到消息" + message);
        };
        channel.basicConsume(normalQueue, false, deliverCallback, consumerTag -> {
        });
    }
}
