package com.sumit.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

    @Bean
    public RouteLocator sumitBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/sumitbank/accounts/**")
                        .filters(f ->
                                f.rewritePath("/sumitbank/accounts/(?<segment>.*)","/${segment}")
                                .addRequestHeader("X-REQUEST-TIME", LocalDateTime.now().toString())
                                .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
                                .circuitBreaker(config ->
                                        config.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support"))
                                // gateway retries for any idempotent GET method (drawback no fall back method)
                                .retry(r ->
                                        r.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://ACCOUNTS"))
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
                        .uri("lb://CARDS"))
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
                        .uri("lb://LOANS"))
                .build();
    }

}