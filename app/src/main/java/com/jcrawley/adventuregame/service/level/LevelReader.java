package com.jcrawley.adventuregame.service.level;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LevelReader {

    private final Context context;
    private final LevelParser levelParser;

    public LevelReader(Context context, LevelParser levelParser){
        this.context = context;
        this.levelParser = levelParser;
    }


    public void readLevel(int resId){
        levelParser.initLevel();
        try(InputStream is = context.getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
            reader.lines().forEach(levelParser::parse);
            Level level = levelParser.getLevel();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
