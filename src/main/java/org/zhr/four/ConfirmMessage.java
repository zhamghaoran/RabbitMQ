package org.zhr.four;

import com.rabbitmq.client.Channel;
import org.zhr.utils.RabbitMqUtils;

import java.util.UUID;

/**
 * 发布确认
 * 1.单个确认   使用的时间 比较哪种确认方式是做好的
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {
    // 批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 单个确认
//        ConfirmMessage.publishMessageIndividually();  //发布1000个单独确认消息，耗时99349ms
        // 批量确认
        ConfirmMessage.publishMessageBatch();           //发布1000个批量确认消息，耗时28884ms
        // 异步批量确认
    }


    //signal
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布模式
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息就马上进行发布
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个" + "单独确认消息，耗时" + (end - begin) + "ms");

    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布模式
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        // 批量确认消息
        int batchSize = 100;
        // 批量发送信息 批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());
            // 判断消息达到100条的时候批量确认一次
            if (i % batchSize == 0)
                channel.waitForConfirms();
        }
        // 发布确认
        channel.waitForConfirms();
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个" + "批量确认消息，耗时" + (end - begin) + "ms");

    }

}
