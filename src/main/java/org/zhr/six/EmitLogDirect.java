package org.zhr.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Map<String ,String > buildingKeyMap = new HashMap<>();
        buildingKeyMap.put("info","普通信息");
        buildingKeyMap.put("warning","警告信息");
        buildingKeyMap.put("error","错误信息");
        // debug没人接受所以就会丢失
        buildingKeyMap.put("debug","debug");
        for (Map.Entry<String,String> bindingKeyEntry : buildingKeyMap.entrySet()) {
            String bindingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,bindingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发出的消息是 " + message);
        }
    }
}
