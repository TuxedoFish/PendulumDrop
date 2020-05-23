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
import com.pendulum.game.userinterface.components.ToggleButton;
import com.pendulum.game.utils.Preferences;

import java.util.ArrayList;

public class RevivePage extends UIContainer implements UIPage {

    boolean isSoundOn = true;

    public interface RevivePageInteractions {
        public void cancel();
        public void confirm();
    }

    public RevivePage(Vector2 dimensions, TextureHolder textures, FreeTypeFontGenerator generator,
                      RevivePageInteractions interactions) {
        components = new ArrayList<>();

        FreeTypeFontGenerator.FreeTypeFontParameter menuParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuParameter.size = (int)(108.0f*(Gdx.graphics.getHeight()/2000.0f));;
        BitmapFont whiteTextFont = generator.generateFont(menuParameter);

        // Create styles
        ButtonStyle cancelButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.35f, 0.0f, dimensions).x,
                textures.getTexture("button_cancel.png"),
                whiteTextFont
        );
        ButtonStyle confirmButtonStyle = new ButtonStyle(
                UIController.getScreenPercentage(0.35f, 0.0f, dimensions).x,
                textures.getTexture("button_confirm.png"),
                whiteTextFont
        );

        // Create the overlay for the menu
        Box menuBox = new Box(
                UIController.getScreenPercentage(1.0f, 1.0f, dimensions),
                UIController.getScreenPercentage(0f, 1.0f, dimensions),
                textures.getTexture("transparent_menu.png")
        );

        // Create buttons for different settings
        Button confirmButton = new Button(
                "",
                UIController.getScreenPercentage(0.1f, 0.5f, dimensions),
                confirmButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Watch video to go back to game");
                        interactions.confirm();
                    }
                }
        );
        Button cancelButton = new Button(
                "",
                UIController.getScreenPercentage(0.55f, 0.5f, dimensions),
                cancelButtonStyle,
                new OnClickButtonInterface() {
                    @Override
                    public void onClick() {
                        System.out.println("Go to replay game page");
                        interactions.cancel();
                    }
                }
        );

        Text cancelText = new Text(
                "Cancel",
                whiteTextFont,
                UIController.getScreenPercentage(0.725f, 0.3f, dimensions)
        );

        Text confirmText = new Text(
                "Watch Ad",
                whiteTextFont,
                UIController.getScreenPercentage(0.275f, 0.3f, dimensions)
        );

        Text optionTextOne = new Text(
                "Would you like",
                whiteTextFont,
                UIController.getScreenPercentage(0.5f, 0.8f, dimensions)
        );

        Text optionTextTwo = new Text(
                "to carry on?",
                whiteTextFont,
                UIController.getScreenPercentage(0.5f, 0.7f, dimensions)
        );

        // Add Elements
        components.add(menuBox);
        components.add(cancelButton);
        components.add(confirmButton);
        components.add(optionTextOne);
        components.add(optionTextTwo);
        components.add(cancelText);
        components.add(confirmText);
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
