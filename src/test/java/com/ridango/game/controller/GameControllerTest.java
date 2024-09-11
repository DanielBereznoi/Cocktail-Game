package com.ridango.game.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class GameControllerTest {

    //@Autowired
    //GameController gameController;
//
    //@MockBean
    //GameService gameService;
//
    //@Test
    //void test_register_user_get_game_session_data() {
    //    String name = "Player";
    //    SessionData sessionDataEntity = SessionData.builder()
    //            .sessionDataId(0)
    //            .name(name)
    //            .cocktailId("5")
    //            .currentCocktailNameFull("Cocktail")
    //            .currentCocktailName("________")
    //            .currentRecipe("Recipe")
    //            .build();
//
    //    SessionDataDto sessionData = SessionDataDto.builder()
    //            .name(name)
    //            .sessionDataId(0)
    //            .attemptsLeft(5)
    //            .currentCocktailName("________")
    //            .currentRecipe("Recipe")
    //            .build();
//
    //    when(gameService.register(name)).thenReturn(sessionDataEntity);
    //    SessionDataDto result = gameController.register(name);
    //    assertEquals(sessionData.toString(), result.toString());
    //    assertTrue(sessionData.equals(result));
    //}

}
