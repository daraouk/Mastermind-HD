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
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.graphics.Typeface;

public class ResourceManager {
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	private static final ResourceManager INSTANCE = new ResourceManager();

	public Engine engine;
	public BaseActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	
	/***************** start ****************/
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
	/***************** end *******************/

	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadGameGraphics() {
		/* *
		 * CREATE BACKGROUND TEXTURE
		 */
		bgTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 1024, TextureOptions.BILINEAR); // must be power of 2
		bgTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bgTexture, activity, "wood_bg.jpg", 0, 0);
		bgSprite = new Sprite(0, 0, bgTextureRegion,
				activity.getVertexBufferObjectManager()); // create sprite to set background

		bgTexture.load(); // load bgTexture to the scene

		/* *
		 * CREATE SPRITE SHEET FOR COLOURED BALLS
		 * Width: 1x64px = 64px
		 * Height: 9x64px = 576px
		 * BitmapTextureAtlas: must use 2^x value for the width and height
		 * */
		ballTexture = new BitmapTextureAtlas(activity.getTextureManager(), 64, 576);

		ballTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "red_ball.png", 0, 0);
		ballTextureRegionArray[1] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "blue_ball.png", 0, 64);
		ballTextureRegionArray[2] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "green_ball.png", 0, 128);
		ballTextureRegionArray[3] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "purple_ball.png", 0, 192);
		ballTextureRegionArray[4] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "yellow_ball.png", 0, 256);
		ballTextureRegionArray[5] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "orange_ball.png", 0, 320);
		ballTextureRegionArray[6] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "black_ball.png", 0, 384);
		ballTextureRegionArray[7] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "white_ball.png", 0, 448);
		ballTextureRegionArray[8] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, activity, "select_ball.png", 0, 512);

		ballTexture.load(); // load ballTexture to the scene

		/* *
		 * CREATE SPRITE SHEET FOR BLACK & WHITE PEGS
		 * Width: 1x64px = 64px
		 * Height: 12x64px = 768px
		 * */
		pegTexture = new BitmapTextureAtlas(activity.getTextureManager(), 64, 768);

		pegTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_1B.png", 0, 0);
		pegTextureRegionArray[1] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_1B2W.png", 0, 64);
		pegTextureRegionArray[2] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_1B3W.png", 0, 128);
		pegTextureRegionArray[3] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_1W.png", 0, 192);
		pegTextureRegionArray[4] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_2B.png", 0, 256);
		pegTextureRegionArray[5] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_2B2W.png", 0, 320);
		pegTextureRegionArray[6] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_2W.png", 0, 384);
		pegTextureRegionArray[7] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_3B.png", 0, 448);
		pegTextureRegionArray[8] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_3B1W.png", 0, 512);
		pegTextureRegionArray[9] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_3W.png", 0, 576);
		pegTextureRegionArray[10] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_4B.png", 0, 640);
		pegTextureRegionArray[11] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, activity, "peg_4W.png", 0, 704);

		pegTexture.load(); // load pegTexture to the scene

		/* CREATE SPRITE SHEET FOR GAME OVER (LOSE) */
		loseTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256);
		loseTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(loseTexture, activity, "gameover_lose.png", 0, 0);

		loseTexture.load(); // load gameOverTexture to the scene
	}

	private void loadGameFonts() {
		// load fonts
		this.mFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),
				256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(activity.getFontManager(), this.mFontTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20, true,
				Color.WHITE);
	}

	private void loadGameAudio() {

	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 1024,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.jpg", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br><br>
	 * We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, BaseActivity activity,
			Camera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------

	public static ResourceManager getInstance() {
		return INSTANCE;
	}
}
