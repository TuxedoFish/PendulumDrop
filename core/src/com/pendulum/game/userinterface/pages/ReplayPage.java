package com.pendulum.game.userinterface.pages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.texture.TextureHolder;
import com.pendulum.game.userinterface.UIController;
import com.pendulum.game.userinterface.components.Box;
import com.pendulum.game.userinterface.components.Button;
import com.pendulum.game.userinterface.components.ButtonStyle;
import com.pendulum.game.userinterface.components.OnClickButtonInterface;

import java.util.ArrayList;

public class ReplayPage extends UIContainer implements UIPage {

    public ReplayPage(Vector2 dimensions, TextureHolder textures, FreeTypeFontGenerator generator) {
        components = new ArrayList<>();

        FreeTypeFontGenerator.FreeTypeFontParameter menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = (int)(108.0f*(Gdx.graphics.getHeight()/2000.0f));;
        BitmapFont whiteTextFont = generator.generateFont(menuParameter);

        // Create styles
        ButtonStyle largeButton = new ButtonStyle(
                UIController.getScreenPercentage(0.6f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );

        // Create the overlay for the menu
        Box optionsBox = new Box(
                UIController.getScreenPercentage(1.0f, 0.4f, dimensions),
                UIController.getScreenPercentage(0f, 0.7f, dimensions),
                textures.getTexture("transparent_menu.png")
        );

        // Create buttons for different settings
        Button shopButton = new Button(
                "Shop",
                UIController.getScreenPercentage(0.2f, 0.5f, dimensions),
                largeButton,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the shop");
                    }
                }
        );

        // Add Elements
        components.add(optionsBox);
        components.add(shopButton);
    }

    @Override
    public void show() {
        shouldRender = true;
    }

    @Override
    public void hide() {
        shouldRender = false;
    }
}
