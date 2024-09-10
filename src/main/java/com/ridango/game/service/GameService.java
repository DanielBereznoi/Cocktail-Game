package com.ridango.game.service;

import static org.mockito.ArgumentMatchers.anyBoolean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ridango.game.dto.SessionDataDto;
import com.ridango.game.entity.CocktailData;
import com.ridango.game.entity.Highscore;
import com.ridango.game.entity.SessionData;
import com.ridango.game.repository.CocktailDataRepository;
import com.ridango.game.repository.HighscoreRepository;
import com.ridango.game.repository.SessionDataRepository;

@Service
public class GameService {

    @Autowired
    private CocktailService cocktailService;

    @Autowired
    private CocktailDataRepository cocktailDataRepository;

    @Autowired
    private SessionDataRepository sessionDataRepository;

    @Autowired
    private HighscoreRepository highscoreRepository;

    @Autowired
    ModelMapper modelMapper;

    public SessionData register(String name) {
        SessionData sessionData = SessionData.builder().name(name).build();
        CocktailData cocktailData = cocktailService.getNextCocktailData();
        sessionData = insertCommonCocktailData(sessionData, cocktailData);
        cocktailData.setSession(sessionData);
        sessionData.setPastCocktails(List.of(cocktailData));
        return sessionDataRepository.save(sessionData);
    }

    public SessionData reactToMistake(SessionData sessionData) {
        String currentCocktailName = sessionData.getCurrentCocktailName();
        String fullCocktailName = sessionData.getCurrentCocktailNameFull();

        List<Integer> underscoreIndices = new ArrayList<>();
        for (int i = 0; i < currentCocktailName.length(); i++) {
            if (currentCocktailName.charAt(i) == '_') {
                underscoreIndices.add(i);
            }
        }

        if (!underscoreIndices.isEmpty()) {
            int randomIndex = underscoreIndices.get((int) (Math.random() * underscoreIndices.size()));
            StringBuilder newCurrentCocktailName = new StringBuilder(currentCocktailName);
            newCurrentCocktailName.setCharAt(randomIndex, fullCocktailName.charAt(randomIndex));
            sessionData.setCurrentCocktailName(newCurrentCocktailName.toString());
        }

        CocktailData cocktailData = cocktailDataRepository.findByCocktailId(sessionData.getCocktailId());
        String category = cocktailData.getCategory();
        List<String> shownIngredients = (sessionData.getIngredients().isEmpty()) ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(sessionData.getIngredients().split(",")));
        String[] allIngredients = cocktailData.getIngredients().split(",");
        for (int i = 0; i < allIngredients.length; i++) {
            if (!shownIngredients.contains(allIngredients[i])) {
                shownIngredients.add(allIngredients[i]);
                break;
            }
        }

        if (sessionData.getAttemptsLeft() <= 3)
            sessionData.setGlass(cocktailData.getGlass());
        if (sessionData.getAttemptsLeft() == 2)
            sessionData.setPictureURL(cocktailData.getPictureURL());

        sessionData.setAttemptsLeft(sessionData.getAttemptsLeft() - 1);
        sessionData.setCategory(category);
        sessionData.setIngredients(String.join(",", shownIngredients));
        return sessionData;
    }

    public SessionData nextRound(SessionData sessionData) {
        sessionData.setScore(sessionData.getScore() + sessionData.getAttemptsLeft());
        sessionData.setAttemptsLeft(5);
        CocktailData newCocktailData = cocktailService.getNextCocktailData();
        sessionData = insertCommonCocktailData(sessionData, newCocktailData);
        newCocktailData.setSession(sessionData);
        List<CocktailData> pastCocktails = new ArrayList<>(sessionData.getPastCocktails());
        pastCocktails.add(newCocktailData);
        sessionData.setPastCocktails(pastCocktails);
        return sessionData;
    }

    private SessionData insertCommonCocktailData(SessionData sessionData, CocktailData cocktailData) {
        sessionData.setCocktailId(cocktailData.getCocktailId());
        sessionData.setCurrentCocktailName(String.valueOf(cocktailData.getName()).replaceAll("[a-zA-Z]", "_"));
        sessionData.setCurrentCocktailNameFull(cocktailData.getName());
        sessionData.setCurrentRecipe(cocktailData.getRecipe());
        return sessionData;
    }

    public Highscore endGame(SessionData sessionData) {
        Highscore highscore = highscoreRepository.findAll().get(0);
        if (highscore.getScore() < sessionData.getScore()) {
            highscore.setPlayerName(sessionData.getName());
            highscore.setScore(sessionData.getScore());
            return highscoreRepository.save(highscore);
        }
        return highscore;
    }

    public SessionDataDto reactToAnswer(SessionDataDto sessionDataDto, String answer) {
        SessionData sessionData = sessionDataRepository.findBySessionDataId(sessionDataDto.getSessionDataId());
        if (answer.equals(sessionData.getCurrentCocktailNameFull())) {
            System.err.println(sessionData.getCurrentCocktailNameFull());
            sessionData = nextRound(sessionData);
        } else if (!answer.equals(sessionData.getCurrentCocktailNameFull())) {
            if (sessionData.getAttemptsLeft() == 1) {
                sessionData.setAttemptsLeft(0);
            } else {
                sessionData = reactToMistake(sessionData);
            }
        }
        sessionDataRepository.save(sessionData);
        return modelMapper.map(sessionData, SessionDataDto.class);
    }
}
