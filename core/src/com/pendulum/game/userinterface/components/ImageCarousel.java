package com.pendulum.game.userinterface.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.texture.TextureHolder;
import com.pendulum.game.userinterface.UIController;

import java.util.ArrayList;

public class ImageCarousel implements UIComponent {

    // Constants
    private ArrayList<ImageStyle> images;
    private Image currentImage;
    private int index = 0;
    private Button leftButton;
    private Button rightButton;

    public ImageCarousel(Vector2 topLeftPosition, ArrayList<ImageStyle> images,
         Vector2 dimensions, TextureHolder textures) {

        ImageStyle style = images.get(0);
        currentImage = new Image(topLeftPosition, images.get(0));
        Vector2 imageSizing = new Vector2(style.getWidth(), style.getHeight());

        // Create button styles
        ButtonStyle leftButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.25f, 0.0f, dimensions).x,
                textures.getTexture("button_left_arrow.png"),
                new BitmapFont()
        );
        ButtonStyle rightButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.25f, 0.0f, dimensions).x,
                textures.getTexture("button_right_arrow.png"),
                new BitmapFont()
        );

        // Create buttons
        leftButton  = new Button(
                "",
                topLeftPosition.add(0.0f, -imageSizing.y-50.0f),
                leftButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        updateIndex(index-1);
                    }
                }
        );
        rightButton  = new Button(
                "",
                topLeftPosition.add(imageSizing.x - rightButtonStyle.getWidth(), 0.0f),
                rightButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        updateIndex(index+1);
                    }
                }
        );

        this.images = images;
    }

    private void updateIndex(int newIndex) {
        if(newIndex<images.size() && newIndex>=0) {
            index = newIndex;
            currentImage.setImage(images.get(index));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        currentImage.render(sb);
        leftButton.render(sb);
        rightButton.render(sb);
    }

    @Override
    public boolean touchDown(Vector2 mousePosition) {
        return leftButton.touchDown(mousePosition) || rightButton.touchDown(mousePosition);
    }

    @Override
    public boolean touchUp(Vector2 mousePosition) {
        return leftButton.touchUp(mousePosition) || rightButton.touchUp(mousePosition);
    }

    @Override
    public void update(float dt) {
        currentImage.update(dt);
        leftButton.update(dt);
        rightButton.update(dt);
    }
}
