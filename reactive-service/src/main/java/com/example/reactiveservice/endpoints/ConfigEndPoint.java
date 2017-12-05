package com.example.reactiveservice.endpoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class ConfigEndPoint {

    @Bean
    RouterFunction<?> routes(RouteHandlers routeHandlers){
        return nest(path("/react/db"),
                route(GET("/{username}"), routeHandlers::byUserName)
                .andRoute(POST("/add"), routeHandlers::add)
                .andRoute(DELETE("/{username}"), routeHandlers::delete));

    }

}
