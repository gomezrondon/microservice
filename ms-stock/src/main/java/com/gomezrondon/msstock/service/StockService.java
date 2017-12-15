package com.gomezrondon.msstock.service;


import com.gomezrondon.msstock.entitie.Quote;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;



@Service
public class StockService {

    @Value("${service.db-service.name}")
    private String serviceName;
    private final EurekaClient client;

    public StockService(@Qualifier("eurekaClient") EurekaClient client) {
        this.client = client;
    }

    @Bean
    WebClient client(){
        InstanceInfo instanceInfo = client.getNextServerFromEureka(serviceName, false);
        String baseUrl = instanceInfo.getHomePageUrl();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> baseUrl: "+baseUrl);
        return WebClient.create(baseUrl);
    }

    public List<String> getPreferredUserStocks(WebClient client) {

        List<String> stringList = client.get().uri("react/db/jose")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(cr -> cr.bodyToFlux(Quote.class)) // bodyToFlux convert List<Quote> => Flux<Quote>
                .map(q -> q.getQuote())
                .collectList().block(); // collect and convert to list (by blocking).


        return stringList;

    }

}
