package com.jcrawley.adventuregame.service.sound;

public enum Sound {

    KEYPAD_BUTTON(SoundType.KEYPAD),
    MENU_BUTTON(SoundType.BUTTON_PRESS),
    GAME_OVER(SoundType.GAME_EFFECT);


    private final SoundType soundType;

    Sound(SoundType soundType){
        this.soundType = soundType;
    }

    public SoundType getSoundType(){
        return soundType;
    }
}
