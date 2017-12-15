package com.gomezrondon.msstock;

import com.gomezrondon.msstock.entitie.Quote;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@EnableEurekaClient
@SpringBootApplication
public class MsStockApplication {

/* // for testing
	@Bean
	CommandLineRunner demo (WebClient client){
		return args -> {
			client.get().uri("react/db/jose")
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.flatMapMany(cr -> cr.bodyToFlux(Quote.class))  // bodyToFlux convert List<Quote> => Flux<Quote>
					.subscribe(System.out::println);

		};
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(MsStockApplication.class, args);
	}
}
