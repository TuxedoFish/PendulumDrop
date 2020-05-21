package com.pendulum.game.userinterface.pages;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.userinterface.components.UIComponent;

import java.util.ArrayList;

public class UIContainer implements UIComponent {
    protected ArrayList<UIComponent> components;

    protected boolean shouldRender = false;

    @Override
    public void render(SpriteBatch sb) {
        if(shouldRender) {
            for (int i = 0; i < components.size(); i++) {
                UIComponent element = components.get(i);
                element.render(sb);
            }
        }
    }

    @Override
    public void update(float dt) {
        for(int i=0; i<components.size(); i++) {
            UIComponent element = components.get(i);
            element.update(dt);
        }
    }

    @Override
    public boolean touchDown(Vector2 position) {
        boolean isUIEvent = false;

        if(shouldRender) {
            for (int i = 0; i < components.size(); i++) {
                UIComponent element = components.get(i);
                boolean isTouching = element.touchDown(position);
                isUIEvent = isUIEvent || isTouching;
            }
        }

        return isUIEvent;
    }

    @Override
    public boolean touchUp(Vector2 position) {
        boolean isUIEvent = false;

        if(shouldRender) {
            for (int i = 0; i < components.size(); i++) {
                UIComponent element = components.get(i);
                boolean isTouching = element.touchUp(position);
                isUIEvent = isUIEvent || isTouching;
            }
        }

        return isUIEvent;
    }
}
