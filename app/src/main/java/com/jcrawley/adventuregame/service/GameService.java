package com.jcrawley.adventuregame.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawley.adventuregame.MainActivity;
import com.jcrawley.adventuregame.service.level.Choice;
import com.jcrawley.adventuregame.service.level.LevelParser;
import com.jcrawley.adventuregame.service.level.LevelReader;
import com.jcrawley.adventuregame.service.level.Page;
import com.jcrawley.adventuregame.service.sound.Sound;
import com.jcrawley.adventuregame.service.sound.SoundPlayer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private MainActivity mainActivity;
    private final Game game;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> notifyGameOverFuture;
    private SoundPlayer soundPlayer;
    private LevelReader levelReader;
    private LevelParser levelParser;



    public GameService() {
        super();
        game = new Game();
    }


    public void playSound(Sound sound){
        soundPlayer.playSound(sound);
    }


    public SharedPreferences getScorePrefs(){
        return getSharedPreferences("score_preferences", MODE_PRIVATE);
    }

    public void selectChoice(int destinationPageNumber){
        game.selectChoice(destinationPageNumber);
    }

    public void notifyPageChange(){
        if(mainActivity != null){
            mainActivity.notifyPageChange();
        }
    }


    public void quitGame(){
        game.quit();
    }


    public void onGameOver(){
        // do something on main activity
    }


    public void notifyThatGameFinished(){
        notifyGameOverFuture.cancel(false);
    }


    public void startGame(){
        game.startGame();
    }


    public void updateView(Page page){
        if(mainActivity != null){
            mainActivity.updatePage(page);
        }
    }


    public Page getCurrentPage(){
        return game.getCurrentPage();
    }


    @Override
    public void onCreate() {
        soundPlayer = new SoundPlayer(getApplicationContext());
        levelParser = new LevelParser();
        levelReader = new LevelReader(getApplicationContext(), levelParser);
        game.init(this, levelReader);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onRebind(Intent intent) {
    }


    @Override
    public void onDestroy() {
        mainActivity = null;
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}
