package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ImageStyle {
    private float width, height;
    private Texture texture;

    public ImageStyle(float width, Texture texture) {
        this.width = width;
        this.texture = texture;

        float aspectRatio = (float) texture.getHeight() /  (float) texture.getWidth();
        this.height = width * aspectRatio;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
            this.texture = texture;
        }

}
