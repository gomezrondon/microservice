package com.example.stock.eurekaservice.endpoints;



import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/stock")
public class StockController {

    private EurekaClient client;

    public StockController( @Qualifier("eurekaClient") EurekaClient client) {
         this.client = client;
    }

    @GetMapping("/{username}")
    public List<Stock> getStock(@PathVariable("username") final String username){

        InstanceInfo instanceInfo = client.getNextServerFromEureka("db-service", false);
        String baseUrl = instanceInfo.getHomePageUrl().concat("rest/db/" + username);

        StockClient stocks = Feign.builder()
                .decoder(new GsonDecoder())
                .target(StockClient.class, baseUrl);

        List<String> lista = stocks.getAllQuotes();

        return  lista.stream()
                .map(this::getYahooStock)
                .collect(Collectors.toList());
    }


    private Stock getYahooStock(String quote) {
        Stock stock = null;
        try {
            stock = YahooFinance.get(quote);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }

}
