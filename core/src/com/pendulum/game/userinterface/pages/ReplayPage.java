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
import com.pendulum.game.userinterface.components.Text;

import java.util.ArrayList;

public class ReplayPage extends UIContainer implements UIPage {

    public interface ReplayPageInteractions {
        public void openShop();
        public void playAgain();
    }

    public ReplayPage(Vector2 dimensions, TextureHolder textures, FreeTypeFontGenerator generator,
          ReplayPageInteractions interactions) {
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
        ButtonStyle shopButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.2f, 0.0f, dimensions).x,
                textures.getTexture("button_shop.png"),
                whiteTextFont
        );
        ButtonStyle twitterButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.2f, 0.0f, dimensions).x,
                textures.getTexture("button_twitter.png"),
                whiteTextFont
        );
        ButtonStyle instagramButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.2f, 0.0f, dimensions).x,
                textures.getTexture("button_instagram.png"),
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
                "",
                UIController.getScreenPercentage(0.15f, 0.4f, dimensions),
                shopButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Open the shop");
                        interactions.openShop();
                    }
                }
        );
        Button twitterButton = new Button(
                "",
                UIController.getScreenPercentage(0.4f, 0.4f, dimensions),
                twitterButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        Gdx.net.openURI("https://twitter.com/DevTuxedofish");
                        System.out.println("Navigate to twitter");
                    }
                }
        );
        Button facebookButton = new Button(
                "",
                UIController.getScreenPercentage(0.65f, 0.4f, dimensions),
                instagramButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        Gdx.net.openURI("https://www.instagram.com/harrysrepairs/");
                        System.out.println("Navigate to instagram");
                    }
                }
        );
        Button playButton = new Button(
                "Play",
                UIController.getScreenPercentage(0.2f, 0.56f, dimensions),
                largeButton,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Play again");
                        interactions.playAgain();
                    }
                }
        );

        Text scoreText = new Text(
                "HIGHSCORE:",
                whiteTextFont,
                UIController.getScreenPercentage(0.5f, 0.65f, dimensions)
        );

        // Add Elements
        components.add(optionsBox);
        components.add(playButton);
        components.add(shopButton);
        components.add(twitterButton);
        components.add(facebookButton);
        components.add(scoreText);
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
