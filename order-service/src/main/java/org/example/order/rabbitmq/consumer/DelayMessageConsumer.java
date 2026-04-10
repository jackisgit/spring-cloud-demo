package org.example.order.rabbitmq.consumer;

import org.example.order.rabbitmq.config.DelayQueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 延迟消息消费者
 * 监听死信队列,处理延迟后的消息
 */
@Component
public class DelayMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(DelayMessageConsumer.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 消费延迟消息
     * 监听死信队列
     */
    @RabbitListener(queues = DelayQueueConfig.DEAD_LETTER_QUEUE)
    public void consumeDelayMessage(Message message) {
        String messageContent = new String(message.getBody(), StandardCharsets.UTF_8);
        String currentTime = LocalDateTime.now().format(FORMATTER);

        log.info("========================================");
        log.info("收到延迟消息: {}", messageContent);
        log.info("消费时间: {}", currentTime);
        log.info("========================================");

        // 这里可以添加具体的业务处理逻辑
        processMessage(messageContent);
    }

    /**
     * 处理消息的业务逻辑
     */
    private void processMessage(String messageContent) {
        // 示例: 打印处理信息
        log.info("正在处理消息: {}", messageContent);

        // 这里可以添加具体的业务处理代码
        // 例如: 订单超时取消、定时任务执行等
    }
}
