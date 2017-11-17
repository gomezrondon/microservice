package com.example.stock.dbservice.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SendingBean {

    private Source source;

    @Autowired
    public SendingBean(Source source) {
        this.source = source;
    }

    public void sayHello(String name){
        source.output().send(MessageBuilder.withPayload(name).build());
        System.out.println(">>>>>>> Message sent:" + name);
    }

}
