package com.pendulum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.pendulum.game.state.PlayScreen;

public class MyGdxGame extends ApplicationAdapter {
	public SpriteBatch overlay;
	public SpriteBatch toworld;

	private PlayScreen playScreen;
	private com.pendulum.game.state.LoadingScreen loadingScreen;

	private float STEP = 1/60f;
	private float accum = 0.0f;

	private int skippedFrames = 0;
	private final int MAX_FRAMESKIPS = 5;

	private boolean created = false;
	private boolean loading = false;

	@Override
	public void create () {
		Gdx.graphics.setTitle("Pendulum");

		overlay = new SpriteBatch();
		toworld = new SpriteBatch();

		loadingScreen = new com.pendulum.game.state.LoadingScreen(this);
	}

	@Override
	public void render () {
		if (Gdx.graphics.getDeltaTime() < 0.1) {
			accum += Gdx.graphics.getDeltaTime();
		}

		skippedFrames = 0;

		while (accum >= STEP && skippedFrames <= MAX_FRAMESKIPS) {
			accum -= STEP;
			if(created) {
				playScreen.update(STEP);
			} else {
				loadingScreen.update();
				if (loading && playScreen.isLoaded()) {
					created = true;
				}
			}
			skippedFrames++;
		}
		if(created) {
			playScreen.render();
		} else {
			loadingScreen.render(Gdx.graphics.getDeltaTime());
		}
		if(!loading) {
			loading = true;
			playScreen = new PlayScreen(this);
			playScreen.create();
		}
	}

	@Override
	public void resize(int width, int height) {
		if(loading) {playScreen.resize(width, height);}
	}

	@Override
	public void dispose () {
		playScreen.dispose();
	}
}
