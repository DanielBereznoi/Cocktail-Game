package com.ridango.game.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.ridango.game.dto.SessionDataDto;
import com.ridango.game.entity.SessionData;
import com.ridango.game.service.GameService;

@SpringBootTest
@ActiveProfiles("test")
public class GameControllerTest {

    @Autowired
    GameController gameController;

    @MockBean
    GameService gameService;

    @Test
    void test_register_user_get_game_session_data() {
        String name = "Player";
        SessionData sessionDataEntity = SessionData.builder()
                .sessionDataId(0)
                .name(name)
                .cocktailId("5")
                .currentCocktailNameFull("Cocktail")
                .currentCocktailName("________")
                .currentRecipe("Recipe")
                .build();

        SessionDataDto sessionData = SessionDataDto.builder()
                .name(name)
                .sessionDataId(0)
                .attemptsLeft(5)
                .currentCocktailName("________")
                .currentRecipe("Recipe")
                .build();

        when(gameService.register(name)).thenReturn(sessionDataEntity);
        SessionDataDto result = gameController.register(name);
        assertEquals(sessionData.toString(), result.toString());
        assertTrue(sessionData.equals(result));
    }

}
