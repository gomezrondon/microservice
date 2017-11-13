package com.example.reactiveservice.endpoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class ConfigEndPoint {

    @Bean
    RouterFunction<?> routes(RouteHandlers routeHandlers){
        return RouterFunctions.route(RequestPredicates.GET("/react/db/{username}"), routeHandlers::byUserName);
    }

}
