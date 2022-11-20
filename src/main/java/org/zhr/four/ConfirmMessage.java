package org.zhr.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import org.zhr.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

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
        //ConfirmMessage.publishMessageBatch();           //发布1000个批量确认消息，耗时28884ms
        // 异步批量确认
        ConfirmMessage.publishMessageAsync();  //发布1000个异步确认消息，耗时1633ms

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

    // 异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布模式
        channel.confirmSelect();
        /**
         * 线程安全有序的一个hash表 适用于高并发的情况下
         * 1.能轻松的将序号和消息进行关联
         * 2.轻松的批量删除 只要给到序号
         * 3.支持高并发(多线程)
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();
        // 准备消息的监听器，那些消息成功了，那些消息失败了
        // 消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            // 2.删除掉已经确认的消息,剩下的就是未确认消息
            System.out.println("确认的消息的编号" + deliveryTag);
        };
        // 消息确认失败回调函数
        /**
         * 1.第一个是消息的标记
         * 2.第二个是是否批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outstandingConfirms.get(deliveryTag);
            // 打印为确认的消息
            System.out.println("未确认的消息是" + message + "未确认的消息的标记 " + deliveryTag);
        };
        /**
         * 1.监听那条消息成功了
         * 2.监听那些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);  // 异步通知
        //开始时间
        long begin = System.currentTimeMillis();
        // 批量发送
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = new String(String.valueOf(i));
            channel.basicPublish("", queueName, null, message.getBytes());
            //1.此处记录下所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个" + "异步确认消息，耗时" + (end - begin) + "ms");
    }

}
