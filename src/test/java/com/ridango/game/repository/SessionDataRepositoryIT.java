package com.ridango.game.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ridango.game.entity.SessionData;
import com.ridango.game.service.GameService;


@SpringBootTest
@ActiveProfiles("test")
public class SessionDataRepositoryIT {

    @Autowired
    SessionDataRepository sessionDataRepository;


    @Autowired
    GameService gameService;

    @Test
    void test_save_session_data() {
        SessionData sessionData = SessionData.builder()
        .sessionDataId(1)
        .name("Player")
        .cocktailId("5")
        .currentCocktailNameFull("Cocktail")
        .currentCocktailName("________")
        .currentRecipe("Recipe")
        .build();

        assertTrue(sessionDataRepository.findAll().size() == 0);
        SessionData saved = sessionDataRepository.save(sessionData);
        assertTrue(sessionDataRepository.findAll().size() == 1);
        assertEquals(sessionData.toString(), saved.toString());
        assertTrue(sessionData.equals(saved));
    }

}
