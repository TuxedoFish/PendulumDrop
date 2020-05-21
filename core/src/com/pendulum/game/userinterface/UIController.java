package com.pendulum.game.userinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.texture.TextureHolder;
import com.pendulum.game.userinterface.pages.MainOverlayPage;
import com.pendulum.game.userinterface.pages.MenuPage;
import com.pendulum.game.userinterface.pages.ReplayPage;
import com.pendulum.game.userinterface.pages.SettingsPage;

public class UIController {

    // Settings passed in from the controller
    private Vector2 dimensions;
    private TextureHolder textures;
    private FreeTypeFontGenerator font;

    // Different pages as a list of their components
    private MainOverlayPage mainOverlayPage;
    private SettingsPage settingsPage;
    private MenuPage menuPage;
    private ReplayPage replayPage;

    // State variables

    public UIController(Vector2 dimensions, TextureHolder textures) {
        // Passed in from controller
        this.textures = textures;
        this.dimensions = dimensions;

        // Generate the fonts
        // Cute font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(("fonts/Choco-Bear.ttf")));
        generator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);

        this.font = generator;
        // Create pages

        // Main Page
        mainOverlayPage = new MainOverlayPage(dimensions, textures, font, new MainOverlayPage.MainOverlayPageInteractions() {
            @Override
            public void openMenu() {
                mainOverlayPage.hide();
                menuPage.show();
            }
        });

        // Menu Page
        menuPage = new MenuPage(dimensions, textures, font, new MenuPage.MenuPageInteractions() {
            @Override
            public void closeMenu() {
                menuPage.hide();
                mainOverlayPage.show();
            }

            @Override
            public void openSettings() {
                menuPage.hide();
                settingsPage.show();
            }

            @Override
            public void openHelp() {
                menuPage.hide();
                mainOverlayPage.show();
            }

            @Override
            public void openCredits() {
                menuPage.hide();
                mainOverlayPage.show();
            }
        });

        // Settings Page
        settingsPage = new SettingsPage(dimensions,textures,generator, new SettingsPage.SettingsPageInteractions() {
            @Override
            public void goBack() {
                settingsPage.hide();
                menuPage.show();
            }
        });

        replayPage = new ReplayPage(dimensions, textures, font);

        // Show initial menu screen
        mainOverlayPage.show();
        // replayPage.show();
    }

    public static Vector2 getScreenPercentage(float xPercentage, float yPercentage, Vector2 dimensions) {
        return new Vector2(dimensions.x * xPercentage, dimensions.y * yPercentage);
    }

    public void render(SpriteBatch sb) {
       mainOverlayPage.render(sb);
       menuPage.render(sb);
       replayPage.render(sb);
       settingsPage.render(sb);
    }

    public void update(float dt) {
        mainOverlayPage.update(dt);
        menuPage.update(dt);
        replayPage.update(dt);
        settingsPage.update(dt);
    }

    public boolean onTouchDown(Vector2 position) {
        boolean isUIEvent = false;

        isUIEvent = isUIEvent || mainOverlayPage.touchDown(position);
        isUIEvent = isUIEvent || menuPage.touchDown(position);
        isUIEvent = isUIEvent || replayPage.touchDown(position);
        isUIEvent = isUIEvent || settingsPage.touchDown(position);

        return isUIEvent;
    }

    public boolean onTouchUp(Vector2 position) {
        boolean isUIEvent = false;

        isUIEvent = isUIEvent || mainOverlayPage.touchUp(position);
        isUIEvent = isUIEvent || menuPage.touchUp(position);
        isUIEvent = isUIEvent || replayPage.touchUp(position);
        isUIEvent = isUIEvent || settingsPage.touchUp(position);

        return isUIEvent;
    }
}
