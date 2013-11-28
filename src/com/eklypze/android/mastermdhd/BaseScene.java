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
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.eklypze.android.mastermdhd.SceneManager.SceneType;

public abstract class BaseScene extends Scene {
	/*** DECLARATIONS ***/
	/* Settings */
	protected Engine eng;
	protected Activity act;
	protected ResourceManager resourceManager;
	protected VertexBufferObjectManager vbom;
	protected Camera cam;

	/************************************
	 * -----------CONSTRUCTOR-----------
	 ************************************/
	public BaseScene() {
		this.resourceManager = ResourceManager.getInstance();
		this.eng = resourceManager.eng;
		this.act = resourceManager.act;
		this.vbom = resourceManager.vbom;
		this.cam = resourceManager.cam;
		createScene();
	}

	/************************************
	 * -----------ABSTRACTION-----------
	 ************************************/

	/*** createScene() ***/
	public abstract void createScene();

	/*** onBackKeyPressed() ***/
	public abstract void onBackKeyPressed();

	/*** getSceneType() ***/
	public abstract SceneType getSceneType();

	/*** disposeScene() ***/
	public abstract void disposeScene();
}
