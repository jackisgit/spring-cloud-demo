package org.example.order.rabbitmq.controller;

import org.example.order.rabbitmq.producer.DelayMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列测试接口
 */
@RestController
@RequestMapping("/delay")
public class DelayQueueController {

    @Autowired
    private DelayMessageProducer delayMessageProducer;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 发送延迟消息
     * @param message 消息内容
     * @param delayTime 延迟时间(毫秒)
     * @return 结果
     */
    @PostMapping("/send")
    public Map<String, Object> sendDelayMessage(
            @RequestParam String message,
            @RequestParam(defaultValue = "10000") long delayTime) {

        String sendTime = LocalDateTime.now().format(FORMATTER);

        // 发送延迟消息
        delayMessageProducer.sendDelayMessage(message, delayTime);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        result.put("delayTime", delayTime + "ms");
        result.put("sendTime", sendTime);
        result.put("expectedConsumeTime", LocalDateTime.now().plusNanos(delayTime * 1_000_000).format(FORMATTER));

        return result;
    }

    /**
     * 发送默认延迟消息(10秒)
     * @param message 消息内容
     * @return 结果
     */
    @PostMapping("/send/default")
    public Map<String, Object> sendDefaultDelayMessage(@RequestParam String message) {
        return sendDelayMessage(message, 10000);
    }

    /**
     * 测试接口
     * @return 测试信息
     */
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "ok");
        result.put("message", "延迟队列服务运行正常");
        result.put("time", LocalDateTime.now().format(FORMATTER));
        return result;
    }
}
