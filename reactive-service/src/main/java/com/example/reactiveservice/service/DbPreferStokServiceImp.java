package com.example.reactiveservice.service;

import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.entities.Quotes;
import com.example.reactiveservice.repository.QuoteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

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

    @Override
    public void add(Quotes quotes) {
        quotes.getQuotes().stream()
                .map(q -> new Quote(UUID.randomUUID().toString(),quotes.getUserName(),q))
                .forEach(quote -> quoteRepository.save(quote).subscribe(System.out::println));
    }


    private Flux<Quote> getQuotesByuserName(String userName){
        return quoteRepository.findByUserName(userName).distinct();
    }

}
