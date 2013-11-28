/*********************************************************
 * GAME TITLE: Mastermind HD
 * AUTHOR: Dara Ouk
 * VERSION: 1.0
 * DESCRIPTION: A simple Mastermind game in HD
 * 	graphics and scoring/leaderboard system.
 *********************************************************/

package com.eklypze.android.mastermdhd;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class BaseActivity extends BaseGameActivity {
	/*** DECLARATIONS ***/
	private Camera mCamera;
	private final int CAMERA_WIDTH = 480;
	private final int CAMERA_HEIGHT = 800;
	private ResourceManager resourceManager;

	/********************************
	 * onCreateEngine()
	 ********************************/
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// limit FPS to 60 frames per second
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	/********************************
	 * onCreateEngineOptions()
	 ********************************/
	public EngineOptions onCreateEngineOptions() {
		// set camera & engine options
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);

		return engineOptions;
	}

	@Override
	/********************************
	 * onCreateResources()
	 ********************************/
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		// initalize resourceManager class with necessary components
		ResourceManager.prepareManager(mEngine, this, mCamera,
				getVertexBufferObjectManager());
		// share instance
		resourceManager = ResourceManager.getInstance();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	/********************************
	 * onCreateScene()
	 ********************************/
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		// initialize the SplashScene and callback from within
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	/********************************
	 * onPopulateScene()
	 ********************************/
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		// set timer for splash to finish and then create the GameScene
		mEngine.registerUpdateHandler(new TimerHandler(2f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						SceneManager.getInstance().createGameScene();
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}
