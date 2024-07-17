package com.jcrawley.adventuregame.service.level;

import android.content.Context;

import com.jcrawley.adventuregame.R;

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
        try(InputStream is = context.getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
            String line = reader.readLine();
            while (line != null) {
                levelParser.parse(line);
                line = reader.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();

        }

    }
}
