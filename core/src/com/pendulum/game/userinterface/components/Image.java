package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Image implements UIComponent {

    // Variables that are changed for different images
    protected Vector2 imageLocation;
    protected ImageStyle style;

    /**
     * @param location top left coordinate of button
     * @param style style describing the button
     */
    public Image(Vector2 location, ImageStyle style) {
        this.imageLocation = location;
        this.style = style;

        // Adjust buttonLocation to be the bottom left for render
        this.imageLocation = new Vector2(
                imageLocation.x,
                imageLocation.y - style.getHeight()
        );
    }

    public void setImage(ImageStyle newStyle) {
        this.style = newStyle;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
            // Render the button image
            spriteBatch.draw(
                    style.getTexture(),
                    imageLocation.x,
                    imageLocation.y,
                    style.getWidth(),
                    style.getHeight()
            );
    }

    @Override
    public boolean touchDown(Vector2 mousePosition) {
        return false;
    }

    @Override
    public boolean touchUp(Vector2 mousePosition) {
        return false;
    }

    @Override
    public void update(float dt) {

    }
}
