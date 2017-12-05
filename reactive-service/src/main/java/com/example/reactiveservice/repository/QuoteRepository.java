package com.example.reactiveservice.repository;

import com.example.reactiveservice.entities.Quote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface QuoteRepository extends ReactiveMongoRepository<Quote,String> {

    Flux<Quote> findByUserName(String userName);
   //ToDO Mono<Void> deleteByUserName(String userName);

}
