package com.example.reactiveservice.endpoints;


import com.example.reactiveservice.entities.Quote;
import com.example.reactiveservice.entities.Quotes;
import com.example.reactiveservice.repository.QuoteRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
public class ReactiveDbServiceController {

    private QuoteRepository quoteRepository;

    public ReactiveDbServiceController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/rest/db/{username}")
    public Mono<List<String>> getQuotes(@PathVariable("username") final String username){
        return getQuotesByuserName(username);
    }

    @PostMapping("/rest/db/add")
    public Mono<List<String>> add(@RequestBody final Quotes quotes){
        quotes.getQuotes().stream()
                .map(q -> new Quote(UUID.randomUUID().toString(),quotes.getUserName(),q))
                .forEach(quote -> quoteRepository.save(quote).subscribe(System.out::println));

        return getQuotesByuserName(quotes.getUserName());
    }

    @DeleteMapping("/rest/db/delete/{username}")
    public Mono<List<String>> delete(@PathVariable("username") final String username){

        System.out.println(">>>>>>>>>>>> deleting");
        List<Quote> quoteList = quoteRepository.findByUserName(username).collectList().block();// from reactive to blocking

        //blocking code
        quoteList.forEach(quote -> {
            System.out.println("Deleting:" + quote.toString());
            quoteRepository.delete(quote).subscribe();
        });

        return Mono.just(quoteList.stream().map(quote -> quote.getQuote()).collect(Collectors.toList()));
    }

    private Mono<List<String>> getQuotesByuserName(String userName){
        return quoteRepository.findByUserName(userName)
                .map(Quote::getQuote)
                .distinct()
                .collectList();
    }
}
