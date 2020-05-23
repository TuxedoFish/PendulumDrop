package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button implements UIComponent {

    // Variables that are changed for different buttons
    protected GlyphLayout text;
    protected OnClickButtonInterface eventListener;
    protected Vector2 buttonLocation;
    protected Vector2 textLocation;
    protected ButtonStyle style;

    // Is there a hover animation
    protected boolean hover = false;
    boolean hidden = false;

    /**
     *
     * @param message text of the button
     * @param location top left coordinate of button
     * @param style style describing the button
     * @param eventListener event to trigger when button is clicked
     */
    public Button(String message, Vector2 location,
                  ButtonStyle style, OnClickButtonInterface eventListener) {
        this.eventListener = eventListener;
        this.buttonLocation = location;
        this.style = style;

        // Create text object and location for rendering
        this.text = createGlyphLayout(message, style);
        this.textLocation = getCenterAlignmentOfText(text, buttonLocation, style);

        // Adjust buttonLocation to be the bottom left for render
        this.buttonLocation = new Vector2(
                buttonLocation.x,
                buttonLocation.y - style.getHeight()
        );
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        if(!hidden) {
            // Hover makes image smaller
            float size = 1.0f;
            if (hover) {
                size = 0.9f;
            }

            // Render the button image
            spriteBatch.draw(
                    style.getTexture(),
                    buttonLocation.x + ((1.0f - size) * style.getWidth() / 2.0f),
                    buttonLocation.y + ((1.0f - size) * style.getHeight() / 2.0f),
                    style.getWidth() * size,
                    style.getHeight() * size
            );

            // Render the text
            style.getFont().draw(
                    spriteBatch,
                    text,
                    textLocation.x,
                    textLocation.y
            );
        }
    }

    public void hide() {
        hidden = true;
    }

    public void show() {
        hidden = false;
    }

    private static GlyphLayout createGlyphLayout(String message, ButtonStyle style) {
        GlyphLayout text = new GlyphLayout();
        text.setText(style.getFont(), message);

        return text;
    }

    private static Vector2 getCenterAlignmentOfText(GlyphLayout text, Vector2 buttonLocation,
                                                    ButtonStyle style) {
        Vector2 centerOfButton = new Vector2(
                buttonLocation.x + ( style.getWidth() / 2.0f),
                buttonLocation.y - ( style.getHeight() / 2.0f)
        );

        Vector2 textLocation = new Vector2(
                centerOfButton.x - ( text.width / 2.0f),
                centerOfButton.y + ( text.height / 2.0f)
        );

        return textLocation;
    }

    @Override
    public boolean touchDown(Vector2 mousePosition) {
        if(hidden) { return false; }

        if(mousePosition.x < buttonLocation.x + style.getWidth() &&
                mousePosition.x > buttonLocation.x &&
                mousePosition.y > buttonLocation.y &&
                mousePosition.y < buttonLocation.y + style.getHeight()) {
            hover = true;
            return true;
        } else {
            hover = false;
            return false;
        }
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
                eventListener.onClick();
                hover = false;
            }
            return true;
        }

        // Started pressing down but didn't release on button
        if(hover) { hover = false; }

        return false;
    }

    @Override
    public void update(float dt) {

    }
}
