package com.sumit.gatewayserver.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Component
public class FilterUtility {

    public static final String TRACE_ID = "sumitbank-trace-id";

    public String generateTraceId() {
        return java.util.UUID.randomUUID().toString();
    }

    public boolean isTraceIdPresent(HttpHeaders requestHeaders) {
        if(getTraceId(requestHeaders) != null)
            return true;
        else
            return false;
    }

    public String getTraceId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(TRACE_ID) == null)
            return null;
        else {
            List<String> requestHeaderList = requestHeaders.get(TRACE_ID);
            return requestHeaderList.stream().findFirst().get();
        }
    }

    public ServerWebExchange setTraceId(ServerWebExchange exchange, String traceId) {
        return this.setRequestHeader(exchange, TRACE_ID, traceId);
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }


}