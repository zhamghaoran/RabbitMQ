package org.zhr.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("");
        connectionFactory.setUsername("");
        connectionFactory.setPassword("");
        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();
    }
}
