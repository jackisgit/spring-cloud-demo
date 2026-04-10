package org.example.order.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列配置
 * 使用死信队列实现延迟队列功能
 */
@Configuration
public class DelayQueueConfig {

    /**
     * 正常队列名称
     */
    public static final String NORMAL_QUEUE = "order.normal.queue";

    /**
     * 死信队列名称(实际消费队列)
     */
    public static final String DEAD_LETTER_QUEUE = "order.dead.letter.queue";

    /**
     * 正常交换机
     */
    public static final String NORMAL_EXCHANGE = "order.normal.exchange";

    /**
     * 死信交换机
     */
    public static final String DEAD_LETTER_EXCHANGE = "order.dead.letter.exchange";

    /**
     * 正常队列路由键
     */
    public static final String NORMAL_ROUTING_KEY = "order.normal.routing.key";

    /**
     * 死信队列路由键
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "order.dead.letter.routing.key";

    /**
     * 声明死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明正常交换机
     */
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    /**
     * 声明死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**
     * 声明正常队列,并设置死信交换机和路由键
     * 消息过期后会自动转发到死信交换机
     */
    @Bean
    public Queue normalQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        // 设置消息过期时间(毫秒),这里设置为10秒作为示例
        // 实际使用时可以在发送消息时动态设置
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定正常队列到正常交换机
     */
    @Bean
    public Binding normalBinding(Queue normalQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with(NORMAL_ROUTING_KEY);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }
}
