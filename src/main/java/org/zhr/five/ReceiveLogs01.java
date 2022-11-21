package org.zhr.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs01 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        /**
         * 生成一个随机队列 队列名称是随机的
         * 当消费者断开和该队列的连接的时候 队列自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        // 把临时队列绑定我们的exchange 其中routingkey (也称之为binding key) 为空字符串
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("等待接受消息,把接受到的消息答应到屏幕上");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("接收到的消息：" + message);
        };
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
