package com.ridango.game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ridango.game.dto.SessionDataDto;
import com.ridango.game.service.GameService;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;


    public SessionDataDto register(String name) {
        return gameService.register(name);
    
    }
    
}
