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
	/*** DECLARATIONS ***/
	/* Settings */
	private static final ResourceManager INSTANCE = new ResourceManager();
	public Engine eng;
	public BaseActivity act;
	public Camera cam;
	public VertexBufferObjectManager vbom;
	/* Fonts */
	private Font mFont;
	private BitmapTextureAtlas mFontTexture;
	/* Textures */
	BitmapTextureAtlas splashTextureAtlas;
	BitmapTextureAtlas ballTexture, pegTexture;
	BitmapTextureAtlas winTexture, loseTexture;
	BitmapTextureAtlas bgTexture; // background textures
	ITextureRegion splashRegion;
	TextureRegion bgTextureRegion;
	TextureRegion loseTextureRegion;
	TextureRegion[] pegTextureRegionArray = new TextureRegion[12];
	/*****************************************************
	 * TEXTURE REGION -> ballTextureRegionArray 
	 * 0=red 1=blue 2=green 3=purple 4=yellow 5=orange 
	 * 6=black 7=white 8="select"
	 *****************************************************/
	TextureRegion[] ballTextureRegionArray = new TextureRegion[9];
	/* Sprites */
	Sprite bgSprite; // background sprite
	/* Other */
	String[] ballColours = { "Red", "Blue", "Green", "Purple", "Yellow",
			"Orange", "Black", "White" };

	/********************************
	 * ---------CLASS LOGIC---------
	 ********************************/

	/********************************
	 * loadGameResources()
	 ********************************/
	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	/********************************
	 * loadGameGraphics
	 ********************************/
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		/* *
		 * CREATE BACKGROUND TEXTURE
		 */
		bgTexture = new BitmapTextureAtlas(act.getTextureManager(), 512, 1024,
				TextureOptions.BILINEAR); // must be power of 2
		bgTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bgTexture, act, "wood_bg.jpg", 0, 0);
		bgSprite = new Sprite(240, 400, bgTextureRegion,
				act.getVertexBufferObjectManager()); // create sprite to set
														// background

		bgTexture.load(); // load bgTexture to the scene

		/* *
		 * CREATE SPRITE SHEET FOR COLOURED BALLS Width: 1x64px = 64px Height:
		 * 9x64px = 576px BitmapTextureAtlas: must use 2^x value for the width
		 * and height
		 */
		ballTexture = new BitmapTextureAtlas(act.getTextureManager(), 64, 576);

		ballTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "red_ball.png", 0, 0);
		ballTextureRegionArray[1] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "blue_ball.png", 0, 64);
		ballTextureRegionArray[2] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "green_ball.png", 0, 128);
		ballTextureRegionArray[3] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "purple_ball.png", 0, 192);
		ballTextureRegionArray[4] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "yellow_ball.png", 0, 256);
		ballTextureRegionArray[5] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "orange_ball.png", 0, 320);
		ballTextureRegionArray[6] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "black_ball.png", 0, 384);
		ballTextureRegionArray[7] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "white_ball.png", 0, 448);
		ballTextureRegionArray[8] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ballTexture, act, "select_ball.png", 0, 512);

		ballTexture.load(); // load ballTexture to the scene

		/* *
		 * CREATE SPRITE SHEET FOR BLACK & WHITE PEGS Width: 1x64px = 64px
		 * Height: 12x64px = 768px
		 */
		pegTexture = new BitmapTextureAtlas(act.getTextureManager(), 64, 768);

		pegTextureRegionArray[0] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_1B.png", 0, 0);
		pegTextureRegionArray[1] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_1B2W.png", 0, 64);
		pegTextureRegionArray[2] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_1B3W.png", 0, 128);
		pegTextureRegionArray[3] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_1W.png", 0, 192);
		pegTextureRegionArray[4] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_2B.png", 0, 256);
		pegTextureRegionArray[5] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_2B2W.png", 0, 320);
		pegTextureRegionArray[6] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_2W.png", 0, 384);
		pegTextureRegionArray[7] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_3B.png", 0, 448);
		pegTextureRegionArray[8] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_3B1W.png", 0, 512);
		pegTextureRegionArray[9] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_3W.png", 0, 576);
		pegTextureRegionArray[10] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_4B.png", 0, 640);
		pegTextureRegionArray[11] = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pegTexture, act, "peg_4W.png", 0, 704);

		pegTexture.load(); // load pegTexture to the scene

		/* CREATE SPRITE SHEET FOR GAME OVER (LOSE) */
		loseTexture = new BitmapTextureAtlas(act.getTextureManager(), 512, 256);
		loseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(loseTexture, act, "gameover_lose.png", 0, 0);

		loseTexture.load(); // load gameOverTexture to the scene
	}

	/********************************
	 * loadGameFonts()
	 ********************************/
	private void loadGameFonts() {
		// load fonts
		this.mFontTexture = new BitmapTextureAtlas(act.getTextureManager(),
				256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(act.getFontManager(), this.mFontTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20, true,
				Color.WHITE);
	}

	/********************************
	 * loadGameAudio()
	 ********************************/
	private void loadGameAudio() {
		// *consider* adding audio
	}

	/********************************
	 * loadSplashScreen()
	 ********************************/
	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(act.getTextureManager(),
				512, 1024, TextureOptions.BILINEAR);
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, act, "splash.jpg", 0, 0);
		splashTextureAtlas.load();
	}

	/********************************
	 * unloadSplashScreen()
	 ********************************/
	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splashRegion = null;
	}

	/*************************************
	 * prepareManager()
	 * Description: We will use this to
	 * prepare the Resource Manager so
	 * that we can later on access them
	 * from different classes.
	 *************************************/
	public static void prepareManager(Engine eng, BaseActivity act, Camera cam,
			VertexBufferObjectManager vbom) {
		getInstance().eng = eng;
		getInstance().act = act;
		getInstance().cam = cam;
		getInstance().vbom = vbom;
	}

	/********************************
	 * --------getInstance()--------
	 ********************************/
	public static ResourceManager getInstance() {
		return INSTANCE;
	}
}
