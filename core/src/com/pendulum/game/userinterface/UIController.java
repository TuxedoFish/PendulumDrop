package com.pendulum.game.userinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.pendulum.game.texture.TextureHolder;
import com.pendulum.game.userinterface.pages.CreditsPage;
import com.pendulum.game.userinterface.pages.HelpPage;
import com.pendulum.game.userinterface.pages.MainOverlayPage;
import com.pendulum.game.userinterface.pages.MenuPage;
import com.pendulum.game.userinterface.pages.ReplayPage;
import com.pendulum.game.userinterface.pages.RevivePage;
import com.pendulum.game.userinterface.pages.SettingsPage;
import com.pendulum.game.userinterface.pages.ShopPage;
import com.pendulum.game.userinterface.pages.UIContainer;
import com.pendulum.game.utils.Preferences;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class UIController {

    public interface UIControllerInteractions {
        public void setIsSoundOn(boolean isSoundOn);
        public boolean navigateToShop();
        public boolean navigateToReplay();
        public boolean playAgain();
        public void confirmDeath();
        public void watchVideo();
    }

    // Settings passed in from the controller
    private Vector2 dimensions;
    private TextureHolder textures;
    private FreeTypeFontGenerator font;

    // Different pages as a list of their components
    private ArrayList<UIContainer> pages = new ArrayList<>();
    private MainOverlayPage mainOverlayPage;
    private SettingsPage settingsPage;
    private CreditsPage creditsPage;
    private MenuPage menuPage;
    private ReplayPage replayPage;
    private HelpPage helpPage;
    private ShopPage shopPage;
    private RevivePage revivePage;

    public UIController(Vector2 dimensions, TextureHolder textures, Preferences preferences,
                        UIControllerInteractions interactions) {
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
                helpPage.show();
            }

            @Override
            public void openCredits() {
                menuPage.hide();
                creditsPage.show();
            }
        });

        // Settings Page
        settingsPage = new SettingsPage(dimensions, textures, generator, preferences,
            new SettingsPage.SettingsPageInteractions() {
                @Override
                public void goBack() {
                    settingsPage.hide();
                    menuPage.show();
                }

                @Override
                public void setIsSoundOn(boolean isSoundOn) {
                    interactions.setIsSoundOn(isSoundOn);
                }
            });

        // Settings Page
        creditsPage = new CreditsPage(dimensions,textures,generator, new CreditsPage.CreditsPageInteractions() {
            @Override
            public void goBack() {
                creditsPage.hide();
                menuPage.show();
            }
        });

        // Help Page
        helpPage = new HelpPage(dimensions,textures,generator, new HelpPage.HelpPageInteractions() {
            @Override
            public void goBack() {
                helpPage.hide();
                menuPage.show();
            }
        });

        replayPage = new ReplayPage(dimensions, textures, font, preferences, new ReplayPage.ReplayPageInteractions() {
            @Override
            public void openShop() {
                if(interactions.navigateToShop()) {
                    replayPage.hide();
                    shopPage.show();
                }
            }

            @Override
            public void playAgain() {
                if(interactions.playAgain()) {
                    replayPage.hide();
                }
            }
        });

        shopPage = new ShopPage(dimensions, textures, font, new ShopPage.ShopPageInteractions() {
            @Override
            public void goBack() {
                if(interactions.navigateToReplay()) {
                    shopPage.hide();
                    replayPage.show();
                }
            }
        });

        revivePage = new RevivePage(dimensions, textures, font, new RevivePage.RevivePageInteractions() {
            @Override
            public void cancel() {
                revivePage.hide();
                interactions.confirmDeath();
            }

            @Override
            public void confirm() {
                revivePage.hide();
                interactions.watchVideo();
            }
        });

        // Add pages
        pages.add(replayPage);
        pages.add(mainOverlayPage);
        pages.add(settingsPage);
        pages.add(creditsPage);
        pages.add(menuPage);
        pages.add(helpPage);
        pages.add(shopPage);
        pages.add(revivePage);
    }

    public void showMainScreen() {
        // Show initial menu screen
        mainOverlayPage.show();
    }

    public void showReviveOpportunity() {
        // Show the revive opportunity
        revivePage.show();
    }

    public void showReplayScreen() {
        // Show the replay screen
        replayPage.show();
    }

    public static Vector2 getScreenPercentage(float xPercentage, float yPercentage, Vector2 dimensions) {
        return new Vector2(dimensions.x * xPercentage, dimensions.y * yPercentage);
    }

    public void render(SpriteBatch sb) {
        for (UIContainer page : pages) {
           page.render(sb);
       };
    }

    public void update(float dt) {
        for (UIContainer page : pages) {
            page.update(dt);
        };
    }

    public boolean onTouchDown(Vector2 position) {
        AtomicBoolean isUIEvent = new AtomicBoolean(false);

        for (UIContainer page : pages) {
            boolean isNewUIEvent = page.touchDown(position);
            isUIEvent.set(isUIEvent.get() || isNewUIEvent);
        };

        return isUIEvent.get();
    }

    public boolean onTouchUp(Vector2 position) {
        AtomicBoolean isUIEvent = new AtomicBoolean(false);

        for (UIContainer page : pages) {
            boolean isNewUIEvent = page.touchUp(position);
            isUIEvent.set(isUIEvent.get() || isNewUIEvent);
        };

        return isUIEvent.get();
    }
}
