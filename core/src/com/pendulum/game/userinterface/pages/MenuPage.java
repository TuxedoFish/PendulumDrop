package com.pendulum.game.userinterface.pages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.texture.TextureHolder;
import com.pendulum.game.userinterface.components.Box;
import com.pendulum.game.userinterface.components.Button;
import com.pendulum.game.userinterface.components.ButtonStyle;
import com.pendulum.game.userinterface.UIController;
import com.pendulum.game.userinterface.components.OnClickButtonInterface;

import java.util.ArrayList;

public class MenuPage extends UIContainer implements UIPage {

    public interface MenuPageInteractions {
        public void closeMenu();
        public void openSettings();
        public void openHelp();
        public void openCredits();
    }

    public MenuPage(Vector2 dimensions, TextureHolder textures,
                    FreeTypeFontGenerator generator, MenuPageInteractions interactions) {
        components = new ArrayList<>();

        FreeTypeFontGenerator.FreeTypeFontParameter menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = (int)(108.0f*(Gdx.graphics.getHeight()/2000.0f));;
        BitmapFont whiteTextFont = generator.generateFont(menuParameter);

        float BUTTONS_Y_PADDING = 100.0f;
        Vector2 TOP_COMPONENT = UIController.getScreenPercentage(
                0.2f,
                0.85f,
                dimensions);

        // Create styles
        ButtonStyle largeButton = new ButtonStyle(
                UIController.getScreenPercentage(0.6f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );

        // Create the overlay for the menu
        Box menuBox = new Box(
                UIController.getScreenPercentage(1.0f, 1.0f, dimensions),
                UIController.getScreenPercentage(0f, 1.0f, dimensions),
                textures.getTexture("transparent_menu.png")
        );

        // Create buttons for different settings
        Button settingsButton = new Button(
                "Settings",
                TOP_COMPONENT,
                largeButton,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the settings");
                        interactions.openSettings();
                    }
                }
        );
        // Create buttons for different settings
        Button helpButton = new Button(
                "Help",
                TOP_COMPONENT.add(0.0f, -BUTTONS_Y_PADDING-largeButton.getHeight()),
                largeButton,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the help");
                        interactions.openHelp();
                    }
                }
        );
        // Create buttons for different settings
        Button creditsButton = new Button(
                "Credits",
                TOP_COMPONENT.add(0.0f, -BUTTONS_Y_PADDING-largeButton.getHeight()),
                largeButton,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the credits");
                        interactions.openCredits();
                    }
                }
        );

        // Add Elements
        components.add(menuBox);
        components.add(settingsButton);
        components.add(helpButton);
        components.add(creditsButton);
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
