package org.zhr.one;

import com.rabbitmq.client.*;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";
    // 接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        Channel channel = RabbitMqUtils.getChannel();
        // 声明接受消息
        DeliverCallback deliverCallback = (consumer,message)->{
            System.out.println(new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断的时候");
        };
        /**
         * 消费者接受消息
         * 1.消费那个队列
         * 2.消费成功之后要不要自动应答 true为自动，false为手动
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        String s = channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
