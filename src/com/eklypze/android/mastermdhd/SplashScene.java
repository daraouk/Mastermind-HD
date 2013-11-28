/*********************************************************
 * GAME TITLE: Mastermind HD
 * AUTHOR: Dara Ouk
 * VERSION: 1.0
 * DESCRIPTION: A simple Mastermind game in HD
 * 	graphics and scoring/leaderboard system.
 *********************************************************/

package com.eklypze.android.mastermdhd;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.eklypze.android.mastermdhd.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	/*** DECLARATIONS ***/
	private Sprite splash;

	/************************************
	 * ------------INHERITED------------
	 ************************************/

	@Override
	/********************************
	 * createScene()
	 ********************************/
	public void createScene() {
		splash = new Sprite(0, 0, resourceManager.splashRegion, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		splash.setScale(1.0f);
		splash.setPosition(240, 400); // centers image... but why???
		attachChild(splash);
	}

	@Override
	/********************************
	 * createScene()
	 ********************************/
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	/********************************
	 * getSceneType()
	 ********************************/
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}

	@Override
	/********************************
	 * disposeScene()
	 ********************************/
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}
}