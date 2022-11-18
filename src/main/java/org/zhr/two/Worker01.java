package org.zhr.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    // 接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明接受消息
        DeliverCallback deliverCallback = (consumer, message) -> {
            System.out.println("接受到的消息: " + new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息被消费者取消消费接口回调逻辑");
        };
        System.out.println("C1等待接受消息......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
