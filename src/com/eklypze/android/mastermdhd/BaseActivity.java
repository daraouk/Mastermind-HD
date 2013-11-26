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
	/*** CAMERA ***/
	private Camera camera;
	private final int CAMERA_WIDTH = 480;
	private final int CAMERA_HEIGHT = 800;
	
	private ResourceManager resourceManager;

	/********************************
	 * onCreateEngine()
	 ******************************
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	} **/
	
	@Override
	/********************************
	 * onCreateEngineOptions()
	 ********************************/
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		return engineOptions;
	}

	@Override
	/********************************
	 * onCreateResources()
	 ********************************/
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		resourceManager = ResourceManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	/********************************
	 * onCreateScene()
	 ********************************/
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	/********************************
	 * onPopulateScene()
	 ********************************/
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
	
	    mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                SceneManager.getInstance().createGameScene();
	                // load menu resources, create menu scene
	                // set menu scene using scene manager
	                // disposeSplashScene();
	                // READ NEXT ARTICLE FOR THIS PART.
	            }
	    }));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}
