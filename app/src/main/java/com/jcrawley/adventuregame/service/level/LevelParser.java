package com.jcrawley.adventuregame.service.level;




public class LevelParser {

    private Level level;
    private enum Mode {PAGE_NUMBER, TEXT, CHOICES}
    private Mode currentMode = Mode.PAGE_NUMBER;
    private Page currentPage;

    public void initLevel(){
        level = new Level();
    }

    public void parse(String line){
        if(line.startsWith(" ")){
            currentMode = switch (currentMode){
                case PAGE_NUMBER -> Mode.TEXT;
                case TEXT -> Mode.CHOICES;
                case CHOICES -> Mode.PAGE_NUMBER;
            };
            return;
        }
        handlePageNumber(line);
    }

    private void handlePageNumber(String line){
        if(currentMode == Mode.PAGE_NUMBER){
            //
        }
    }

}
