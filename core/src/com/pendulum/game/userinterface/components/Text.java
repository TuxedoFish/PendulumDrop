package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Text implements UIComponent {

    // Variables that are changed for different buttons
    private GlyphLayout text;
    private BitmapFont font;
    private Vector2 textLocation;

    /**
     * @param message text of the button
     * @param location top left coordinate of text
     */
    public Text(String message, BitmapFont font, Vector2 location) {
        // Create text object and location for rendering
        this.text = createGlyphLayout(message, font);
        this.textLocation = location;
        this.font = font;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // Render the text
        font.draw(
                spriteBatch,
                text,
                textLocation.x-(text.width/2.0f),
                textLocation.y
        );
    }

    private static GlyphLayout createGlyphLayout(String message, BitmapFont font) {
        GlyphLayout text = new GlyphLayout();
        text.setText(font, message);

        return text;
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
