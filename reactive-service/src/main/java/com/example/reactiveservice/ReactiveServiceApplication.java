package com.example.reactiveservice;

import com.example.reactiveservice.endpoints.RouteHandlers;
import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.repository.QuoteRepository;
import com.example.reactiveservice.service.DbPreferStokService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class ReactiveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveServiceApplication.class, args);
	}

 	@Bean
	CommandLineRunner QuoteCLR(QuoteRepository quoteRepository){
		return args -> {
			quoteRepository.deleteAll().subscribe(null,null,() ->
					 Stream.of("AAPL,ADBE,ADI,ADP,ADSK,AMGN,AMZN".split(","))
					.map(q -> new Quote(UUID.randomUUID().toString(),"jose",q))
					//.peek(q -> System.out.println(q.toString()))
					.forEach(quote -> quoteRepository.save(quote).subscribe(System.out::println)));

		};
	}

}

