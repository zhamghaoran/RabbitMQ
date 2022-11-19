package org.zhr.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.RandomAccess;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失的
 */
public class Task02 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_name";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明队列
        // 第二个参数设置为true实现队列持久化
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        // 从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 实现消息持久化
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
