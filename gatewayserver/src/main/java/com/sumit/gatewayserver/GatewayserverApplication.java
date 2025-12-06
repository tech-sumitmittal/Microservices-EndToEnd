package com.sumit.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

    @Bean
    public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/sumitbank/accounts/**")
                        .filters( f -> f.rewritePath("/sumitbank/accounts/(?<segment>.*)","/${segment}"))
                        .uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path("/sumitbank/cards/**")
                        .filters( f -> f.rewritePath("/sumitbank/cards/(?<segment>.*)","/${segment}"))
                        .uri("lb://CARDS"))
                .route(p -> p
                        .path("/sumitbank/loans/**")
                        .filters( f -> f.rewritePath("/sumitbank/loans/(?<segment>.*)","/${segment}"))
                        .uri("lb://LOANS"))
                .build();
    }

}
