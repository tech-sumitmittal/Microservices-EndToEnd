package com.sumit.gatewayserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayserverApplication {

    private static final Logger log = LoggerFactory.getLogger(GatewayserverApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

    @Bean
    public RouteLocator sumitBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        log.info("Entry GatewayserverApplication.sumitBankRouteConfig : routeLocatorBuilder = {}", routeLocatorBuilder);
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/sumitbank/accounts/**")
                        .filters(f ->
                                f.rewritePath("/sumitbank/accounts/(?<segment>.*)","/${segment}")
                                .addRequestHeader("X-REQUEST-TIME", LocalDateTime.now().toString())
                                .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
                                // gateway circuit breaker
                                .circuitBreaker(config ->
                                        config.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support"))
                                // gateway retries for any idempotent GET method (drawback no fall back method)
                                .retry(r ->
                                        r.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                                // gateway rate limiter
                                .requestRateLimiter(rl ->
                                        rl.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(ipKeyResolver()))
                        )
                        .uri("http://accounts:8080"))
                .route(p -> p
                        .path("/sumitbank/cards/**")
                        .filters( f ->
                                f.rewritePath("/sumitbank/cards/(?<segment>.*)","/${segment}")
                                .addRequestHeader("X-REQUEST-TIME", LocalDateTime.now().toString())
                                .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
                                // gateway retries for any idempotent GET method (drawback no fall back method)
                                .retry(r ->
                                         r.setRetries(3)
                                         .setMethods(HttpMethod.GET)
                                         .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("http://cards:8081"))
                .route(p -> p
                        .path("/sumitbank/loans/**")
                        .filters( f ->
                                f.rewritePath("/sumitbank/loans/(?<segment>.*)","/${segment}")
                                .addRequestHeader("X-REQUEST-TIME", LocalDateTime.now().toString())
                                .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
                                // gateway retries for any idempotent GET method (drawback no fall back method)
                                .retry(r ->
                                        r.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("http://loans:8082"))
                .build();
    }


    // *********** for RateLimiter *******************************************************

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        log.info("Entry GatewayserverApplication.redisRateLimiter.");
        return new RedisRateLimiter( 5, 20, 1);
    }

    // KeyResolver : Uses client IP as key
    @Bean
    public KeyResolver ipKeyResolver() {
        log.info("Entry GatewayserverApplication.ipKeyResolver.");
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }

    // *********** for RateLimiter Ends *******************************************************


}