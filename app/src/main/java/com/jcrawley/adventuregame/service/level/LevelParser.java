package com.jcrawley.adventuregame.service.level;


import java.util.ArrayList;
import java.util.List;

public class LevelParser {
    private Level level;
    private final int EMPTY_PAGE_NUMBER = -1;
    private int currentPageNumber = EMPTY_PAGE_NUMBER;
    private StringBuilder currentTextStr = new StringBuilder();
    private List<Choice> currentChoices = new ArrayList<>();

    public void initLevel(){
        level = new Level();
    }


    public Level getLevel(){
        saveExistingPage();
        return level;
    }


    public void parse(String line){
        line = line.trim();
        handleFirstLine(line);
        addPlainLineToText(line);
        handleChoiceLine(line);
    }


    private void handleFirstLine(String line){
        if(isFirstLineInPage(line)){
            saveExistingPage();
            parsePageNumberAndFirstLineFrom(line);
        }
    }


    private void saveExistingPage(){
        if(currentTextStr.length() == 0 || currentPageNumber < 0){
            return;
        }
        level.addPage(new Page(currentPageNumber, currentTextStr.toString().trim(), currentChoices));
        currentChoices = new ArrayList<>();
        currentTextStr = new StringBuilder();
        currentPageNumber = -1;
    }


    private void parsePageNumberAndFirstLineFrom(String line){
        String[] results = line.split("\\.", 2);
        String pageNumberStr = results[0].substring(1).trim();
        currentPageNumber = Integer.parseInt(pageNumberStr);
        currentTextStr.append(results[1].trim());
    }


    private void handleChoiceLine(String line){
        if(isChoiceLine(line)){
            String choiceMarker = "-";
            String choiceStr = line.substring(line.indexOf(choiceMarker));
            String[] data = choiceStr.split(",");
            int destinationPageNumber = Integer.parseInt(data[1].trim());
            int choiceMarkerIndex = data[0].indexOf(choiceMarker);
            if(choiceMarkerIndex > data[0].length() - 2){
                return;
            }

            String choiceLabel = data[0].substring(choiceMarkerIndex + 1).trim();
            currentChoices.add(new Choice(destinationPageNumber, choiceLabel));
        }
    }


    private void addPlainLineToText(String line){
        if(!isFirstLineInPage(line) && !isChoiceLine(line)){
            if(currentTextStr.length() != 0){
                currentTextStr.append("\n");
            }
            currentTextStr.append(line.isBlank() ? "\n" : line);
        }
    }


    private boolean isFirstLineInPage(String line){
        return line.startsWith("[");
    }


    private boolean isChoiceLine(String line){
        return line.startsWith("-");
    }

}
