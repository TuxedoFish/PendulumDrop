package com.pendulum.game.input;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Harry on 28/01/2017.
 */

public class GameGestureHandler implements GestureDetector.GestureListener {
    private ArrayList<InputEvent> events;
    Vector2 down;
    Vector2 scroll;

    public GameGestureHandler() {
        down=new Vector2();
        scroll=new Vector2();
        events = new ArrayList<InputEvent>();
    }
    public ArrayList<InputEvent> update() {
        return events;
    }

    public void clear() {
        events.clear();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        down.x=x;
        down.y=y;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(button==0) {
            events.add(new InputEvent("TAP", new Vector2(x, y)));
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        scroll.x=x;
        scroll.y=y;
        // events.add(new InputEvent("PAN", new Vector2(x, y)));
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
