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

        Vector2 TOP_LEFT = UIController.getScreenPercentage(
                0.05f,
                0.98f,
                dimensions);

        // Create styles
        ButtonStyle largeButtonStyle  = new ButtonStyle(
                UIController.getScreenPercentage(0.6f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );
        ButtonStyle backButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.4f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );

        float button_vertical = TOP_COMPONENT.y - ((BUTTONS_Y_PADDING+largeButtonStyle.getHeight())*3.0f);
        Vector2 TWITTER_BUTTON_LOCATION = UIController.getScreenPercentage(
                0.2f,
                0.0f,
                dimensions);
        TWITTER_BUTTON_LOCATION.y = button_vertical;

        Vector2 INSTAGRAM_BUTTON_LOCATION = UIController.getScreenPercentage(
                0.55f,
                0.0f,
                dimensions);
        INSTAGRAM_BUTTON_LOCATION.y = button_vertical;

        ButtonStyle twitterButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.25f, 0.0f, dimensions).x,
                textures.getTexture("button_twitter.png"),
                whiteTextFont
        );
        ButtonStyle instagramButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.25f, 0.0f, dimensions).x,
                textures.getTexture("button_instagram.png"),
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
                largeButtonStyle,
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
                TOP_COMPONENT.add(0.0f, -BUTTONS_Y_PADDING-largeButtonStyle.getHeight()),
                largeButtonStyle,
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
                TOP_COMPONENT.add(0.0f, -BUTTONS_Y_PADDING-largeButtonStyle.getHeight()),
                largeButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the credits");
                        interactions.openCredits();
                    }
                }
        );
        // Create button for navigating back
        Button backButton = new Button(
                "Back",
                TOP_LEFT,
                backButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Go back");
                        interactions.closeMenu();
                    }
                }
        );
        Button twitterButton = new Button(
                "",
                TWITTER_BUTTON_LOCATION,
                twitterButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        Gdx.net.openURI("https://twitter.com/DevTuxedofish");
                        System.out.println("Open Twitter");
                    }
                }
        );
        Button instagramButton = new Button(
                "",
                INSTAGRAM_BUTTON_LOCATION,
                instagramButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        Gdx.net.openURI("https://www.instagram.com/harrysrepairs/");
                        System.out.println("Open Instagram");
                    }
                }
        );

        // Add Elements
        components.add(menuBox);
        components.add(settingsButton);
        components.add(helpButton);
        components.add(creditsButton);
        components.add(backButton);
        components.add(twitterButton);
        components.add(instagramButton);
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
