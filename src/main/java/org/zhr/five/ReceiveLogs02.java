package org.zhr.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;
import org.zhr.utils.RabbitMqUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接受消息,把接受到的消息答应到屏幕上");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            File file = new File("C:\\Users\\20179\\Desktop\\学习笔记\\rabbitInfo.txt");
            FileUtils.writeStringToFile(file,message,"UTF-8");
            System.out.println("写入文件成功");
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
