package org.zhr.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class Consumer02 {
    public static final String dead_exchange = "dead-exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(dead_exchange, BuiltinExchangeType.DIRECT);
        String deadQueue = "dead-queue";
        channel.queueDeclare(deadQueue,false,false,false,null);
        channel.queueBind(deadQueue,dead_exchange,"lisi");
        System.out.println("死信队列等待接受消息");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Consumer02接受死信队列消息：" + message);
        };
        channel.basicConsume(deadQueue,true,deliverCallback,consumerTag -> {});
    }
}
