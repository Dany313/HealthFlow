package com.diaia.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserIdHeaderFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .map(jwtToken -> jwtToken.getToken().getSubject()) // Estrae l'UID di Firebase
                .flatMap(userId -> {
                    // Aggiunge l'header X-User-Id alla richiesta che va al microservizio
                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(r -> r.header("X-User-Id", userId))
                            .build();
                    return chain.filter(modifiedExchange);
                })
                .switchIfEmpty(chain.filter(exchange)); // Se non c'è token, continua (per rotte pubbliche)
    }
}