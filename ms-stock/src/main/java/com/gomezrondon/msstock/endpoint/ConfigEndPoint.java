package com.gomezrondon.msstock.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class ConfigEndPoint {

    @Bean
    RouterFunction<?> routes(RouteHandlers routeHandlers){
        return nest(path("/react/stock"),
                route(GET("/{username}"), routeHandlers::byUserName));

    }

}
