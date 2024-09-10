package com.ridango.game.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import com.ridango.game.dto.SessionDataDto;
import com.ridango.game.entity.CocktailData;
import com.ridango.game.entity.Highscore;
import com.ridango.game.entity.SessionData;
import com.ridango.game.repository.CocktailDataRepository;
import com.ridango.game.repository.HighscoreRepository;
import com.ridango.game.repository.SessionDataRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class GameServiceTest {

  @Autowired
  GameService gameService;

  @MockBean
  CocktailService cocktailService;

  @MockBean
  CocktailDataRepository mockedCocktailRepo;

  @MockBean
  SessionDataRepository sessionDataRepository;

  @MockBean
  HighscoreRepository highscoreRepository;

  SessionData sessionData;
  CocktailData cocktailData;
  SessionData nextRoundData;
  CocktailData nextCocktailData;

  @BeforeEach
  void setUp() {

    cocktailData = buildCocktailData("The Jackie Welles", "2077", "Vodka,Ginger Beer,Lime juice");
    sessionData = buildSessionData(1, "Player", cocktailData, "___ ______ ______", 0, 5, "", "");
    sessionData.setPastCocktails(List.of(cocktailData));

    nextCocktailData = buildCocktailData("The Johnny Silverhand", "2023", "Tequila,Chilli garnish,Beer");
    nextRoundData = buildSessionData(1, "Player", nextCocktailData, "___ ______ __________", 5, 5, "", "");
    nextRoundData.setPastCocktails(List.of(cocktailData, nextCocktailData));

  }

  private CocktailData buildCocktailData(String name, String id, String ingredients) {
    return CocktailData.builder()
        .name(name)
        .cocktailId(id)
        .ingredients(ingredients)
        .recipe("Sample Recipe")
        .category("Cocktail")
        .pictureURL("https://image.url")
        .glass("Glass Type")
        .build();
  }

  private SessionData buildSessionData(int id, String name, CocktailData cocktailData, String currentName, int score,
      int attemptsLeft, String ingredients, String category) {
    return SessionData.builder()
        .sessionDataId(id)
        .name(name)
        .cocktailId(cocktailData.getCocktailId())
        .currentCocktailNameFull(cocktailData.getName())
        .currentCocktailName(currentName)
        .currentRecipe(cocktailData.getRecipe())
        .attemptsLeft(attemptsLeft)
        .ingredients(ingredients)
        .category(category)
        .score(score)
        .build();
  }

  private SessionDataDto buildSessionDataDto(int sessionDataId, int attemptsLeft, String ingredients,
      String currentCocktailname, String pictureUrl, String glass, String category, int score) {
    return SessionDataDto.builder()
        .sessionDataId(1)
        .name("Player")
        .attemptsLeft(attemptsLeft)
        .ingredients(ingredients)
        .category(category)
        .currentCocktailName(currentCocktailname)
        .currentRecipe("Sample Recipe")
        .pictureURL(pictureUrl)
        .glass(glass)
        .score(score)
        .build();
  }

  @Test
  void test_register_when_insertName_do_createAndSaveSessionDataAndFirstCocktail_return_sessionData() {
    when(cocktailService.getNextCocktailData()).thenReturn(cocktailData);
    when(sessionDataRepository.save(any(SessionData.class))).thenReturn(sessionData);

    SessionData result = gameService.register(sessionData.getName());
    assertTrue(sessionData.equals(result));
    List<CocktailData> savedCocktails = result.getPastCocktails();
    assertEquals(1, savedCocktails.size());
  }

  @Test
  void test_reactToMistakeOrSkip_when_insertSessionDataFirstOrSecondMistake_return_sessionDataWithClues() {
    when(mockedCocktailRepo.findByCocktailId(sessionData.getCocktailId())).thenReturn(cocktailData);
    SessionData result = gameService.reactToMistake(sessionData);
    test_reactToMistakeOrSkip_commonAsserts(result, cocktailData, 1, 4, 1);
  }

  @Test
  void test_reactToMistakeOrSkip_when_insertSessionDataThirdMistake_return_sessionDataWithClues() {
    sessionData.setAttemptsLeft(3);
    sessionData.setIngredients("Vodka,Lime juice");
    sessionData.setCurrentCocktailName("T__ __l___ ______");
    when(mockedCocktailRepo.findByCocktailId(sessionData.getCocktailId())).thenReturn(cocktailData);
    SessionData result = gameService.reactToMistake(sessionData);
    test_reactToMistakeOrSkip_commonAsserts(result, cocktailData, 3, 2, 2);
    assertEquals(cocktailData.getGlass(), result.getGlass(), "Glass mismatch");
  }

  @Test
  void test_reactToMistakeOrSkip_when_insertSessionDataFourthMistake_return_sessionDataWithClues() {
    sessionData.setAttemptsLeft(2);
    sessionData.setIngredients("Vodka,Lime juice,Ginger Beer");
    sessionData.setCurrentCocktailName("T__ __l___ W_____");
    when(mockedCocktailRepo.findByCocktailId(sessionData.getCocktailId())).thenReturn(cocktailData);
    SessionData result = gameService.reactToMistake(sessionData);
    test_reactToMistakeOrSkip_commonAsserts(result, cocktailData, 3, 1, 3);
    assertEquals(cocktailData.getGlass(), result.getGlass(), "Glass mismatch");
    assertEquals(cocktailData.getPictureURL(), result.getPictureURL(), "Cocktail picture url mismatch");
  }

  void test_reactToMistakeOrSkip_commonAsserts(SessionData result, CocktailData cocktailData, int ingredientNr,
      int attemptsLeft, int revealedCharNr) {
    assertEquals(cocktailData.getCategory(), result.getCategory(), "Category mismatch");
    assertEquals(ingredientNr, result.getIngredients().split(",").length, "Incorrect number of ingredients");
    assertEquals(attemptsLeft, sessionData.getAttemptsLeft(), "Incorrect number of attempts left");
    assertTrue(result.getCurrentCocktailName().replace("[_ \\W]", "").length() >= revealedCharNr,
        "Incorrect number of characters revealed");
  }

  @Test
  void test_nextRound_when_insertSessionData_start_nextRound() {
    when(cocktailService.getNextCocktailData()).thenReturn(nextCocktailData);
    SessionData result = gameService.nextRound(sessionData);
    assertEquals(nextRoundData.toString(), result.toString());
    assertTrue(nextRoundData.equals(result));
  }

  @Test
  void test_endGame_noHighscore() {
    Highscore highscore = new Highscore(9, "Player", 23);
    when(highscoreRepository.findAll()).thenReturn(List.of(highscore));
    Highscore result = gameService.endGame(sessionData);
    assertTrue(highscore.equals(result));
  }

  @Test
  void test_endGame_newHighscore() {
    Highscore highscore = new Highscore(9, "Player", -23);
    Highscore newHighscore = new Highscore(9, "Player", 0);
    when(highscoreRepository.findAll()).thenReturn(List.of(highscore));
    when(highscoreRepository.save(highscore)).thenReturn(newHighscore);
    Highscore result = gameService.endGame(sessionData);
    assertTrue(newHighscore.equals(result));
  }

  @Test
  void test_reactToAnswer_when_wrongAnswerAndSessionDataDto_do_revealClues_return_sessionDataDto() {
    SessionDataDto sessionDataDto = buildSessionDataDto(1, 5, "", "___ ______ ______", "", "", "", 0);
    SessionDataDto newSessionDataDto = buildSessionDataDto(1, 4, "Vodka", "___ _a____ ______", "", "", "Cocktail", 0);

    SessionData newSessionData = buildSessionData(1, "Player", cocktailData, "___ _a____ ______", 0, 4, "Vodka",
        "Cocktail");
    String answer = "Wrong";
    GameService gameServiceSpy = Mockito.spy(gameService);

    doReturn(sessionData).when(sessionDataRepository).findBySessionDataId(sessionDataDto.getSessionDataId());
    doReturn(cocktailData).when(mockedCocktailRepo).findByCocktailId(sessionData.getCocktailId());
    doReturn(newSessionData).when(gameServiceSpy).reactToMistake(sessionData);
    Mockito.when(sessionDataRepository.save(any(SessionData.class))).thenReturn(newSessionData);

    SessionDataDto result = gameServiceSpy.reactToAnswer(sessionDataDto, answer);
    assertEquals(newSessionDataDto.toString(), result.toString());
    assertTrue(newSessionDataDto.equals(result));
  }

  @Test
  void test_reactToAnswer_when_rightAnswer_do_fetchNewCocktailData_return_nextRoundSessionData() {
    SessionDataDto sessionDataDto = buildSessionDataDto(1, 5, "", "___ ______ ______", "", "", "", 0);
    SessionDataDto newSessionDataDto = buildSessionDataDto(1, 5, "", "___ ______ __________", "", "", "", 5);

    String answer = "The Jackie Welles";
    GameService gameServiceSpy = Mockito.spy(gameService);

    doReturn(sessionData).when(sessionDataRepository).findBySessionDataId(sessionDataDto.getSessionDataId());
    when(cocktailService.getNextCocktailData()).thenReturn(nextCocktailData);
    doReturn(nextRoundData).when(gameServiceSpy).nextRound(sessionData);

    SessionDataDto result = gameServiceSpy.reactToAnswer(sessionDataDto, answer);
    assertEquals(newSessionDataDto.toString(), result.toString());
    assertTrue(newSessionDataDto.equals(result));
  }
  @Test
  void test_reactToAnswer_when_wrongAnswerNoAttemptsLeft_do_removeAllAttempts_return_sessionDataDto() {
    SessionDataDto sessionDataDto = buildSessionDataDto(1, 1, "", "___ ______ ______", "", "", "", 0);
    SessionDataDto newSessionDataDto = buildSessionDataDto(1, 0, "", "___ ______ ______", "", "", "", 0);
    SessionData noAttemptsData = buildSessionData(1, "Player", cocktailData, "___ ______ ______", 0, 1, "",
    "");
    String answer = "Wrong";
    GameService gameServiceSpy = Mockito.spy(gameService);

    doReturn(noAttemptsData).when(sessionDataRepository).findBySessionDataId(sessionDataDto.getSessionDataId());
 
    SessionDataDto result = gameServiceSpy.reactToAnswer(sessionDataDto, answer);
    assertEquals(newSessionDataDto.toString(), result.toString());
    assertTrue(newSessionDataDto.equals(result));
  }


}
