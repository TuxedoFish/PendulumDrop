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

public class ShopPage extends UIContainer implements UIPage {

    public interface ShopPageInteractions {
        public void goBack();
    }

    public ShopPage(Vector2 dimensions, TextureHolder textures,
                    FreeTypeFontGenerator generator, ShopPageInteractions interactions) {
        components = new ArrayList<>();

        FreeTypeFontGenerator.FreeTypeFontParameter menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = (int)(108.0f*(Gdx.graphics.getHeight()/2000.0f));;
        BitmapFont whiteTextFont = generator.generateFont(menuParameter);

        Vector2 BOTTOM_RIGHT = UIController.getScreenPercentage(
                0.55f,
                0.1f,
                dimensions);

        // Create styles
        ButtonStyle backButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.4f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );

        // Create button for navigating back
        Button backButton = new Button(
                "Exit",
                BOTTOM_RIGHT,
                backButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Go back");
                        interactions.goBack();
                    }
                }
        );

        // Add Elements
        components.add(backButton);
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
