package com.example.reactiveservice.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Quote(@Id val id:String, val userName:String, val quote:String)

data class Quotes(val userName:String, val quotes:List<String>)