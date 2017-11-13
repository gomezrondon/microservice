package com.example.reactiveservice.service;

import com.example.reactiveservice.entities.Quote;
import reactor.core.publisher.Flux;


public interface DbPreferStokService {

    public Flux<Quote> getQuotes(String username);

}
