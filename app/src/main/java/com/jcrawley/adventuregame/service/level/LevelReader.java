package com.jcrawley.adventuregame.service.level;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LevelReader {

    private final Context context;
    private final LevelParser levelParser;

    public LevelReader(Context context, LevelParser levelParser){
        this.context = context;
        this.levelParser = levelParser;
    }


    public Level readLevel(int resId){
        try(InputStream is = context.getResources().openRawResource(resId)){
            return scanLevelFrom(is);
        }catch (IOException e){
            handleException(e);
        }
        return new Level();
    }


    public Level scanLevelFrom(InputStream is){
        levelParser.initLevel();
        Scanner s = new Scanner(is).useDelimiter("\\n");
        while(s.hasNext()){
            levelParser.parse(s.next());
        }
        return levelParser.getLevel();
    }


    private void handleException(IOException e){

    }

}
