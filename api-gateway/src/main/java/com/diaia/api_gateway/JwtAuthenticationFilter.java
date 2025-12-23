package com.diaia.api_gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 1. Controlla se l'header Authorization è presente
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization Header");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization Header");
            }

            String token = authHeader.substring(7);

            try {
                // 2. Qui dovresti validare il token (per ora simuliamo la validazione)
                // In un caso reale useresti Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                if (token.equals("invalid-token-test")) {
                    throw new RuntimeException("Invalid Token");
                }

                // 3. (Opzionale) Aggiungi l'utente agli header per i microservizi a valle
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User-Id", "USER_EXTRACTED_FROM_JWT")
                        .build();

                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token not valid");
            }
        };
    }
}