package org.zhr.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.zhr.utils.RabbitMqUtils;
import org.zhr.utils.SleepUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失的，放回队列中重新消费
 */
public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_name";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接受消息处理时间较短");
        // 采用手动应答
        DeliverCallback deliverCallback = ( consumerTag,message) -> {
            // 睡一秒
            try {
                SleepUtils.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("接收到的消息 " + new String(message.getBody(), StandardCharsets.UTF_8));
            // 手动应答
            /**
             * 1.消息的标记 tag
             * 2.是否批量应答 false表示不批量应答信道中的消息 true表示批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,(consumerTag -> {
            System.out.println("消费者取消消费接口回调逻辑");
        }));
    }
}
