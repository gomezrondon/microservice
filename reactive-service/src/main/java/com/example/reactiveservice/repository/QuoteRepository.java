package com.example.reactiveservice.repository;

import com.example.reactiveservice.entities.Quote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends ReactiveMongoRepository<Quote,String> {
}
