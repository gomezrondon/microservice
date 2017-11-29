package com.example.reactiveservice.service;

import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.entities.Quotes;
import reactor.core.publisher.Flux;


public interface DbPreferStokService {

    Flux<Quote> getQuotes(String username);
    void add(Quotes quotes);
}
