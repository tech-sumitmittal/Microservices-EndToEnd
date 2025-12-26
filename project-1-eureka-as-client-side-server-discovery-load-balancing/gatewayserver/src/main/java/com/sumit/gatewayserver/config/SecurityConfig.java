package com.sumit.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/sumitbank/accounts/**").hasRole("ACCOUNTS")
                        .pathMatchers("/sumitbank/cards/**").hasRole("CARDS")
                        .pathMatchers("/sumitbank/loans/**").hasRole("LOANS")
                        //.pathMatchers("/sumitbank/accounts/**").authenticated()
                        //.pathMatchers("/sumitbank/cards/**").authenticated()
                        //.pathMatchers("/sumitbank/loans/**").authenticated()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        //.jwt(Customizer.withDefaults()));

                        // tells Spring Security to use our custom JWT → Authentication conversion logic instead of the default one.
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

        // there is no browsers hence disable csrf security
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }


    // Integrates our JWT role converter into Spring Security's reactive authentication pipeline (WebFlux)
    // means this method wraps the role extractor into Spring Security’s reactive pipeline
    // means this method only wires/wraps the role extractor into Spring Security (WebFlux), actual conversion logic is written into KeycloakRoleConverter class
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        // inject our custom Keycloak role extraction logic
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        // into Spring Security (WebFlux)
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}