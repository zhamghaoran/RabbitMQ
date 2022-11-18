package org.zhr.two;

import com.rabbitmq.client.Channel;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * 可以发送大量的消息
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 队列名称
         * 队列消息是否需要持久化
         * 是否进行消息的共享
         * 是否自动删除
         * 其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 从控制台当中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送一个消费
             * 1.发送到那个交换机
             * 2.路由的Key值是哪个，本次是队列的名称
             * 3.其他参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完后" + message);
        }
    }
}
