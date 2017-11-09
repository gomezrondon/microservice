package com.example.stock.eurekaservice.endpoints;



import com.example.stock.eurekaservice.entitie.Stock;
import com.example.stock.eurekaservice.service.StockService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                .map(this::getGoogleStock)
                .filter(stock -> stock != null)
                .collect(Collectors.toList());

    }


    private Stock googlefallback(String quote){
        System.out.println(">>>>>>>>>>>>>>>> fallback: "+ quote);
        return null;
    }

    @HystrixCommand(fallbackMethod = "googlefallback")
    private Stock getGoogleStock(String quote){
        return stockService.getGoogleStocks(quote);
    }


}
