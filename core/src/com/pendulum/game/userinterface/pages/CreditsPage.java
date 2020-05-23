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

public class CreditsPage extends UIContainer implements UIPage {

    public interface CreditsPageInteractions {
        public void goBack();
    }

    public CreditsPage(Vector2 dimensions, TextureHolder textures,
                       FreeTypeFontGenerator generator, CreditsPageInteractions interactions) {
        components = new ArrayList<>();

        FreeTypeFontGenerator.FreeTypeFontParameter menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = (int)(108.0f*(Gdx.graphics.getHeight()/2000.0f));;
        BitmapFont whiteTextFont = generator.generateFont(menuParameter);

        Vector2 TOP_LEFT = UIController.getScreenPercentage(
                0.05f,
                0.98f,
                dimensions);

        Vector2 DEVELOPER_DESCRIPTION_POSITION = UIController.getScreenPercentage(
                0.5f,
                0.85f,
                dimensions);

        Vector2 DEVELOPER_NAME_POSITION = UIController.getScreenPercentage(
                0.5f,
                0.75f,
                dimensions);

        // Create styles
        ButtonStyle backButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.4f, 0.0f, dimensions).x,
                textures.getTexture("button_long.png"),
                whiteTextFont
        );

        // Create the overlay for the menu
        Box menuBox = new Box(
                UIController.getScreenPercentage(1.0f, 1.0f, dimensions),
                UIController.getScreenPercentage(0f, 1.0f, dimensions),
                textures.getTexture("transparent_menu.png")
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
                        interactions.goBack();
                    }
                }
        );

        Text developHint = new Text(
                "Developed by:",
                whiteTextFont,
                DEVELOPER_DESCRIPTION_POSITION
        );

        Text developName = new Text(
                "Harry Liversedge",
                whiteTextFont,
                DEVELOPER_NAME_POSITION
        );

        // Add Elements
        components.add(menuBox);
        components.add(backButton);
        components.add(developHint);
        components.add(developName);
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
