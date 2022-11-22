package org.zhr.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;
import org.zhr.utils.RabbitMqUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = "disk";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"error");
        System.out.println("等待接受消息");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            message = "接受绑定键" + delivery.getEnvelope().getRoutingKey() + " 消息" + message;
            File file = new File("C:\\Users\\20179\\Desktop\\学习笔记\\rabbitInfo.txt");
            FileUtils.writeStringToFile(file,message,StandardCharsets.UTF_8);
            System.out.println("错误日志已经接受");
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});

    }
}
