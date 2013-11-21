/*********************************************************
 * GAME TITLE: Mastermind HD
 * AUTHOR: Dara Ouk
 * VERSION: 1.0
 * DESCRIPTION: A simple Mastermind game in HD
 * 	graphics and scoring/leaderboard system.
 *********************************************************/

package com.eklypze.android.mastermdhd;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

public class BaseActivity extends BaseGameActivity {
	private final int CAMERA_WIDTH = 480;
	private final int CAMERA_HEIGHT = 800;

	private Camera camera;
	private Scene splashScene;
	private Scene mainScene;

	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private Sprite splash;

	/* FONT */
	private Font mFont;
	private BitmapTextureAtlas mFontTexture;
	/* RESOURCES */
	BitmapTextureAtlas ballTexture, pegTexture;
	BitmapTextureAtlas winTexture;
	BitmapTextureAtlas loseTexture;
	BitmapTextureAtlas bgTexture; // background textures
	TextureRegion bgTextureRegion;
	// ballTextureRegionArray: 0=red 1=blue 2=green 3=purple 4=yellow 5=orange
	// 6=black 7=white 8="select"
	TextureRegion[] ballTextureRegionArray = new TextureRegion[9];
	TextureRegion[] pegTextureRegionArray = new TextureRegion[12];
	TextureRegion[] loseTextureRegionArray = new TextureRegion[2];
	Sprite[] panel = new Sprite[8]; // selection panel
	Sprite bgSprite; // background sprite
	String[] ballColours = { "Red", "Blue", "Green", "Purple", "Yellow",
			"Orange", "Black", "White" };

	private enum SceneType {
		SPLASH, MAIN, OPTIONS, WORLD_SELECTION, LEVEL_SELECTION, CONTROLLER
	}

	private SceneType currentScene = SceneType.SPLASH;

	public Engine getEngine() {
		return this.mEngine;
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				480, 800, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, this, "splash.jpg", 0, 0);
		splashTextureAtlas.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		initSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(3f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						loadResources();
						loadScenes();
						splash.detachSelf();
						mEngine.setScene(mainScene);
						currentScene = SceneType.MAIN;
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (currentScene) {
			case SPLASH:
				break;
			case MAIN:
				System.exit(0);
				break;
			}
		}
		return false;
	}

	public void loadResources() {
		/* *
		 * CREATE BACKGROUND TEXTURE
		 */
		bgTexture = new BitmapTextureAtlas(getTextureManager(), 480, 800);
		bgTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bgTexture, this, "wood_bg.jpg", 0, 0);
		bgSprite = new Sprite(0, 0, bgTextureRegion,
				getVertexBufferObjectManager()); // create sprite to set background

		bgTexture.load(); // load bgTexture to the scene

		/* *
		 * CREATE SPRITE SHEET FOR COLOURED BALLS
		 * Width: 1x64px = 64px
		 * Height: 9x64px = 576px
		 * BitmapTextureAtlas: must use 2^x value for the width and height
		 * */
		ballTexture = new BitmapTextureAtlas(getTextureManager(), 64, 576);

		ballTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "red_ball.png", 0, 0);
		ballTextureRegionArray[1] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "blue_ball.png", 0, 64);
		ballTextureRegionArray[2] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "green_ball.png", 0, 128);
		ballTextureRegionArray[3] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "purple_ball.png", 0, 192);
		ballTextureRegionArray[4] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "yellow_ball.png", 0, 256);
		ballTextureRegionArray[5] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "orange_ball.png", 0, 320);
		ballTextureRegionArray[6] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "black_ball.png", 0, 384);
		ballTextureRegionArray[7] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "white_ball.png", 0, 448);
		ballTextureRegionArray[8] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, this, "select_ball.png", 0, 512);

		ballTexture.load(); // load ballTexture to the scene

		/* *
		 * CREATE SPRITE SHEET FOR BLACK & WHITE PEGS
		 * Width: 1x64px = 64px
		 * Height: 12x64px = 768px
		 * */
		pegTexture = new BitmapTextureAtlas(getTextureManager(), 64, 768);

		pegTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_1B.png", 0, 0);
		pegTextureRegionArray[1] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_1B2W.png", 0, 64);
		pegTextureRegionArray[2] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_1B3W.png", 0, 128);
		pegTextureRegionArray[3] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_1W.png", 0, 192);
		pegTextureRegionArray[4] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_2B.png", 0, 256);
		pegTextureRegionArray[5] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_2B2W.png", 0, 320);
		pegTextureRegionArray[6] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_2W.png", 0, 384);
		pegTextureRegionArray[7] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_3B.png", 0, 448);
		pegTextureRegionArray[8] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_3B1W.png", 0, 512);
		pegTextureRegionArray[9] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_3W.png", 0, 576);
		pegTextureRegionArray[10] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_4B.png", 0, 640);
		pegTextureRegionArray[11] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, this, "peg_4W.png", 0, 704);

		pegTexture.load(); // load pegTexture to the scene

		/* CREATE SPRITE SHEET FOR GAME OVER (LOSE) */
		loseTexture = new BitmapTextureAtlas(getTextureManager(), 512, 256);
		loseTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(loseTexture, this, "gameover_lose.png", 0, 0);

		loseTexture.load(); // load gameOverTexture to the scene
	}

	private void loadScenes() {
		// load your game here, you scenes
		mainScene = new GameScene();
	}

	// ===========================================================
	// INITIALIZIE  
	// ===========================================================

	private void initSplashScene() {
		splashScene = new Scene();
		splash = new Sprite(0, 0, splashTextureRegion,
				mEngine.getVertexBufferObjectManager()) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		splash.setScale(1.5f);
		splash.setPosition((CAMERA_WIDTH - splash.getWidth()) * 0.5f,
				(CAMERA_HEIGHT - splash.getHeight()) * 0.5f);
		splashScene.attachChild(splash);
	}
}
