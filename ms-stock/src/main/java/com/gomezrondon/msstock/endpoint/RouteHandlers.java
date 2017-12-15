package com.gomezrondon.msstock.endpoint;

import com.gomezrondon.msstock.entitie.Stock;
import com.gomezrondon.msstock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.*;

@Component
public class RouteHandlers {

    private final StockService service;

    public RouteHandlers(StockService service) {
        this.service = service;
    }

    public Mono<ServerResponse> byUserName(ServerRequest request) {
        return ServerResponse.ok()
                .body(service.getStock(request.pathVariable("username")),Stock.class);
    }

}
