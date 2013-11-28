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

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.eklypze.android.mastermdhd.SceneManager.SceneType;

public class GameScene extends BaseScene {
	/*** DECLARATIONS ***/
	/* Settings */
	protected static final int CAMERA_WIDTH = 480;
	protected static final int CAMERA_HEIGHT = 800;
	/* Sprites */
	Sprite[] selectPanel = new Sprite[8];
	Sprite boardPieces[] = new Sprite[40];
	Sprite bwPegs[] = new Sprite[10];
	Sprite nextSpot; // "next spot" cursor
	Sprite gameoverLose, gameoverWin; // REPLACE with scenes
	/* Game Options */
	private int turn = 0;
	private int turnCounter = 0; // per line turn counter
	private int currentX = 1;
	private int currentY = 13;
	private int[] code = new int[4]; // array to store generated code
	private int[] codeCopy = new int[4]; // for black&white peg use
	private int blackPegs = 0, whitePegs = 0;
	// remember to take currentX-1 as the array indexes
	private int[] currentXValues = new int[4];
	// dummy variable when drawing selectPanel for touch *don't delete*
	private int z = 0;
	Boolean gameOver = false;
	Boolean doublesAllowed = false;

	/************************************
	 * ------------INHERITED------------
	 ************************************/

	@Override
	/********************************
	 * createScene()
	 ********************************/
	public void createScene() {
		// create scene with bgSprite background
		setBackground(new SpriteBackground(resourceManager.bgSprite));
		// STEP 1: Start a New Game
		newGame();
		// STEP 2: Draw selectPanel
		drawPanel();

	}

	@Override
	/********************************
	 * onBackKeyPressed()
	 ********************************/
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	/********************************
	 * getSceneType()
	 ********************************/
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	/********************************
	 * disposeScene()
	 ********************************/
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
	}

	/************************************
	 * -----------GAME METHODS-----------
	 ************************************/

	/********************************
	 * newGame()
	 * Description: Initialize game
	 * settings for a new session.
	 ********************************/
	private void newGame() {
		/* [START] Generate New Code Combination */
		for (int x = 0; x < 4; x++) {
			Random r = new Random();
			// if doubles is not allowed check if new generated number is
			// a double, if yes, generate another number. NOTE: doubles are
			// defaulted to 'OFF' until feature is added.
			int randomNumber = r.nextInt(8); // why (7-0)+0?
			Log.v("randomR", "Number generated is " + randomNumber);
			code[x] = randomNumber;
			// write to log (debugging)
			Log.v("theCode", "Number generated for " + x + " is: " + code[x]
					+ " (" + resourceManager.ballColours[randomNumber] + ")");
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

	/********************************
	 * drawPanel()
	 * Description: Draw the panels
	 * required for user selection.
	 ********************************/
	private void drawPanel() {
		int column = 7; // constant?
		int rowStart = 2;

		/* [START] Draw Selection selectPanel */
		for (int i = 0; i < 8; i++) {
			final int j = i;
			selectPanel[i] = new Sprite(grid_xPixel(column),
					grid_yPixel(rowStart),
					resourceManager.ballTextureRegionArray[i], vbom) {
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
			registerTouchArea(selectPanel[i]);
			attachChild(selectPanel[i]);
			rowStart++;
		} /* [END] Draw Selection selectPanel */
		setTouchAreaBindingOnActionDownEnabled(true);
	}

	/********************************
	 * makeMove()
	 * Description: Allow the player
	 * to make their selection and
	 * display pegs as a result.
	 ********************************/
	private void makeMove(int inColor) {
		boardPieces[turn] = new Sprite(grid_xPixel(currentX),
				grid_yPixelBoard(currentY),
				resourceManager.ballTextureRegionArray[inColor], vbom);
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
		attachChild(boardPieces[turn]);
		currentX++;
		turn++;

		// advance to next row, draw B/W pegs
		if (currentX > 4) {
			currentX = 1;
			currentY--;

			// Draw Pegs
			drawBWPegs(blackPegs, whitePegs);
			turnCounter++;

			// Reset pegs for next line
			blackPegs = 0;
			whitePegs = 0;

			// codeCopy is only used for counting black and white
			// pegs per line to ensure all cases work
			codeCopy = code.clone();
		}

		/* [START] Draw Blinking Cursor in Next Spot */
		nextSpot = new Sprite(grid_xPixel(currentX),
				grid_yPixelBoard(currentY),
				resourceManager.ballTextureRegionArray[8], vbom);
		nextSpot.setScale(0.75f);

		nextSpot.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		nextSpot.registerEntityModifier(new LoopEntityModifier(
				new AlphaModifier(2, 0f, 1.0f)));
		attachChild(nextSpot);
		/* [END] Draw Blinking Cursor in Next Spot */

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
					CAMERA_HEIGHT / 2 - 64, resourceManager.loseTextureRegion,
					vbom);
			attachChild(gameoverLose);
		}
	}

	/********************************
	 * GameOverWin()
	 * Description: Display GameOver
	 * image as a result of the user
	 * winning the game.
	 ********************************/
	private void GameOverWin(boolean win) {
		// clear game
		detachChildren();
		turn = 0;

	}

	/********************************
	 * drawBWPegs()
	 * Description: Draw the black
	 * and white pegs to the scene
	 * based on game results.
	 ********************************/
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
			bwPegs[turnCounter] = new Sprite(grid_xPixel(5),
					grid_yPixelBoard(currentY + 1),
					resourceManager.pegTextureRegionArray[pegScore], vbom);
			bwPegs[turnCounter].setScale(0.80f);
			attachChild(bwPegs[turnCounter]);
		}
	}

	/********************************
	 * ---------GRID SYSTEM---------
	 ********************************/

	/************************************
	 * grid_xPixel()
	 * Description: Converts grid
	 * coordinates to pixel coordinates
	 * based on a 480 by 800 resolution
	 * screen. Needs to be updated.
	 ************************************/
	private int grid_xPixel(int x) {
		int pixel = 0;
		pixel = x * (CAMERA_WIDTH / 8 + 2) - 32;
		return pixel;
	}

	/************************************
	 * grid_yPixel()
	 * Description: Y-grid for user
	 * selection panel.
	 ************************************/
	private int grid_yPixel(int y) {
		int pixel = 0;
		pixel = y * (CAMERA_HEIGHT / 10) - 32;
		return pixel;
	}

	/************************************
	 * grid_yPixelBoard()
	 * Description: Y-grid for the main
	 * game board.
	 ************************************/
	private int grid_yPixelBoard(int y) {
		int pixel = 0;
		pixel = y * (CAMERA_HEIGHT / 15) - 32;
		return pixel;
	}
}
