package com.ridango.game.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class SessionDataTest {

    @Test
    void test_session_data_equals_different_instances() {
        SessionDataDto sessionData = SessionDataDto.builder()
                .name("Player")
                .attemptsLeft(6)
                .currentCocktailName("_")
                .currentRecipe("Recipe")
                .build();
        SessionDataDto comparand = SessionDataDto.builder()
                .name("Player")
                .attemptsLeft(6)
                .currentCocktailName("_")
                .currentRecipe("Recipe")
                .build();
        SessionDataDto wrongComparand = SessionDataDto.builder()
                .name("Player2")
                .attemptsLeft(6)
                .currentCocktailName("_")
                .currentRecipe("Recipe")
                .build();
        assertTrue(sessionData.equals(comparand));
        assertFalse(sessionData.equals(wrongComparand));
    }
}
