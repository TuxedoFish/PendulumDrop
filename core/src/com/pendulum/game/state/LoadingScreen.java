package com.pendulum.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.pendulum.game.MyGdxGame;

/**
 * Created by Harry on 19/05/2017.
 */

public class LoadingScreen {
    private final float DEVICE_WIDTH;
    private final float DEVICE_HEIGHT;

    private Texture logo;

    private MyGdxGame app;

    private AssetManager assetManager;

    private boolean loaded = false;

    public LoadingScreen(MyGdxGame app) {
        assetManager = new AssetManager();

        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;
        assetManager.load("images/logo.png", Texture.class, param);

        this.app = app;

        DEVICE_WIDTH = Gdx.graphics.getWidth();
        DEVICE_HEIGHT = Gdx.graphics.getHeight();
    }

    public void update() {
        assetManager.update();
    }

    public void render(float delta) {
        if(assetManager.isLoaded("images/logo.png")) {
            logo = assetManager.get("images/logo.png", Texture.class);
            loaded = true;
        }

        Gdx.gl.glClearColor((255.0f), (255.0f), (255.0f), 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);

        if(loaded) {
            app.overlay.begin();
            app.overlay.draw(logo, 0.0f, (DEVICE_HEIGHT / 2.0f) - (DEVICE_WIDTH / 2.0f), DEVICE_WIDTH, DEVICE_WIDTH);
            app.overlay.end();
        }
    }
}
