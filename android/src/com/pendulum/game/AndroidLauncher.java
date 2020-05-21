package com.pendulum.game;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pendulum.game.MyGdxGame;

public class AndroidLauncher extends AndroidApplication{

	private static final String TAG = "AndroidLauncher";
	private static final String AD_UNIT_ID =  "ca-app-pub-8987593883601416/2645733083";
	protected AdView adView;
	protected View gameView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		layout.setPadding(0, 1, 0, 0);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		AdView admobView = createAdView();
		View gameView = createGameView(config);
		layout.addView(gameView);
		layout.addView(admobView);

		setContentView(layout);
		startAdvertising(admobView);
	}

	private AdView createAdView() {
		adView = new AdView(this);

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
		});

		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		adView.setBackgroundColor(Color.argb(255, 255, 255, 255));

		RelativeLayout.LayoutParams ad_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		ad_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		ad_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		ad_params.addRule(RelativeLayout.ABOVE);
		adView.setLayoutParams(ad_params);

		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new MyGdxGame(), cfg);
		RelativeLayout.LayoutParams params_game = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params_game.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params_game.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params_game.addRule(RelativeLayout.BELOW, adView.getId());
		params_game.topMargin = 150;
		gameView.setLayoutParams(params_game);

		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) adView.resume();
	}

	@Override
	public void onPause() {
		if (adView != null) adView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.removeAllViews();
			adView.destroy();
		}
		super.onDestroy();
	}
}
