package com.ridango.game.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public SessionDataDto register(String name) {
        SessionData sessionData = SessionData.builder().name(name).build();
        CocktailData cocktailData = cocktailService.getNextCocktailData();
        
        // TODO: Delete
        //System.out.println(cocktailData.getName());
        sessionData = insertCommonCocktailData(sessionData, cocktailData);
        cocktailData.setSession(sessionData);
        sessionData.setPastCocktails(List.of(cocktailData));
        return modelMapper.map(sessionDataRepository.save(sessionData), SessionDataDto.class);
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

        CocktailData cocktailData = cocktailDataRepository.findByCocktailId(sessionData.getCocktailId()).get(0);
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
        List<String> cocktailIds = new ArrayList<>();
        sessionData.setScore(sessionData.getScore() + sessionData.getAttemptsLeft());
        sessionData.setAttemptsLeft(5);
        sessionData.getPastCocktails().forEach(coctail -> cocktailIds.add(coctail.getCocktailId()));
        CocktailData newCocktailData;
        while (true) {
            newCocktailData = cocktailService.getNextCocktailData();
            if (!cocktailIds.contains(newCocktailData.getCocktailId())) {
                break;
            }
        }
        // TODO: Delete
        //System.out.println(newCocktailData.getName());
        sessionData = insertCommonCocktailData(sessionData, newCocktailData);
        newCocktailData.setSession(sessionData);
        List<CocktailData> pastCocktails = new ArrayList<>(sessionData.getPastCocktails());
        pastCocktails.add(newCocktailData);
        sessionData.setPastCocktails(pastCocktails);
        return sessionData;
    }

    private SessionData insertCommonCocktailData(SessionData sessionData, CocktailData cocktailData) {
        sessionData.setCocktailId(cocktailData.getCocktailId());
        sessionData.setCurrentCocktailName(String.valueOf(cocktailData.getName()).replaceAll("[a-zA-Z0-9]", "_"));
        sessionData.setCurrentCocktailNameFull(cocktailData.getName());
        sessionData.setCurrentRecipe(cocktailData.getRecipe());
        return sessionData;
    }

    public Highscore endGame(SessionDataDto sessionData) {
        Highscore highscore = (!highscoreRepository.findAll().isEmpty()) ? highscoreRepository.findAll().get(0) : new Highscore(0, "", -1);
        if (highscore.getScore() < sessionData.getScore()) {
            highscore.setScoreId(0);
            highscore.setPlayerName(sessionData.getName());
            highscore.setScore(sessionData.getScore());
            return highscoreRepository.save(highscore);
        }
        return highscore;
    }

    public SessionDataDto reactToAnswer(SessionDataDto sessionDataDto, String answer) {
        SessionData sessionData = sessionDataRepository.findBySessionDataId(sessionDataDto.getSessionDataId());
        if (answer.toLowerCase().equals(sessionData.getCurrentCocktailNameFull().toLowerCase())) {
            //System.err.println(sessionData.getCurrentCocktailNameFull());
            sessionData = nextRound(sessionData);
        } else  {
            if (sessionData.getAttemptsLeft() == 1) {
                sessionData.setAttemptsLeft(0);
            } else {
                sessionData = reactToMistake(sessionData);
            }
        }
        //System.out.println("debug " + sessionData.getCurrentCocktailNameFull());
        sessionDataRepository.save(sessionData);
        return modelMapper.map(sessionData, SessionDataDto.class);
    }
}
