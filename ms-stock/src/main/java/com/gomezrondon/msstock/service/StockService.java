package com.gomezrondon.msstock.service;


import com.gomezrondon.msstock.entitie.Quote;
import com.gomezrondon.msstock.entitie.Stock;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockService {

    @Value("${service.db-service.name}")
    private String serviceName;
    private final EurekaClient client;

    public StockService(@Qualifier("eurekaClient") EurekaClient client) {
        this.client = client;
    }

    public Flux<Stock> getStock(String username) {

        return this.getPreferredUserStocks(username)
                .map(quote -> getGoogleStocks(quote).block())
                .filter(stock -> stock != null)
                .log("Fin de getStock");
    }


    private Flux<String> getPreferredUserStocks(String username) {
        InstanceInfo instanceInfo = client.getNextServerFromEureka(serviceName, false);
        String baseUrl = instanceInfo.getHomePageUrl();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> baseUrl: "+baseUrl);

        return WebClient.create(baseUrl)
                .get().uri("react/db/jose")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(cr -> cr.bodyToFlux(Quote.class)) // bodyToFlux convert List<Quote> => Flux<Quote>
                .map(q -> q.getQuote())
                .log("Fin de getPreferredUserStocks"); // collect and convert to list (by blocking).
    }

    private Mono<Stock> getGoogleStocks(String quote) {

         return WebClient.create("https://finance.google.com/finance?q=NASDAQ:" + quote + "&output=json")
                .get()
                .exchange()
                .flatMap(cr -> cr.bodyToMono(String.class))
            //    .log("Antes de Map ")
                .map(s -> {
                    try {
                        JSONObject json = new JSONObject(s);
                        String symbol = json.getString("symbol");
                        String name = json.getString("name");
                        String exchange = json.getString("exchange");
                        int id = json.getInt("id");

                        JSONArray json2 = json.getJSONArray("related");
                        String strPrice = json2.getJSONObject(0).getString("l");
                        Double price = Double.valueOf(strPrice.replace(",", ""));
                        Stock stock = new Stock(symbol, id, exchange, name, price);
                        return stock;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                 .log("Despues de Map ");

    }

}
