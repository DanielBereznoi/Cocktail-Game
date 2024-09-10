package com.ridango.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.ridango.game.entity.CocktailData;
import com.ridango.game.entity.SessionData;
import com.ridango.game.repository.CocktailDataRepository;
import com.ridango.game.repository.SessionDataRepository;

@SpringBootTest
@ActiveProfiles("test")
public class GameServiceIT {

    @MockBean
    CocktailService cocktailService;

    @Autowired
    SessionDataRepository sessionDataRepository;

    @Autowired
    CocktailDataRepository cocktailDataRepository;

    @Autowired
    GameService gameService;

    @Test
    void IT_register_when_insertName_do_createAndSaveSessionDataAndFirstCocktail_return_sessionData() {
        CocktailData cocktaildData = CocktailData.builder()
                .name("The Jackie Welles")
                .cocktailId("2077")
                .ingredients("Vodka,Ginger Beer,Lime juice")
                .recipe("Add ice before pouring. Most importantly… don't forget a splash of love")
                .category("Cocktail")
                .pictureURL(
                        "https://static.wikia.nocookie.net/cyberpunk/images/9/94/TopQualityAlcohol9.png")
                .glass("Stemless Glass")
                .build();
        when(cocktailService.getNextCocktailData()).thenReturn(cocktaildData);
        gameService.register("Player");
        List<CocktailData> savedCocktails = cocktailDataRepository.findAll();
        List<SessionData> savedSessionData = sessionDataRepository.findAll();
        // assertEquals("savedCocktails", savedSessionData.get(0).toString());
        assertEquals(1, savedSessionData.size());
        assertEquals(1, savedCocktails.size());
    }

}
