package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface UIComponent {
    public void render(SpriteBatch spriteBatch);
    public boolean touchDown(Vector2 mousePosition);
    public boolean touchUp(Vector2 mousePosition);
    public void update(float dt);
}
