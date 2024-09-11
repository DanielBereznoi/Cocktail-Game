package com.ridango.game.service;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebService {

    @Autowired
    private WebClient webClient;

     public String callCocktailAPI(String uri) {
       // System.err.println("url: " + uri);
        
        String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
        return StringEscapeUtils.unescapeJava(response);
    }
}
