package com.jcrawley.adventuregame.service.level;


import java.util.ArrayList;
import java.util.List;

public class LevelParser {

    private Level level;
    public enum ParseState {PAGE_NUMBER, TEXT, CHOICES}
    private ParseState currentParseState = ParseState.PAGE_NUMBER;
    private final int EMPTY_PAGE_NUMBER = -1;
    private int currentPageNumber = EMPTY_PAGE_NUMBER;
    private String currentText = "";
    private List<Choice> currentChoices;


    public void initLevel(){
        level = new Level();
    }


    public Level getLevel(){
        addCurrentPageToLevel();
        return level;
    }


    public void parse(String line){
        if(line.startsWith(" ")){
            updateState();
            return;
        }
        switch (currentParseState){
            case PAGE_NUMBER -> parsePageNumberFrom(line);
            case TEXT -> parseTextFrom(line);
            case CHOICES -> parseChoiceFrom(line);
        }
    }


    private void updateState(){
        currentParseState = switch (currentParseState){
            case PAGE_NUMBER -> currentParseState;
            case TEXT -> ParseState.CHOICES;
            case CHOICES -> ParseState.PAGE_NUMBER;
        };
    }


    private void reset(){
        currentPageNumber = EMPTY_PAGE_NUMBER;
        currentText = "";
        currentChoices = new ArrayList<>();
    }


    private void parsePageNumberFrom(String line){
        addCurrentPageToLevel();
        reset();
        parseNumber(line);
        currentParseState = ParseState.TEXT;
    }


    private void parseNumber(String line){
        currentPageNumber = Integer.parseInt(line.trim());
    }


    private void addCurrentPageToLevel(){
        if(currentPageNumber != EMPTY_PAGE_NUMBER){
            level.addPage(new Page(currentPageNumber, currentText, currentChoices));
        }
    }


    private void parseTextFrom(String line){
        if(!currentText.isBlank()){
            currentText += "\n";
        }
        currentText += line;
    }


    private void parseChoiceFrom(String line){
        String[] segments = line.split(",");
        int destinationPageNumber = Integer.parseInt(segments[1].trim());
        Choice choice = new Choice(destinationPageNumber, segments[0].trim());
        currentChoices.add(choice);
    }

}
