/*********************************************************
 * GAME TITLE: Mastermind HD
 * AUTHOR: Dara Ouk
 * VERSION: 1.0
 * DESCRIPTION: A simple Mastermind game in HD
 * 	graphics and scoring/leaderboard system.
 *********************************************************/

package com.eklypze.android.mastermdhd;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {
	/*** Scenes ***/
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	/*** Settings ***/
	private static final SceneManager INSTANCE = new SceneManager();
	private Engine engine = ResourceManager.getInstance().eng;
	private BaseScene currentScene;
	// start with splash scene
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	/*** Scene Types ***/
	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_LOADING,
	}

	/********************************
	 * ---------CLASS LOGIC---------
	 ********************************/

	/********************************
	 * setScene(BaseScene)
	 ********************************/
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	/********************************
	 * setScene(SceneType)
	 ********************************/
	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}

	/********************************
	 * -----------GETTERS-----------
	 ********************************/

	/********************************
	 * getInstance()
	 ********************************/
	public static SceneManager getInstance() {
		return INSTANCE;
	}

	/********************************
	 * getCurrentSceneType()
	 ********************************/
	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	/********************************
	 * getCurrentScene()
	 ********************************/
	public BaseScene getCurrentScene() {
		return currentScene;
	}

	/********************************
	 * -----------SETTERS-----------
	 ********************************/

	/********************************
	 * createSplashScene()
	 ********************************/
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		setScene(splashScene);
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	/********************************
	 * disposeSplashScene()
	 ********************************/
	private void disposeSplashScene() {
		ResourceManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	/********************************
	 * createGameScene()
	 ********************************/
	public void createGameScene() {
		ResourceManager.getInstance().loadGameResources();
		gameScene = new GameScene();
		setScene(gameScene);
		disposeSplashScene();
	}
}