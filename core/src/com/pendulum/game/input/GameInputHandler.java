package com.pendulum.game.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Harry on 08/01/2017.
 */

public class GameInputHandler implements InputProcessor{
    private ArrayList<InputEvent> events;

    public GameInputHandler() {
        events = new ArrayList<InputEvent>();
    }
    public ArrayList<InputEvent> update() {
        return events;
    }

    public void clear() {
        events.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer==0) {
            events.add(new InputEvent("TOUCH_DOWN", new Vector2(screenX, screenY)));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer==0) {
            events.add(new InputEvent("TOUCH_UP", new Vector2(screenX, screenY)));
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
