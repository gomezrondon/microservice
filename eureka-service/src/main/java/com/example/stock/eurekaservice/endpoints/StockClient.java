package com.example.stock.eurekaservice.endpoints;


import feign.RequestLine;

import java.util.List;

public interface StockClient {

    @RequestLine("GET /")
    List<String> getAllQuotes();
}
