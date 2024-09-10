package com.ridango.game.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ridango.game.dto.SessionDataDto;
import com.ridango.game.entity.SessionData;
import com.ridango.game.service.GameService;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ModelMapper modelMapper;

    public SessionDataDto register(String name) {
        SessionData sessionData =  gameService.register(name);
        
        return modelMapper.map(sessionData, SessionDataDto.class);
    }
    
}
