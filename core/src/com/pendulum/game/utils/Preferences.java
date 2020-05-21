package com.pendulum.game.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by Harry on 03/02/2017.
 */

public class Preferences {
    public final static String PREFERENCE_NAME_OPTION = "options";
    public final static String HIGHSCORE = "score";
    public final static String PELLETS = "pellets";
    public final static String UNLOCKED = "unlocked";
    public final static String INSTRUCTION = "instruction";
    public final static String SLALOM_INSTRUCTION = "slalom_instruction";
    public final static String SWINGS_INSTRUCTION = "swings_instruction";
    public final static String SELECTED = "selected";

    private com.badlogic.gdx.Preferences optionPref = Gdx.app.getPreferences(PREFERENCE_NAME_OPTION);

    public Preferences() {
        optionPref = Gdx.app.getPreferences(PREFERENCE_NAME_OPTION);
    }
    public int getHighScore() {
        return optionPref.getInteger(HIGHSCORE);
    }
    public void setHighScore(int highScore) {
        optionPref.putInteger(HIGHSCORE, highScore);
        optionPref.flush();
    }
    public int getSelected() {
        return optionPref.getInteger(SELECTED);
    }
    public void setSelected(int i) {
        optionPref.putInteger(SELECTED, i);
        optionPref.flush();
    }
    public void setInstruction(String instruction, int new_level) {
        optionPref.putInteger(instruction, new_level);
        optionPref.flush();
    }
    public int getInstruction(String instruction) {
        return optionPref.getInteger(instruction);
    }
    public void setUnlocked(int i, boolean value) {
        optionPref.putBoolean(UNLOCKED + String.valueOf(i), value);
        optionPref.flush();
    }
    public boolean getUnlocked(int i) {
        return optionPref.getBoolean(UNLOCKED + String.valueOf(i));
    }
    public int getPellets() {
        return optionPref.getInteger(PELLETS);
    }
    public void setPellets(int pellets) {
        optionPref.putInteger(PELLETS, pellets);
        optionPref.flush();
    }
}
