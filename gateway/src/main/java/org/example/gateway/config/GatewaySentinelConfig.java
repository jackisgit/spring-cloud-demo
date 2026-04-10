package org.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class GatewaySentinelConfig {

    @PostConstruct
    public void doInit() {
        initGatewayRules();
    }

    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        
        rules.add(new GatewayFlowRule("user-service")
                .setCount(10)
                .setIntervalSec(1));
        
        rules.add(new GatewayFlowRule("product-service")
                .setCount(20)
                .setIntervalSec(1));
        
        rules.add(new GatewayFlowRule("order-service")
                .setCount(15)
                .setIntervalSec(1));
        
        GatewayRuleManager.loadRules(rules);
    }
    
    @Component
    public static class CustomBlockRequestHandler implements BlockRequestHandler {
        
        @Override
        public Mono<ServerResponse> handleRequest(org.springframework.web.server.ServerWebExchange exchange, Throwable ex) {
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}"));
        }
    }
}

