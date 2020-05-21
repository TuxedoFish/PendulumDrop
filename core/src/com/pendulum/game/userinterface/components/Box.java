package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Box implements UIComponent {

    // Constants
    Vector2 size;
    Vector2 location;
    Texture background;

    /**
     *
     * @param size Size of the box in pixels
     * @param location Top left location of box
     * @param background The texture to render of the menu
     */
    public Box(Vector2 size, Vector2 location, Texture background) {
        this.size = size;
        this.background = background;

        // Make the location the bottom left
        this.location = new Vector2(
                location.x,
                location.y - size.y
        );
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(
                background,
                location.x,
                location.y,
                size.x,
                size.y
        );
    }

    @Override
    public void update(float dt) {

    }


    @Override
    public boolean touchDown(Vector2 mousePosition) {
        if(mousePosition.x < location.x + size.x &&
                mousePosition.x > location.x &&
                mousePosition.y > location.y &&
                mousePosition.y < location.y + size.y) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(Vector2 mousePosition) {
        if(mousePosition.x < location.x + size.x &&
                mousePosition.x > location.x &&
                mousePosition.y > location.y &&
                mousePosition.y < location.y + size.y) {
            return true;
        }

        return false;
    }
}
