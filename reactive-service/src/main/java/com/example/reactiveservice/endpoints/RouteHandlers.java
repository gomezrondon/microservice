package com.example.reactiveservice.endpoints;

import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.service.DbPreferStokService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RouteHandlers{

    private final DbPreferStokService service;

    public RouteHandlers(DbPreferStokService service) {
        this.service = service;
    }

    public Mono<ServerResponse> byUserName(ServerRequest request) {
        String username = request.pathVariable("username");
        return ServerResponse.ok()
                .body(service.getQuotes(username), Quote.class);
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return null;
    }
}