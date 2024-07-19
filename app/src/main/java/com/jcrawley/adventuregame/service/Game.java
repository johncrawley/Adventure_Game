package com.jcrawley.adventuregame.service;

import com.jcrawley.adventuregame.R;
import com.jcrawley.adventuregame.service.level.Level;
import com.jcrawley.adventuregame.service.level.LevelReader;
import com.jcrawley.adventuregame.service.level.Page;

public class Game {

    private GameService gameService;
    private LevelReader levelReader;
    private Level currentLevel;
    private Page currentPage;

    public void init(GameService gameService, LevelReader levelReader){
        this.gameService = gameService;
        this.levelReader = levelReader;
        currentLevel = levelReader.readLevel(R.raw.level_1);
        currentPage = currentLevel.getPage(1);
    }

    private void updateView(){
        gameService.updateView(currentPage);
    }

    public void startGame(){}

    public void quit(){

    }
}
