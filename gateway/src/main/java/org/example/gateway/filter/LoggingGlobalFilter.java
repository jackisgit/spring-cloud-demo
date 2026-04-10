package org.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        logger.info("请求路径：{}", exchange.getRequest().getPath());
        logger.info("请求方法：{}", exchange.getRequest().getMethod());
        logger.info("请求头：{}", exchange.getRequest().getHeaders());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            logger.info("请求耗时：{}ms", (endTime - startTime));
            logger.info("响应状态码：{}", exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
