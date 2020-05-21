package com.pendulum.game.userinterface.pages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.texture.TextureHolder;
import com.pendulum.game.userinterface.components.Button;
import com.pendulum.game.userinterface.components.ButtonStyle;
import com.pendulum.game.userinterface.components.OnClickButtonInterface;
import com.pendulum.game.userinterface.UIController;
import com.pendulum.game.userinterface.components.Text;

import java.util.ArrayList;

public class MainOverlayPage extends UIContainer implements UIPage {

    public interface MainOverlayPageInteractions {
        public void openMenu();
    }

    public MainOverlayPage(Vector2 dimensions, TextureHolder textures,
                           FreeTypeFontGenerator generator, MainOverlayPageInteractions interactions) {
        components = new ArrayList<>();

        FreeTypeFontGenerator.FreeTypeFontParameter menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = (int)(64.0f*(Gdx.graphics.getHeight()/2000.0f));;
        BitmapFont whiteTextFont = generator.generateFont(menuParameter);

        // Create styles
        ButtonStyle smallButton = new ButtonStyle(
                UIController.getScreenPercentage(0.32f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );

        // Create menu button
        Button menuButton = new Button(
                "Menu",
                UIController.getScreenPercentage(0.05f, 0.95f, dimensions),
                smallButton,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the menu");
                        interactions.openMenu();
                    }
                }
        );

        // Add Elements
        components.add(menuButton);
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
