package com.ridango.game.service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.entity.CocktailData;

@Service
public class CocktailService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebService webService;

    public CocktailData getNextCocktailData() {
        String randomCocktailUrl = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
        CocktailData cocktailData;
        while (true) {
            String cocktailJSONString = webService.callCocktailAPI(randomCocktailUrl);
            cocktailData = parseCocktailJSON(cocktailJSONString);
            if (cocktailData != null) {
                break;
            }
        }
        // System.err.println("data: " + cocktailData.toString());
        return cocktailData;
    }

    public CocktailData parseCocktailJSON(String cocktailJSONString) {
        TypeReference<HashMap<String, List<HashMap<String, String>>>> typeRef = new TypeReference<HashMap<String, List<HashMap<String, String>>>>() {
        };
        try {
            HashMap<String, String> mappedData = mapper.readValue(cocktailJSONString, typeRef).get("drinks").get(0);
            String ingredients = IntStream.rangeClosed(1, 15)
                    .mapToObj(i -> mappedData.get("strIngredient" + i))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(","));

            return CocktailData.builder()
                    .cocktailId(mappedData.get("idDrink"))
                    .name(mappedData.get("strDrink"))
                    .category(mappedData.get("strCategory"))
                    .glass(mappedData.get("strGlass"))
                    .ingredients(ingredients.toString())
                    .pictureURL(mappedData.get("strDrinkThumb"))
                    .recipe(mappedData.get("strInstructions"))
                    .build();

        } catch (JsonProcessingException e) {
            // Me no care, me believe in god's good mood
        }
        return null;
    }

}
