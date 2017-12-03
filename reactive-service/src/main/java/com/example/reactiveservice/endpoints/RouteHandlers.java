package com.example.reactiveservice.endpoints;

import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.entities.Quotes;
import com.example.reactiveservice.service.DbPreferStokService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class RouteHandlers{

    private final DbPreferStokService service;

    public RouteHandlers(DbPreferStokService service) {
        this.service = service;
    }

    public Mono<ServerResponse> byUserName(ServerRequest request) {
        return ServerResponse.ok()
                .body(service.getQuotes(request.pathVariable("username")), Quote.class);
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        return ServerResponse.ok().body(request.bodyToMono(Quotes.class).doOnNext(service::add), Quotes.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {

        //return ServerResponse.ok().body(Mono.empty(),Quotes.class); // esto funciona
        return ServerResponse.ok().body(fromObject(service.deleteQuotes(request.pathVariable("username"))));
    }


}