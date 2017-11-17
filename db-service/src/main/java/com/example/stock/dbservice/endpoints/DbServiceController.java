package com.example.stock.dbservice.endpoints;

import com.example.stock.dbservice.entities.Quote;
import com.example.stock.dbservice.entities.Quotes;
import com.example.stock.dbservice.entities.SendingBean;
import com.example.stock.dbservice.repository.QuotesRepository;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@EnableBinding(Source.class)
@RestController
@RequestMapping("/rest/db")
public class DbServiceController {

    private QuotesRepository quotesRepository;

    private SendingBean sendingBean;


    public DbServiceController(QuotesRepository quotesRepository, SendingBean sendingBean) {
        this.quotesRepository = quotesRepository;
        this.sendingBean = sendingBean;
    }

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") final String username){
        sendingBean.sayHello(username);
        List<String> quotesByUserName = getQuotesByUserName(username);

        return quotesByUserName;
    }




    @PostMapping(value={"/add",MediaType.APPLICATION_JSON_VALUE} )
    public List<String> add(@RequestBody final Quotes quotes) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+quotes.toString());
        quotes.getQuotes().stream().forEach(q -> quotesRepository.save(new Quote(quotes.getUserName(),q)));



        return getQuotesByUserName(quotes.getUserName());
    }

    @DeleteMapping("/delete/{username}")
    public List<String> delete(@PathVariable("username") final String username) {
        List<Quote> quotes = quotesRepository.findByUserName(username);
        quotesRepository.delete(quotes);
        return getQuotesByUserName(username);
    }


    private List<String> getQuotesByUserName(String username) {
        return quotesRepository.findByUserName(username)
                .stream()
                .map(Quote::getQuote)
                .distinct()
                .collect(Collectors.toList());
    }

}
