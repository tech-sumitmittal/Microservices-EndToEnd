package com.sumit.gatewayserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    // Creating and adding trace id in the request

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

        if (!filterUtility.isTraceIdPresent(requestHeaders)) {
            String traceId = filterUtility.generateTraceId();
            exchange = filterUtility.setTraceId(exchange, traceId);
            logger.debug("trace-id generated in RequestTraceFilter: {}", traceId);
        }
        else {
            logger.debug("trace-id found in RequestTraceFilter: {}", filterUtility.getTraceId(requestHeaders));
        }

        return chain.filter(exchange);
    }

}