package com.example.stock.eurekaservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class Config {

    @LoadBalanced  //client side load balancing
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
