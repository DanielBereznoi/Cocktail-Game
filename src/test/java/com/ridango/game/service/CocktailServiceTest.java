package com.ridango.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ridango.game.entity.CocktailData;

@SpringBootTest
@ActiveProfiles("test")
public class CocktailServiceTest {

        @Autowired
        CocktailService cocktailService;

        @MockBean
        WebService webService;



        @Test
        void test_parseCocktailJSON_when_insertJSON_return_FullCocktailrecord() {
                CocktailData cocktailData = CocktailData.builder()
                                .entityID(null)
                                .name("The Jackie Welles")
                                .cocktailId("2077")
                                .ingredients("Vodka,Ginger Beer,Lime juice")
                                .recipe("Add ice before pouring. Most importantly… don't forget a splash of love")
                                .category("Cocktail")
                                .pictureURL(
                                                "https://static.wikia.nocookie.net/cyberpunk/images/9/94/TopQualityAlcohol9.png")
                                .glass("Stemless Glass")
                                .build();
                String cocktailDataString = "{\"drinks\":[{\"idDrink\":\"2077\",\"strDrink\":\"The Jackie Welles\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Cocktail\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Stemless Glass\",\"strInstructions\":\"Add ice before pouring. Most importantly… don't forget a splash of love\",\"strInstructionsES\":\"Agrega hielo antes de servir. Lo más importante... no olvides una pizca de amor.\",\"strInstructionsDE\":\"Füge Eis hinzu, bevor du einschenkst. Am wichtigsten... vergiss nicht einen Spritzer Liebe.\",\"strInstructionsFR\":\"Ajoutez des glaçons avant de verser. Le plus important… n’oubliez pas une touche d'amour.\",\"strInstructionsIT\":\"Aggiungi il ghiaccio prima di versare. La cosa più importante... non dimenticare un pizzico d'amore.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/static.wikia.nocookie.net\\/cyberpunk\\/images\\/9\\/94\\/TopQualityAlcohol9.png\",\"strIngredient1\":\"Vodka\",\"strIngredient2\":\"Ginger Beer\",\"strIngredient3\":\"Lime juice\",\"strIngredient4\":null,\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 shot \",\"strMeasure2\":\"1 shot \",\"strMeasure3\":\"1 shot \",\"strMeasure4\":null,\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2024-09-09 19:29:51\"}]}";
                CocktailData parsedData = cocktailService.parseCocktailJSON(cocktailDataString);
                assertEquals(cocktailData.toString(), parsedData.toString());
                assertTrue(cocktailData.equals(parsedData));
        }

        @Test
        void test_getNextCoctailData__return_CocktailData() throws JsonMappingException, JsonProcessingException {
                String cocktailDataString = "{\"drinks\":[{\"idDrink\":\"2077\",\"strDrink\":\"The Jackie Welles\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Cocktail\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Stemless Glass\",\"strInstructions\":\"Add ice before pouring. Most importantly… don't forget a splash of love\",\"strInstructionsES\":\"Agrega hielo antes de servir. Lo más importante... no olvides una pizca de amor.\",\"strInstructionsDE\":\"Füge Eis hinzu, bevor du einschenkst. Am wichtigsten... vergiss nicht einen Spritzer Liebe.\",\"strInstructionsFR\":\"Ajoutez des glaçons avant de verser. Le plus important… n’oubliez pas une touche d'amour.\",\"strInstructionsIT\":\"Aggiungi il ghiaccio prima di versare. La cosa più importante... non dimenticare un pizzico d'amore.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/static.wikia.nocookie.net\\/cyberpunk\\/images\\/9\\/94\\/TopQualityAlcohol9.png\",\"strIngredient1\":\"Vodka\",\"strIngredient2\":\"Ginger Beer\",\"strIngredient3\":\"Lime juice\",\"strIngredient4\":null,\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 shot \",\"strMeasure2\":\"1 shot \",\"strMeasure3\":\"1 shot \",\"strMeasure4\":null,\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2024-09-09 19:29:51\"}]}";
                CocktailData cocktailData = CocktailData.builder()
                                .entityID(null)
                                .name("The Jackie Welles")
                                .cocktailId("2077")
                                .ingredients("Vodka,Ginger Beer,Lime juice")
                                .recipe("Add ice before pouring. Most importantly… don't forget a splash of love")
                                .category("Cocktail")
                                .pictureURL(
                                                "https://static.wikia.nocookie.net/cyberpunk/images/9/94/TopQualityAlcohol9.png")
                                .glass("Stemless Glass")
                                .build();
                HashMap<String, String> drinkMap =  new HashMap<>(Map.ofEntries(
                        entry("idDrink", "2077"),
                        entry("strDrink", "The Jackie Welles"),
                        entry("strCategory", "Cocktail"),
                        entry("strGlass", "Stemless Glass"),
                        entry("strInstructions", "Add ice before pouring. Most importantly… don't forget a splash of love"),
                        entry("strInstructionsES", "Agrega hielo antes de servir. Lo más importante... no olvides una pizca de amor."),
                        entry("strInstructionsDE", "Füge Eis hinzu, bevor du einschenkst. Am wichtigsten... vergiss nicht einen Spritzer Liebe."),
                        entry("strInstructionsFR", "Ajoutez des glaçons avant de verser. Le plus important… n’oubliez pas une touche d'amour."),
                        entry("strInstructionsIT", "Aggiungi il ghiaccio prima di versare. La cosa più importante... non dimenticare un pizzico d'amore."),
                        entry("strDrinkThumb", "https://static.wikia.nocookie.net/cyberpunk/images/9/94/TopQualityAlcohol9.png"),
                        entry("strIngredient1", "Vodka"),
                        entry("strIngredient2", "Ginger Beer"),
                        entry("strIngredient3", "Lime juice"),
                        entry("strIngredient4", "null"),
                        entry("strIngredient5", "null"),
                        entry("strIngredient6", "null"),
                        entry("strIngredient7", "null")
                ));

                HashMap<String, List<HashMap<String, String>>> mockResponse = new HashMap<>();
                mockResponse.put("drinks", List.of(drinkMap));
                CocktailService mockedService = Mockito.spy(cocktailService);
               
                when(webService.callCocktailAPI(anyString()))
                                .thenReturn(cocktailDataString);
                Mockito.when(mockedService.parseCocktailJSON(cocktailDataString)).thenReturn(cocktailData);

                //when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(mockResponse);
                CocktailData result = cocktailService.getNextCocktailData();
                assertTrue(cocktailData.equals(result));
        }

      
}
