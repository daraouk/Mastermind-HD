/*********************************************************
 * GAME TITLE: Mastermind HD
 * AUTHOR: Dara Ouk
 * VERSION: 1.0
 * DESCRIPTION: A simple Mastermind game in HD
 * 	graphics and scoring/leaderboard system.
 * ===> Added this one line just to test using GitHub.
 *********************************************************/

package com.eklypze.android.mastermdhd;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.util.Log;

public class GameActivity extends BaseGameActivity {
	/***************************
	 * DECLARATIONS
	 ***************************/
	Scene scene;
	/* CAMERA */
	protected static final int CAMERA_WIDTH = 480;
	protected static final int CAMERA_HEIGHT = 800;
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
	/* GAME OPTIONS */
	private int turn = 0; // can you be more specific? turn vs turn2
	private int turn2 = 0; // per line turn counter
	private int currentX = 1;
	private int currentY = 13;
	Sprite boardPieces[] = new Sprite[40];
	Sprite bwPegs[] = new Sprite[10];
	Sprite nextSpot;
	Sprite gameoverLose, gameoverWin; // ***will replace with scenes***
	Boolean gameOver = false;
	Boolean doublesAllowed = false;
	private int[] code = new int[4]; // array to store generated code
	private int[] codeCopy = new int[4]; // for bw peg use
	private int blackPegs = 0, whitePegs = 0;
	// remember to take currentX-1 as the array indexes
	private int[] currentXValues = new int[4];
	// dummy variable when drawing panel for touch *don't delete*
	private int z = 0;

	@Override
	/***************************
	 * onCreateEngineOptions
	 ***************************/
	public EngineOptions onCreateEngineOptions() {
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		// RatioResolutionPolicy is for scaling for different devices
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	/***************************
	 * onCreateResources 
	 ***************************/
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		// STEP 1: Load Graphics
		loadGfx();
		// STEP 2: Start a New Game
		newGame();
		// callback once done loading resources
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	/*============================
	 * LOAD GRAPHICS 
	 *===========================*/
	private void loadGfx() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		/* *
		 * CREATE BACKGROUND TEXTURE
		 */
		bgTexture = new BitmapTextureAtlas(getTextureManager(), 480, 800);
        bgTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgTexture, this, "wood_bg.jpg", 0, 0);
        bgSprite = new Sprite(0, 0, bgTextureRegion, getVertexBufferObjectManager()); // create sprite to set background
        
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

	/*============================
	 * START A NEW GAME 
	 *===========================*/
	private void newGame() {
		/* [START] Generate New Code Combination */
		for (int x = 0; x < 4; x++) {
			Random r = new Random();
			int randomNumber = r.nextInt(7 - 0) + 0; // why (7-0)+0?
			code[x] = randomNumber;
			// write to log (debugging)
			Log.v("theCode", "Number generated for " + x + " is: " + code[x]
					+ " (" + ballColours[randomNumber] + ")");
			// if doubles is not allowed check if new generated number is
			// a double, if yes, generate another number. NOTE: doubles are
			// defaulted to 'OFF' until feature is added.
			if (!doublesAllowed && x > 0) {
				for (int y = x - 1; y >= 0; y--) {
					// Log.v("theY", "y is "+y);
					if (code[y] == randomNumber) {
						x--;
					}
				}
			}
		} /* [END] Generate New Code Combination */
		codeCopy = code.clone(); // copies code array for white/black peg use
	}

	@Override
	/***************************
	 * onCreateScene 
	 ***************************/
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// create scene with bgSprite background
		this.scene = new Scene();
		this.scene.setBackground(new SpriteBackground(bgSprite));
		// load fonts
		this.mFontTexture = new BitmapTextureAtlas(getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(getFontManager(), this.mFontTexture,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20, true,
				Color.WHITE);
		// callback once done creating scene
		pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
	}

	@Override
	/***************************
	 * onPopulateScene 
	 ***************************/
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// STEP 3: Draw Panel
		drawPanel();
		// callback once done populating scene
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	/*============================
	 * DRAW PANEL 
	 *===========================*/
	private void drawPanel() {
		int column = 7; // constant?
		int rowStart = 2;

		/* [START] Draw Selection Panel */
		for (int i = 0; i < 8; i++) {
			final int j = i;
			panel[i] = new Sprite(grid_xPixel(column), grid_yPixel(rowStart),
					ballTextureRegionArray[i],
					this.mEngine.getVertexBufferObjectManager()) {
				@Override
				/* [START] Touch Detection */
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					switch (pSceneTouchEvent.getAction()) {
					// On Touch
					case TouchEvent.ACTION_DOWN:
						this.setScale(2.0f); // enlarge effect
						z = j;
						Log.v("thisIsZ", "Z: " + z);
						break;
					// On Move/Drag
					case TouchEvent.ACTION_MOVE: {
						/* to be implemented in the future */
						// this.setPosition(pSceneTouchEvent.getX() -
						// this.getWidth()/2, pSceneTouchEvent.getY() -
						// this.getHeight()/2);
						break;
					}
					// On Release
					case TouchEvent.ACTION_UP:
						// z = j; *** not needed ***
						makeMove(z);
						this.setScale(1.0f); // normal size
						break;
					}
					return super.onAreaTouched(pSceneTouchEvent,
							pTouchAreaLocalX, pTouchAreaLocalY);
				}
				/* [END] Touch Detection */
			};
			this.scene.registerTouchArea(panel[i]);
			this.scene.attachChild(panel[i]);
			rowStart++;
		} /* [END] Draw Selection Panel */
		this.scene.setTouchAreaBindingOnActionDownEnabled(true);
	}

	/*============================
	 * MAKE MOVE (PLAYER)
	 *===========================*/
	private void makeMove(int inColor) {
		boardPieces[turn] = new Sprite(grid_xPixel(currentX),
				grid_yPixelboard(currentY), ballTextureRegionArray[inColor],
				this.mEngine.getVertexBufferObjectManager());
		boardPieces[turn].setScale(0.75f); // set 75% size on board

		// store current line, compare values to code and generate B/W pegs
		currentXValues[currentX - 1] = inColor;
		if (currentXValues[currentX - 1] == codeCopy[currentX - 1]) {
			blackPegs++;
			// dummy variable so this isn't counted again as a white peg
			codeCopy[currentX - 1] = 999;
		}

		for (int i = 0; i < 4; i++) {
			if ((currentXValues[currentX - 1] == codeCopy[i])) {
				whitePegs++;
				// dummy variable so this isn't counted again as a white peg
				codeCopy[i] = 999;
			}
		}

		/* log for debugging */
		Log.v("pegs", "blackPegs: " + blackPegs);
		Log.v("pegs", "whitePegs: " + whitePegs);

		// Draw pieces to scene and advance to next turn & column
		this.scene.attachChild(boardPieces[turn]);
		currentX++;
		turn++;

		// advance to next row, draw B/W pegs
		if (currentX > 4) {
			currentX = 1;
			currentY--;

			// Draw Pegs
			drawBWPegs(blackPegs, whitePegs);
			turn2++;

			// Reset pegs for next line
			blackPegs = 0;
			whitePegs = 0;

			// codeCopy is only used for counting black and white
			// pegs per line to ensure all cases work
			codeCopy = code.clone();
		}

		/* [START] Draw Blinking Cursor in Next Spot */
		nextSpot = new Sprite(grid_xPixel(currentX),
				grid_yPixelboard(currentY), ballTextureRegionArray[8],
				this.mEngine.getVertexBufferObjectManager());
		nextSpot.setScale(0.75f);

		nextSpot.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		nextSpot.registerEntityModifier(new LoopEntityModifier(
				new AlphaModifier(2, 0f, 1.0f)));
		this.scene.attachChild(nextSpot);
		/* [END] Draw Blinking Cursor in Next Spot */

		// =================== UNUSED CODE ===================
		// final Text textCenter = new Text(70, 60, this.mFont,
		// "Hello AndEngine!\nYou can even have multilined text!",
		// getVertexBufferObjectManager());
		// this.scene.attachChild(textCenter);
		// =================== UNUSED CODE ===================

		/* *
		 * GAME OVER (LOSE)
		 *  If player reaches turn 40 and still has not received
		 *  correct code, go to Game Over (Lose) scene.
		 * */
		if (turn == 40) {
			// NOTE: I will replace this with a Game Over scene.
			GameOverWin(false);
			Log.v("Game Over", "You Lose");
			gameoverLose = new Sprite(CAMERA_WIDTH / 2 - 256,
					CAMERA_HEIGHT / 2 - 64, loseTextureRegionArray[0],
					this.mEngine.getVertexBufferObjectManager());
			this.scene.attachChild(gameoverLose);
		}
	}

	/*============================
	 * GAME OVER (WIN)
	 *===========================*/
	private void GameOverWin(boolean win) {
		// clear game
		scene.detachChildren();
		turn = 0;

	}

	/*============================
	 * DRAW BLACK & WHITE PEGS
	 *===========================*/
	private void drawBWPegs(int numBlack, int numWhite) {
		/* [START] if */
		// do not display if no pegs were counted
		if (numBlack > 0 || numWhite > 0) {
			int pegScore = 0;
			// determine pegScore
			if (numBlack == 1 && numWhite == 0) {
				pegScore = 0;
			} else if (numBlack == 1 && numWhite == 2) {
				pegScore = 1;
			} else if (numBlack == 1 && numWhite == 3) {
				pegScore = 2;
			} else if (numBlack == 0 && numWhite == 1) {
				pegScore = 3;
			} else if (numBlack == 2 && numWhite == 0) {
				pegScore = 4;
			} else if (numBlack == 2 && numWhite == 2) {
				pegScore = 5;
			} else if (numBlack == 0 && numWhite == 2) {
				pegScore = 6;
			} else if (numBlack == 3 && numWhite == 0) {
				pegScore = 7;
			} else if (numBlack == 3 && numWhite == 1) {
				pegScore = 8;
			} else if (numBlack == 0 && numWhite == 3) {
				pegScore = 9;
			} else if (numBlack == 4 && numWhite == 0) {
				pegScore = 10;
			} else if (numBlack == 0 && numWhite == 4) {
				pegScore = 11;
			} /* [END] if */
			// use pegScore to display corresponding image
			bwPegs[turn2] = new Sprite(grid_xPixel(5),
					grid_yPixelboard(currentY + 1),
					pegTextureRegionArray[pegScore],
					this.mEngine.getVertexBufferObjectManager());
			bwPegs[turn2].setScale(0.80f);
			this.scene.attachChild(bwPegs[turn2]);
		}
	}

	/*============================
	 * COVERT GRID-PIXEL COORDS
	 *===========================*/
	/* Converts grid coordinates to pixel coordinates.
	 * Based on a 480x800 resolution screen. */
	private int grid_xPixel(int x) {
		int pixel = 0;
		pixel = x * (CAMERA_WIDTH / 8 + 2) - 32;
		return pixel;
	}

	// Y grid for input panel
	private int grid_yPixel(int y) {
		int pixel = 0;
		pixel = y * (CAMERA_HEIGHT / 10) - 32;
		return pixel;
	}

	// Y grid for game board
	private int grid_yPixelboard(int y) {
		int pixel = 0;
		pixel = y * (CAMERA_HEIGHT / 15) - 32;
		return pixel;
	}
}
