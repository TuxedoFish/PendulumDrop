package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.math.Vector2;

public class ToggleButton extends Button {

    public boolean enabled = false;
    public ButtonStyle styleOn, styleOff;

    public ToggleButton(String message, Vector2 location, ButtonStyle styleOn,
                        ButtonStyle styleOff,  boolean state, OnClickButtonInterface eventListener) {
        super(message, location, state ? styleOn : styleOff, eventListener);

        this.enabled = state;
        this.styleOn = styleOn;
        this.styleOff = styleOff;
    }

    @Override
    public boolean touchUp(Vector2 mousePosition) {
        if(hidden) { return false; }

        if(mousePosition.x < buttonLocation.x + style.getWidth() &&
                mousePosition.x > buttonLocation.x &&
                mousePosition.y > buttonLocation.y &&
                mousePosition.y < buttonLocation.y + style.getHeight()) {
            // Previously pressed down so when released up it is a click
            if(hover) {
                enabled = !enabled;
                this.style = enabled ? styleOn : styleOff;
                eventListener.onClick();
                hover = false;
            }
            return true;
        }

        // Started pressing down but didn't release on button
        if(hover) { hover = false; }

        return false;
    }
}