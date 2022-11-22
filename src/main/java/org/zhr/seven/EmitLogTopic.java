package org.zhr.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.zhr.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列Q1Q2接受");
        bindingKeyMap.put("lazy.orange.elephant","被队列Q1Q2接受");
        bindingKeyMap.put("quick.orange.fox","被队列Q1接受");
        bindingKeyMap.put("lazy.brow.fox","被队列Q2接受");
        bindingKeyMap.put("lazy.pink.rabbit","虽然不满足两个绑定只被队列Q2接受一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接受");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被绑定");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词单匹配Q2");
        for (Map.Entry<String,String> bindingKeyEntry : bindingKeyMap.entrySet()) {
            String bindingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,bindingKey,null,message.getBytes());
            System.out.println("生产者发出消息" + message);
        }
    }
}
