package com.example.stock.eurekaservice.entitie

data class Stock (
     val symbol: String,
     val id: Int,
     val exchange: String,
     val name: String,
     val price: Double)

