package com.example.stock.eurekaservice.service;


import com.example.stock.eurekaservice.entitie.Stock;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class StockService {

    @Value("${service.db-service.name}")
    private String serviceName;

    @Value("${spring.application.name}")
    private String applicationName;

    private RestTemplate restTemplate;
    private EurekaClient client;


    public StockService(RestTemplate restTemplate, @Qualifier("eurekaClient") EurekaClient client) {
        this.restTemplate = restTemplate;
        this.client = client;
    }

    public List<String> fallback(@PathVariable("username") String username){
        System.out.println(">>>>>>> fallback stock list: IBM,MSFT,GOOGL,AMZN,ORCL,FB");
        return Arrays.asList("IBM,MSFT,GOOGL,AMZN,ORCL,FB".split(","));
    }


    @HystrixCommand(fallbackMethod = "fallback")
    public List<String> getPreferredUserStocks(@PathVariable("username") String username) {
        InstanceInfo instanceInfo = client.getNextServerFromEureka(serviceName, false);
        String baseUrl = instanceInfo.getHomePageUrl().concat("rest/db/" + username);

        ResponseEntity<List<String>> quotesResponse = restTemplate.exchange(baseUrl, HttpMethod.GET, null
                , new ParameterizedTypeReference<List<String>>() {});

        List<String> quotes = quotesResponse.getBody();
        System.out.println(">>>>>>> stock list: "+quotes);
        return quotes;
    }


    public Stock getGoogleStocks(String quote) {
        final int random = new Random().nextInt(1000) + 20;
        InstanceInfo instanceInfo = client.getNextServerFromEureka(applicationName, false);
        String port = Integer.toString(instanceInfo.getPort());
        return new Stock(quote, 0, port, "", random/1.5);
    }

}
