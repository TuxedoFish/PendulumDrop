package com.pendulum.game.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by Harry on 24/01/2017.
 */

public class TextureHolder {
    private ArrayList<Texture> textures;
    private ArrayList<String> texturenames;

    private AssetManager assetManager;

    private Sound coin_noise;
    private Sound hit_noise;
    private Sound tap_noise;

    public TextureHolder() {
        assetManager = new AssetManager();
        textures = new ArrayList<Texture>();
        texturenames = new ArrayList<String>();
    }
    public Sound getCoin_noise() {
        return coin_noise;
    }
    public Sound getHit_noise() {
        return hit_noise;
    }
    public Sound getTap_noise() {
        return tap_noise;
    }
    public boolean isFinishedLoading() {
        assetManager.update();
        if(assetManager.isLoaded("images/borders/closedcircle16.png")) {
            //SOUNDS
            coin_noise = assetManager.get("sounds/coin.wav", Sound.class);
            hit_noise = assetManager.get("sounds/hit.wav", Sound.class);
            tap_noise = assetManager.get("sounds/tap.wav", Sound.class);
            //SKINS
            textures.add(assetManager.get("images/skins/rope_green.png", Texture.class));
            texturenames.add("rope_green.png");
            textures.add(assetManager.get("images/skins/rope_brown.png", Texture.class));
            texturenames.add("rope_brown.png");
            textures.add(assetManager.get("images/skins/ball_fruit_watermelon.png", Texture.class));
            texturenames.add("ball_fruit_watermelon.png");
            textures.add(assetManager.get("images/skins/ball_fruit_orange.png", Texture.class));
            texturenames.add("ball_fruit_orange.png");
            textures.add(assetManager.get("images/skins/ball_fruit_kiwi.png", Texture.class));
            texturenames.add("ball_fruit_kiwi.png");
            textures.add(assetManager.get("images/skins/ball_yin_yang.png", Texture.class));
            texturenames.add("ball_yin_yang.png");

            // CUSTOM SKINS
            textures.add(assetManager.get("images/skins/ball_smile.png", Texture.class));
            texturenames.add("ball_orange.png");
            textures.add(assetManager.get("images/skins/ball_caitlin.png", Texture.class));
            texturenames.add("ball_caitlin.png");
            textures.add(assetManager.get("images/skins/ball_fred.png", Texture.class));
            texturenames.add("ball_fred.png");

            textures.add(assetManager.get("images/skins/ball_purple.png", Texture.class));
            texturenames.add("ball_purple.png");
            textures.add(assetManager.get("images/skins/ball_yellow.png", Texture.class));
            texturenames.add("ball_yellow.png");
            textures.add(assetManager.get("images/skins/ball_tyre.png", Texture.class));
            texturenames.add("ball_tyre.png");
            textures.add(assetManager.get("images/skins/ball_aztec_1.png", Texture.class));
            texturenames.add("ball_aztec_1.png");
            textures.add(assetManager.get("images/skins/rope_chain.png", Texture.class));
            texturenames.add("rope_chain.png");
            textures.add(assetManager.get("images/skins/rod_chain.png", Texture.class));
            texturenames.add("rod_chain.png");
            textures.add(assetManager.get("images/skins/holder.png", Texture.class));
            texturenames.add("holder.png");
            textures.add(assetManager.get("images/skins/rope_purple.png", Texture.class));
            texturenames.add("rope_purple.png");
            textures.add(assetManager.get("images/skins/rope_yellow.png", Texture.class));
            texturenames.add("rope_yellow.png");
            textures.add(assetManager.get("images/skins/rope_aztec_1.png", Texture.class));
            texturenames.add("rope_aztec_1.png");
            textures.add(assetManager.get("images/skins/rod_aztec_1.png", Texture.class));
            texturenames.add("rod_aztec_1.png");
            textures.add(assetManager.get("images/skins/ball_wooden_wheel.png", Texture.class));
            texturenames.add("ball_wooden_wheel.png");
            textures.add(assetManager.get("images/skins/ball_steering_wheel.png", Texture.class));
            texturenames.add("ball_steering_wheel.png");
            textures.add(assetManager.get("images/skins/rope_wooden.png", Texture.class));
            texturenames.add("rope_wooden.png");
            //CHASER
            textures.add(assetManager.get("images/chaser/chaser1.png", Texture.class));
            texturenames.add("chaser1.png");
            textures.add(assetManager.get("images/chaser/chaser2.png", Texture.class));
            texturenames.add("chaser2.png");
            textures.add(assetManager.get("images/chaser/chaser3.png", Texture.class));
            texturenames.add("chaser3.png");
            textures.add(assetManager.get("images/chaser/chaser4.png", Texture.class));
            texturenames.add("chaser4.png");
            textures.add(assetManager.get("images/chaser/chaser5.png", Texture.class));
            texturenames.add("chaser5.png");
            textures.add(assetManager.get("images/chaser/chaser6.png", Texture.class));
            texturenames.add("chaser6.png");
            textures.add(assetManager.get("images/chaser/chaser7.png", Texture.class));
            texturenames.add("chaser7.png");
            textures.add(assetManager.get("images/chaser/chaser8.png", Texture.class));
            texturenames.add("chaser8.png");
            //GAME TEXTURES
            textures.add(assetManager.get("images/sound_off.png", Texture.class));
            texturenames.add("sound_off.png");
            textures.add(assetManager.get("images/sound_on.png", Texture.class));
            texturenames.add("sound_on.png");
            textures.add(assetManager.get("images/arrow_invert.png", Texture.class));
            texturenames.add("arrow_invert.png");
            textures.add(assetManager.get("images/slider.png", Texture.class));
            texturenames.add("slider.png");
            textures.add(assetManager.get("images/play.png", Texture.class));
            texturenames.add("play.png");
            textures.add(assetManager.get("images/title.png", Texture.class));
            texturenames.add("title.png");
            textures.add(assetManager.get("images/replay.png", Texture.class));
            texturenames.add("replay.png");
            textures.add(assetManager.get("images/arrow.png", Texture.class));
            texturenames.add("arrow.png");
            textures.add(assetManager.get("images/shop.png", Texture.class));
            texturenames.add("shop.png");
            textures.add(assetManager.get("images/lock.png", Texture.class));
            texturenames.add("lock.png");
            textures.add(assetManager.get("images/platform.png", Texture.class));
            texturenames.add("platform.png");
            textures.add(assetManager.get("images/score.png", Texture.class));
            texturenames.add("score.png");
            textures.add(assetManager.get("images/tap_up.png", Texture.class));
            texturenames.add("tap_up.png");
            textures.add(assetManager.get("images/tap_down.png", Texture.class));
            texturenames.add("tap_down.png");
            textures.add(assetManager.get("images/platformend.png", Texture.class));
            texturenames.add("platformend.png");
            textures.add(assetManager.get("images/background.png", Texture.class));
            texturenames.add("background.png");

            /**
             * UI JAZZ
             */
            textures.add(assetManager.get("images/button.png", Texture.class));
            texturenames.add("button.png");
            textures.add(assetManager.get("images/button_long.png", Texture.class));
            texturenames.add("button_long.png");
            textures.add(assetManager.get("images/button_twitter.png", Texture.class));
            texturenames.add("button_twitter.png");
            textures.add(assetManager.get("images/button_instagram.png", Texture.class));
            texturenames.add("button_instagram.png");
            textures.add(assetManager.get("images/button_sound_on.png", Texture.class));
            texturenames.add("button_sound_on.png");
            textures.add(assetManager.get("images/button_sound_off.png", Texture.class));
            texturenames.add("button_sound_off.png");
            textures.add(assetManager.get("images/button_left_arrow.png", Texture.class));
            texturenames.add("button_left_arrow.png");
            textures.add(assetManager.get("images/button_right_arrow.png", Texture.class));
            texturenames.add("button_right_arrow.png");
            textures.add(assetManager.get("images/button_shop.png", Texture.class));
            texturenames.add("button_shop.png");
            textures.add(assetManager.get("images/button_confirm.png", Texture.class));
            texturenames.add("button_confirm.png");
            textures.add(assetManager.get("images/button_cancel.png", Texture.class));
            texturenames.add("button_cancel.png");
            textures.add(assetManager.get("images/help_image_1.png", Texture.class));
            texturenames.add("help_image_1.png");
            textures.add(assetManager.get("images/help_image_2.png", Texture.class));
            texturenames.add("help_image_2.png");
            textures.add(assetManager.get("images/help_image_3.png", Texture.class));
            texturenames.add("help_image_3.png");
            textures.add(assetManager.get("images/transparent_menu.png", Texture.class));
            texturenames.add("transparent_menu.png");

            textures.add(assetManager.get("images/post.png", Texture.class));
            texturenames.add("post.png");
            textures.add(assetManager.get("images/gameover.png", Texture.class));
            texturenames.add("gameover.png");
            textures.add(assetManager.get("images/logo.png", Texture.class));
            texturenames.add("logo.png");
            //BORDERS
            textures.add(assetManager.get("images/borders/blocked.png", Texture.class)); texturenames.add("blocked.png");
            textures.add(assetManager.get("images/borders/blocked1.5.png", Texture.class)); texturenames.add("blocked1.5.png");
            textures.add(assetManager.get("images/borders/blocked2.png", Texture.class)); texturenames.add("blocked2.png");
            textures.add(assetManager.get("images/borders/blocked2.5.png", Texture.class)); texturenames.add("blocked2.5.png");
            textures.add(assetManager.get("images/borders/blocked3.png", Texture.class)); texturenames.add("blocked3.png");
            textures.add(assetManager.get("images/borders/blocked3.5.png", Texture.class)); texturenames.add("blocked3.5.png");
            textures.add(assetManager.get("images/borders/blocked4.png", Texture.class)); texturenames.add("blocked4.png");
            textures.add(assetManager.get("images/borders/blocked4.5.png", Texture.class)); texturenames.add("blocked4.5.png");
            textures.add(assetManager.get("images/borders/blocked4.51.png", Texture.class)); texturenames.add("blocked4.51.png");
            textures.add(assetManager.get("images/borders/blocked4.52.png", Texture.class)); texturenames.add("blocked4.52.png");
            textures.add(assetManager.get("images/borders/blocked5.png", Texture.class)); texturenames.add("blocked5.png");
            textures.add(assetManager.get("images/borders/blocked5.5.png", Texture.class)); texturenames.add("blocked5.5.png");
            textures.add(assetManager.get("images/borders/blocked6.png", Texture.class)); texturenames.add("blocked6.png");
            textures.add(assetManager.get("images/borders/blocked6.5.png", Texture.class)); texturenames.add("blocked6.5.png");
            textures.add(assetManager.get("images/borders/blocked7.png", Texture.class)); texturenames.add("blocked7.png");
            textures.add(assetManager.get("images/borders/blocked7.5.png", Texture.class)); texturenames.add("blocked7.5.png");
            textures.add(assetManager.get("images/borders/open.png", Texture.class)); texturenames.add("open.png");
            textures.add(assetManager.get("images/borders/open1.5.png", Texture.class)); texturenames.add("open1.5.png");
            textures.add(assetManager.get("images/borders/open2.png", Texture.class)); texturenames.add("open2.png");
            textures.add(assetManager.get("images/borders/open2.5.png", Texture.class)); texturenames.add("open2.5.png");
            textures.add(assetManager.get("images/borders/open3.png", Texture.class)); texturenames.add("open3.png");
            textures.add(assetManager.get("images/borders/open3.5.png", Texture.class)); texturenames.add("open3.5.png");
            textures.add(assetManager.get("images/borders/open4.png", Texture.class)); texturenames.add("open4.png");
            textures.add(assetManager.get("images/borders/open4.5.png", Texture.class)); texturenames.add("open4.5.png");
            textures.add(assetManager.get("images/borders/open4.51.png", Texture.class)); texturenames.add("open4.51.png");
            textures.add(assetManager.get("images/borders/open4.52.png", Texture.class)); texturenames.add("open4.52.png");
            textures.add(assetManager.get("images/borders/open5.png", Texture.class)); texturenames.add("open5.png");
            textures.add(assetManager.get("images/borders/open5.5.png", Texture.class)); texturenames.add("open5.5.png");
            textures.add(assetManager.get("images/borders/open6.png", Texture.class)); texturenames.add("open6.png");
            textures.add(assetManager.get("images/borders/open6.5.png", Texture.class)); texturenames.add("open6.5.png");
            textures.add(assetManager.get("images/borders/open7.png", Texture.class)); texturenames.add("open7.png");
            textures.add(assetManager.get("images/borders/open7.5.png", Texture.class)); texturenames.add("open7.5.png");
            textures.add(assetManager.get("images/borders/opencircle1.png", Texture.class)); texturenames.add("opencircle1.png");
            textures.add(assetManager.get("images/borders/opencircle2.png", Texture.class)); texturenames.add("opencircle2.png");
            textures.add(assetManager.get("images/borders/opencircle3.png", Texture.class)); texturenames.add("opencircle3.png");
            textures.add(assetManager.get("images/borders/opencircle4.png", Texture.class)); texturenames.add("opencircle4.png");
            textures.add(assetManager.get("images/borders/opencircle5.png", Texture.class)); texturenames.add("opencircle5.png");
            textures.add(assetManager.get("images/borders/opencircle6.png", Texture.class)); texturenames.add("opencircle6.png");
            textures.add(assetManager.get("images/borders/opencircle7.png", Texture.class)); texturenames.add("opencircle7.png");
            textures.add(assetManager.get("images/borders/opencircle8.png", Texture.class)); texturenames.add("opencircle8.png");
            textures.add(assetManager.get("images/borders/opencircle9.png", Texture.class)); texturenames.add("opencircle9.png");
            textures.add(assetManager.get("images/borders/opencircle10.png", Texture.class)); texturenames.add("opencircle10.png");
            textures.add(assetManager.get("images/borders/opencircle11.png", Texture.class)); texturenames.add("opencircle11.png");
            textures.add(assetManager.get("images/borders/opencircle12.png", Texture.class)); texturenames.add("opencircle12.png");
            textures.add(assetManager.get("images/borders/opencircle13.png", Texture.class)); texturenames.add("opencircle13.png");
            textures.add(assetManager.get("images/borders/opencircle14.png", Texture.class)); texturenames.add("opencircle14.png");
            textures.add(assetManager.get("images/borders/opencircle15.png", Texture.class)); texturenames.add("opencircle15.png");
            textures.add(assetManager.get("images/borders/opencircle16.png", Texture.class)); texturenames.add("opencircle16.png");
            textures.add(assetManager.get("images/borders/closedcircle1.png", Texture.class)); texturenames.add("closedcircle1.png");
            textures.add(assetManager.get("images/borders/closedcircle2.png", Texture.class)); texturenames.add("closedcircle2.png");
            textures.add(assetManager.get("images/borders/closedcircle3.png", Texture.class)); texturenames.add("closedcircle3.png");
            textures.add(assetManager.get("images/borders/closedcircle4.png", Texture.class)); texturenames.add("closedcircle4.png");
            textures.add(assetManager.get("images/borders/closedcircle5.png", Texture.class)); texturenames.add("closedcircle5.png");
            textures.add(assetManager.get("images/borders/closedcircle6.png", Texture.class)); texturenames.add("closedcircle6.png");
            textures.add(assetManager.get("images/borders/closedcircle7.png", Texture.class)); texturenames.add("closedcircle7.png");
            textures.add(assetManager.get("images/borders/closedcircle8.png", Texture.class)); texturenames.add("closedcircle8.png");
            textures.add(assetManager.get("images/borders/closedcircle9.png", Texture.class)); texturenames.add("closedcircle9.png");
            textures.add(assetManager.get("images/borders/closedcircle10.png", Texture.class)); texturenames.add("closedcircle10.png");
            textures.add(assetManager.get("images/borders/closedcircle11.png", Texture.class)); texturenames.add("closedcircle11.png");
            textures.add(assetManager.get("images/borders/closedcircle12.png", Texture.class)); texturenames.add("closedcircle12.png");
            textures.add(assetManager.get("images/borders/closedcircle13.png", Texture.class)); texturenames.add("closedcircle13.png");
            textures.add(assetManager.get("images/borders/closedcircle14.png", Texture.class)); texturenames.add("closedcircle14.png");
            textures.add(assetManager.get("images/borders/closedcircle15.png", Texture.class)); texturenames.add("closedcircle15.png");
            textures.add(assetManager.get("images/borders/closedcircle16.png", Texture.class)); texturenames.add("closedcircle16.png");
            return true;
        }
        return false;
    }
    public void loadTextures() {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;
        //SOUNDS
        assetManager.load("sounds/coin.wav", Sound.class);
        assetManager.load("sounds/hit.wav", Sound.class);
        assetManager.load("sounds/tap.wav", Sound.class);
        //SKINS
        assetManager.load("images/skins/rope_brown.png", Texture.class, param);
        assetManager.load("images/skins/rope_green.png", Texture.class, param);
        assetManager.load("images/skins/ball_fruit_watermelon.png", Texture.class, param);
        assetManager.load("images/skins/ball_fruit_orange.png", Texture.class, param);
        assetManager.load("images/skins/ball_fruit_kiwi.png", Texture.class, param);
        assetManager.load("images/skins/ball_yin_yang.png", Texture.class, param);
        // CUSTOM SKINS
        assetManager.load("images/skins/ball_smile.png", Texture.class, param);
        assetManager.load("images/skins/ball_caitlin.png", Texture.class, param);
        assetManager.load("images/skins/ball_fred.png", Texture.class, param);

        assetManager.load("images/skins/ball_purple.png", Texture.class, param);
        assetManager.load("images/skins/ball_yellow.png", Texture.class, param);
        assetManager.load("images/skins/ball_tyre.png", Texture.class, param);
        assetManager.load("images/skins/ball_aztec_1.png", Texture.class, param);
        assetManager.load("images/skins/rope_chain.png", Texture.class, param);
        assetManager.load("images/skins/holder.png", Texture.class, param);
        assetManager.load("images/skins/rod_chain.png", Texture.class, param);
        assetManager.load("images/skins/rope_purple.png", Texture.class, param);
        assetManager.load("images/skins/rope_yellow.png", Texture.class, param);
        assetManager.load("images/skins/rope_aztec_1.png", Texture.class, param);
        assetManager.load("images/skins/rod_aztec_1.png", Texture.class, param);
        assetManager.load("images/skins/ball_wooden_wheel.png", Texture.class, param);
        assetManager.load("images/skins/ball_steering_wheel.png", Texture.class, param);
        assetManager.load("images/skins/rope_wooden.png", Texture.class, param);
        //CHASER
        assetManager.load("images/chaser/chaser1.png", Texture.class, param);
        assetManager.load("images/chaser/chaser2.png", Texture.class, param);
        assetManager.load("images/chaser/chaser3.png", Texture.class, param);
        assetManager.load("images/chaser/chaser4.png", Texture.class, param);
        assetManager.load("images/chaser/chaser5.png", Texture.class, param);
        assetManager.load("images/chaser/chaser6.png", Texture.class, param);
        assetManager.load("images/chaser/chaser7.png", Texture.class, param);
        assetManager.load("images/chaser/chaser8.png", Texture.class, param);
        //GAME TEXTURES
        assetManager.load("images/sound_off.png", Texture.class, param);
        assetManager.load("images/sound_on.png", Texture.class, param);
        assetManager.load("images/arrow_invert.png", Texture.class, param);
        assetManager.load("images/slider.png", Texture.class, param);
        assetManager.load("images/play.png", Texture.class, param);
        assetManager.load("images/title.png", Texture.class, param);
        assetManager.load("images/replay.png", Texture.class, param);
        assetManager.load("images/arrow.png", Texture.class, param);
        assetManager.load("images/shop.png", Texture.class, param);
        assetManager.load("images/lock.png", Texture.class, param);
        assetManager.load("images/platform.png", Texture.class, param);
        assetManager.load("images/score.png", Texture.class, param);
        assetManager.load("images/tap_up.png", Texture.class, param);
        assetManager.load("images/tap_down.png", Texture.class, param);
        assetManager.load("images/platformend.png", Texture.class, param);
        assetManager.load("images/background.png", Texture.class, param);
        assetManager.load("images/post.png", Texture.class, param);
        assetManager.load("images/gameover.png", Texture.class, param);
        assetManager.load("images/logo.png", Texture.class, param);
        // UI JAZZ
        assetManager.load("images/transparent_menu.png", Texture.class, param);
        assetManager.load("images/button.png", Texture.class, param);
        assetManager.load("images/button_long.png", Texture.class, param);
        assetManager.load("images/button_twitter.png", Texture.class, param);
        assetManager.load("images/button_instagram.png", Texture.class, param);
        assetManager.load("images/button_sound_on.png", Texture.class, param);
        assetManager.load("images/button_sound_off.png", Texture.class, param);
        assetManager.load("images/button_left_arrow.png", Texture.class, param);
        assetManager.load("images/button_right_arrow.png", Texture.class, param);
        assetManager.load("images/button_shop.png", Texture.class, param);
        assetManager.load("images/button_confirm.png", Texture.class, param);
        assetManager.load("images/button_cancel.png", Texture.class, param);
        assetManager.load("images/help_image_1.png", Texture.class, param);
        assetManager.load("images/help_image_2.png", Texture.class, param);
        assetManager.load("images/help_image_3.png", Texture.class, param);
        // BORDERS
        assetManager.load("images/borders/blocked.png", Texture.class, param);
        assetManager.load("images/borders/blocked1.5.png", Texture.class, param);
        assetManager.load("images/borders/blocked2.png", Texture.class, param);
        assetManager.load("images/borders/blocked2.5.png", Texture.class, param);
        assetManager.load("images/borders/blocked3.png", Texture.class, param);
        assetManager.load("images/borders/blocked3.5.png", Texture.class, param);
        assetManager.load("images/borders/blocked4.png", Texture.class, param);
        assetManager.load("images/borders/blocked4.5.png", Texture.class, param);
        assetManager.load("images/borders/blocked4.51.png", Texture.class, param);
        assetManager.load("images/borders/blocked4.52.png", Texture.class, param);
        assetManager.load("images/borders/blocked5.png", Texture.class, param);
        assetManager.load("images/borders/blocked5.5.png", Texture.class, param);
        assetManager.load("images/borders/blocked6.png", Texture.class, param);
        assetManager.load("images/borders/blocked6.5.png", Texture.class, param);
        assetManager.load("images/borders/blocked7.png", Texture.class, param);
        assetManager.load("images/borders/blocked7.5.png", Texture.class, param);
        assetManager.load("images/borders/open.png", Texture.class, param);
        assetManager.load("images/borders/open1.5.png", Texture.class, param);
        assetManager.load("images/borders/open2.png", Texture.class, param);
        assetManager.load("images/borders/open2.5.png", Texture.class, param);
        assetManager.load("images/borders/open3.png", Texture.class, param);
        assetManager.load("images/borders/open3.5.png", Texture.class, param);
        assetManager.load("images/borders/open4.png", Texture.class, param);
        assetManager.load("images/borders/open4.5.png", Texture.class, param);
        assetManager.load("images/borders/open4.51.png", Texture.class, param);
        assetManager.load("images/borders/open4.52.png", Texture.class, param);
        assetManager.load("images/borders/open5.png", Texture.class, param);
        assetManager.load("images/borders/open5.5.png", Texture.class, param);
        assetManager.load("images/borders/open6.png", Texture.class, param);
        assetManager.load("images/borders/open6.5.png", Texture.class, param);
        assetManager.load("images/borders/open7.png", Texture.class, param);
        assetManager.load("images/borders/open7.5.png", Texture.class, param);
        assetManager.load("images/borders/opencircle1.png", Texture.class, param);
        assetManager.load("images/borders/opencircle2.png", Texture.class, param);
        assetManager.load("images/borders/opencircle3.png", Texture.class, param);
        assetManager.load("images/borders/opencircle4.png", Texture.class, param);
        assetManager.load("images/borders/opencircle5.png", Texture.class, param);
        assetManager.load("images/borders/opencircle6.png", Texture.class, param);
        assetManager.load("images/borders/opencircle7.png", Texture.class, param);
        assetManager.load("images/borders/opencircle8.png", Texture.class, param);
        assetManager.load("images/borders/opencircle9.png", Texture.class, param);
        assetManager.load("images/borders/opencircle10.png", Texture.class, param);
        assetManager.load("images/borders/opencircle11.png", Texture.class, param);
        assetManager.load("images/borders/opencircle12.png", Texture.class, param);
        assetManager.load("images/borders/opencircle13.png", Texture.class, param);
        assetManager.load("images/borders/opencircle14.png", Texture.class, param);
        assetManager.load("images/borders/opencircle15.png", Texture.class, param);
        assetManager.load("images/borders/opencircle16.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle1.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle2.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle3.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle4.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle5.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle6.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle7.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle8.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle9.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle10.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle11.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle12.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle13.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle14.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle15.png", Texture.class, param);
        assetManager.load("images/borders/closedcircle16.png", Texture.class, param);
    }
    public Texture getTexture(String texturename) {
        for(int i=0; i<texturenames.size(); i++) {
            if(texturenames.get(i).contains(texturename)) {
                return textures.get(i);
            }
        }
        System.err.println("TEXTURE NOT FOUND IN TEXTURE HOLDER: " + texturename);
        return null;
    }
}
