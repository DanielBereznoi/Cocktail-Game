package com.ridango;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ridango.game.entity.CocktailData;

public class EntityTest {

    @Test
    void test_CocktailDataComparison() {
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
        CocktailData comparand = CocktailData.builder()
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
        assertTrue(cocktailData.equals(comparand));
    }

}
