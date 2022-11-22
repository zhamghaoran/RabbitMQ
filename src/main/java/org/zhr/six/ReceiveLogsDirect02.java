package org.zhr.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DeliverCallback;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = "console";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"info");
        channel.queueBind(queueName,EXCHANGE_NAME,"warning");
        System.out.println("等待接受消息");
        DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String message = new String (delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("接受绑定键" + delivery.getEnvelope().getRoutingKey() + " 消息" + message);
        });
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
