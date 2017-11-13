package com.example.reactiveservice.service;

import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.repository.QuoteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DbPreferStokServiceImp implements DbPreferStokService{

    private QuoteRepository quoteRepository;

    public DbPreferStokServiceImp(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public Flux<Quote> getQuotes(String username) {
        return getQuotesByuserName(username);
    }


    private Flux<Quote> getQuotesByuserName(String userName){
        return quoteRepository.findByUserName(userName).distinct();
    }

}
