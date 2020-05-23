package com.pendulum.game;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AndroidLauncher extends AndroidApplication implements MyGdxGame.AdService {

	private static final String TAG = "AndroidLauncher";
	protected View gameView;

	// Banner Advert
	private static final String AD_UNIT_ID =  "ca-app-pub-8987593883601416/2645733083";
	private static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
	protected AdView bannerAdView;

	// Intersitial Advert
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-8987593883601416/5462689135";
	private static final String TEST_AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";
	private InterstitialAd interstitialAdView;

	// Reward Advert
	private static final String AD_UNIT_ID_REWARD = "ca-app-pub-8987593883601416/2658424210";
	private static final String TEST_AD_UNIT_ID_REWARD = "ca-app-pub-3940256099942544/5224354917";
	private RewardedAd rewardAdView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// SETUP THE GAME VIEW
		RelativeLayout layout = new RelativeLayout(this);
		layout.setPadding(0, 1, 0, 0);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		// SETUP THE BANNER AD VIEW
		AdView admobView = createAdView();
		View gameView = createGameView(config);
		layout.addView(gameView);
		layout.addView(admobView);

		// SETUP THE REWARD AD VIEW
		rewardAdView = createAndLoadRewardedAd();

		setContentView(layout);
		startAdvertising(admobView);

		// SETUP THE OVERLAY AD SETUP TO DISPLAY AFTER X GAMES
		interstitialAdView = new InterstitialAd(this);
			interstitialAdView.setAdUnitId(TEST_AD_UNIT_ID_INTERSTITIAL);
			interstitialAdView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {}

				@Override
				public void onAdClosed() {
					loadIntersitialAd();
				}
		});

		loadIntersitialAd();
	}

	public RewardedAd createAndLoadRewardedAd() {
		RewardedAd rewardedAd = new RewardedAd(this, TEST_AD_UNIT_ID_REWARD);
		RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
			@Override
			public void onRewardedAdLoaded() {
				// Ad successfully loaded.
			}

			@Override
			public void onRewardedAdFailedToLoad(int errorCode) {
				// Ad failed to load.
			}
		};
		rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
		return rewardedAd;
	}

	private void loadIntersitialAd(){
		AdRequest interstitialRequest = new AdRequest.Builder().build();
		interstitialAdView.loadAd(interstitialRequest);
	}

	@Override
	public void showInterstitial() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (interstitialAdView.isLoaded())
					interstitialAdView.show();
				else
					loadIntersitialAd();
			}
		});
	}

	@Override
	public void showRewardAdvert(final MyGdxGame.CustomRewardCallback callback) {
        runOnUiThread(new Runnable() {
			public void run() {
				rewardAdView.show(AndroidLauncher.this, new RewardedAdCallback() {
					@Override
					public void onRewardedAdOpened() {
						// Ad opened.
					}

					@Override
					public void onRewardedAdClosed() {
						rewardAdView = createAndLoadRewardedAd();
					}

					@Override
					public void onUserEarnedReward(@NonNull RewardItem reward) {
						callback.onSuccess();
					}

					@Override
					public void onRewardedAdFailedToShow(int errorCode) {
						callback.onFailure();
					}

				});
			}
		});
	}

	@Override
	public boolean isInterstitialLoaded() {
		return interstitialAdView.isLoaded();
	}

	private AdView createAdView() {
		bannerAdView = new AdView(this);

		bannerAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
		});

		bannerAdView.setAdSize(AdSize.SMART_BANNER);
		bannerAdView.setAdUnitId(TEST_AD_UNIT_ID);
		bannerAdView.setBackgroundColor(Color.argb(255, 255, 255, 255));

		RelativeLayout.LayoutParams ad_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		ad_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		ad_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		ad_params.addRule(RelativeLayout.ABOVE);
		bannerAdView.setLayoutParams(ad_params);

		return bannerAdView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new MyGdxGame(this), cfg);
		RelativeLayout.LayoutParams params_game = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params_game.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params_game.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params_game.addRule(RelativeLayout.BELOW, bannerAdView.getId());
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
		if (bannerAdView != null) bannerAdView.resume();
	}

	@Override
	public void onPause() {
		if (bannerAdView != null) bannerAdView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (bannerAdView != null) {
			bannerAdView.removeAllViews();
			bannerAdView.destroy();
		}
		super.onDestroy();
	}
}
