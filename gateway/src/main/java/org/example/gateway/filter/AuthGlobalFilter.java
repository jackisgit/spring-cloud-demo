package org.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 检查是否包含 Authorization 头
        if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            // 这里可以添加 token 验证逻辑
            if (token != null && !token.isEmpty()) {
                return chain.filter(exchange);
            }
        }
        
        // 对于不需要认证的路径，直接放行
        String path = request.getPath().value();
        if (path.contains("/health") || path.contains("/actuator")) {
            return chain.filter(exchange);
        }
        
        // 未通过认证，返回 401
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -99;
    }
}
