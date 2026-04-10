package org.example.order.rabbitmq.producer;

import org.example.order.rabbitmq.config.DelayQueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 延迟消息生产者
 */
@Component
public class DelayMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(DelayMessageProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送延迟消息
     * @param messageContent 消息内容
     * @param delayTime 延迟时间(毫秒)
     */
    public void sendDelayMessage(String messageContent, long delayTime) {
        log.info("发送延迟消息: {}, 延迟时间: {}ms", messageContent, delayTime);

        // 创建消息属性
        MessageProperties messageProperties = new MessageProperties();
        // 设置消息过期时间(毫秒)
        messageProperties.setExpiration(String.valueOf(delayTime));

        // 创建消息
        Message message = new Message(messageContent.getBytes(StandardCharsets.UTF_8), messageProperties);

        // 发送消息到正常交换机
        rabbitTemplate.send(
            DelayQueueConfig.NORMAL_EXCHANGE,
            DelayQueueConfig.NORMAL_ROUTING_KEY,
            message
        );

        log.info("消息已发送到延迟队列,将在 {}ms 后被消费", delayTime);
    }

    /**
     * 发送延迟消息(使用默认延迟时间)
     * @param messageContent 消息内容
     */
    public void sendDelayMessage(String messageContent) {
        // 默认延迟10秒
        sendDelayMessage(messageContent, 10000);
    }
}
