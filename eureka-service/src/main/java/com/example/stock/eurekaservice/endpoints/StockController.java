package com.example.stock.eurekaservice.endpoints;



import com.example.stock.eurekaservice.service.StockService;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/stock")
public class StockController {


    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{username}")
    public List<Stock> getStock(@PathVariable("username") final String username){

        List<String> lista = stockService.getPreferredUserStocks(username);



        return  lista.stream()
                .map(this::getYahooStock)
                .filter(stock -> stock != null)
                .collect(Collectors.toList());
    }


    private Stock yahoofallback(String quote){
        System.out.println(">>>>>>>>>>>>>>>> yahoo fallback: "+ quote);
        return null;

    }

    @HystrixCommand(fallbackMethod = "yahoofallback")
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
