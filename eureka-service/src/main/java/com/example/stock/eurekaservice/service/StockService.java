package com.example.stock.eurekaservice.service;


import com.example.stock.eurekaservice.entitie.Stock;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class StockService {

    @Value("${service.db-service.name}")
    private String serviceName;

    private RestTemplate restTemplate;
    private EurekaClient client;
    private RestTemplateBuilder restTemplateBuilder;

    public StockService(RestTemplate restTemplate, @Qualifier("eurekaClient") EurekaClient client, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplate;
        this.client = client;
        this.restTemplateBuilder = restTemplateBuilder;
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

        String baseUrl = "https://finance.google.com/finance?q=NASDAQ:"+quote+"&output=json";
        ResponseEntity<String> quotesResponse = restTemplate.exchange(baseUrl, HttpMethod.GET, null,String.class);
        String data = quotesResponse.getBody();

        try {
            JSONObject json = new JSONObject(data);
            String symbol = json.getString("symbol");
            String name = json.getString("name");
            String exchange = json.getString("exchange");
            int id = json.getInt("id");

            JSONArray json2 =   json.getJSONArray("related");
            String strPrice =json2.getJSONObject(0).getString("l");
            Double price = Double.valueOf(strPrice.replace(",",""));
            Stock stock = new Stock(symbol, id, exchange, name, price);
            return stock;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

}
