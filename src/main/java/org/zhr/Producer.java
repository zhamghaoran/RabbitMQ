package org.zhr;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class  Producer {
    public static final String QUEUE_NAME = "hello";

    //ToDo 别学了
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("");
        // 用户名
        connectionFactory.setUsername("");
        connectionFactory.setPassword("");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();

        /**
         * 创建一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化，默认是存在内存当中的
         * 3.该队列是否只供一个消费者进行消费，是否进行消费共享，true可以多个消费者，false不共享
         * 4.是否自动删除 最后一个消费者断开连接以后，该队是否自动删除，true自动删除，false不自动删除
         * 5.其他参数
         */
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello world"; // 初次使用
        /**
         * 发送一个消费
         * 1.发送到那个交换机
         * 2.路由的key值是那个，本次是队列名称
         * 3.其他参数信息
         * 4.发送的消息
         */
        channel.basicPublish("",QUEUE_NAME,null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
