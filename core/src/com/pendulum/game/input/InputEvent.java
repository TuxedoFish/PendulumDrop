package com.pendulum.game.input;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Harry on 08/01/2017.
 */

public class InputEvent {
    private String TYPE;
    private Vector2 POSITION;

    public InputEvent(String TYPE, Vector2 POSITION) {
        this.TYPE = TYPE;
        this.POSITION = POSITION;
    }

    public String getTYPE () {
        return TYPE;
    }
    public Vector2 getPOSITION () {
        return POSITION;
    }
}
