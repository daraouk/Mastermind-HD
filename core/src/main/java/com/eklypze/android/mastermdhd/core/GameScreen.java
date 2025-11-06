/*********************************************************
 * GAME TITLE: Mastermind HD - Game Screen
 * AUTHOR: Dara Ouk (Modernized with libGDX)
 * VERSION: 2.0
 * DESCRIPTION: Main game screen handling rendering and input
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Main game screen - handles rendering and player input
 */
public class GameScreen implements Screen {

    // Game reference
    private final MastermindHDGame game;
    private final MastermindGame gameLogic;

    // Camera and viewport
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    // Textures
    private Texture backgroundTexture;
    private Texture[] ballTextures;
    private Texture[] pegTextures;
    private Texture gameoverLoseTexture;

    // Sprites
    private Sprite backgroundSprite;
    private Sprite[] selectionPanel;  // 8 color balls for selection
    private Rectangle[] panelBounds;  // Touch areas for panel
    private Sprite[][] boardPieces;   // [10 rows][4 columns] for guesses
    private Sprite[] feedbackPegs;    // Feedback for each row

    // Game state tracking
    private float[] blinkTimers;  // For blinking cursor effect
    private boolean[] rowsComplete; // Track which rows are complete

    // Constants
    private static final float BALL_SIZE = 64f;
    private static final float BALL_SCALE = 0.75f;
    private static final float PANEL_SCALE = 1.0f;
    private static final float BLINK_DURATION = 1.0f;

    public GameScreen(MastermindHDGame game) {
        this.game = game;
        this.gameLogic = new MastermindGame(false); // No duplicates

        // Initialize camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT, camera);
        camera.position.set(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 0);
        touchPoint = new Vector3();

        // Initialize arrays
        selectionPanel = new Sprite[MastermindGame.NUM_COLORS];
        panelBounds = new Rectangle[MastermindGame.NUM_COLORS];
        boardPieces = new Sprite[MastermindGame.MAX_TURNS][MastermindGame.CODE_LENGTH];
        feedbackPegs = new Sprite[MastermindGame.MAX_TURNS];
        blinkTimers = new float[MastermindGame.MAX_TURNS];
        rowsComplete = new boolean[MastermindGame.MAX_TURNS];

        loadAssets();
        setupGame();
    }

    /**
     * Load all game assets (textures)
     */
    private void loadAssets() {
        // Load background
        backgroundTexture = new Texture(Gdx.files.internal("gfx/wood_bg.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setPosition(0, 0);
        backgroundSprite.setSize(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT);

        // Load colored ball textures
        String[] ballFiles = {
            "gfx/red_ball.png", "gfx/blue_ball.png", "gfx/green_ball.png",
            "gfx/purple_ball.png", "gfx/yellow_ball.png", "gfx/orange_ball.png",
            "gfx/black_ball.png", "gfx/white_ball.png"
        };

        ballTextures = new Texture[8];
        for (int i = 0; i < 8; i++) {
            ballTextures[i] = new Texture(Gdx.files.internal(ballFiles[i]));
        }

        // Load peg textures (feedback)
        String[] pegFiles = {
            "gfx/peg_1B.png", "gfx/peg_1B2W.png", "gfx/peg_1B3W.png",
            "gfx/peg_1W.png", "gfx/peg_2B.png", "gfx/peg_2B2W.png",
            "gfx/peg_2W.png", "gfx/peg_3B.png", "gfx/peg_3B1W.png",
            "gfx/peg_3W.png", "gfx/peg_4B.png", "gfx/peg_4W.png"
        };

        pegTextures = new Texture[12];
        for (int i = 0; i < 12; i++) {
            pegTextures[i] = new Texture(Gdx.files.internal(pegFiles[i]));
        }

        // Load game over texture
        gameoverLoseTexture = new Texture(Gdx.files.internal("gfx/gameover_lose.png"));

        Gdx.app.log("GameScreen", "Assets loaded successfully");
    }

    /**
     * Set up the game UI (selection panel, etc.)
     */
    private void setupGame() {
        // Create selection panel on the right side
        int panelColumn = 7;
        int panelRowStart = 2;

        for (int i = 0; i < MastermindGame.NUM_COLORS; i++) {
            float x = gridToPixelX(panelColumn);
            float y = gridToPixelY(panelRowStart + i);

            selectionPanel[i] = new Sprite(ballTextures[i]);
            selectionPanel[i].setPosition(x, y);
            selectionPanel[i].setSize(BALL_SIZE * PANEL_SCALE, BALL_SIZE * PANEL_SCALE);

            // Create touch bounds
            panelBounds[i] = new Rectangle(x, y, BALL_SIZE * PANEL_SCALE, BALL_SIZE * PANEL_SCALE);
        }

        Gdx.app.log("GameScreen", "Game setup complete - secret code generated");
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Screen shown");
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.4f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Handle input
        handleInput();

        // Update blink timers
        updateBlinkTimers(delta);

        // Render everything
        game.batch.begin();

        // Draw background
        backgroundSprite.draw(game.batch);

        // Draw selection panel
        for (int i = 0; i < MastermindGame.NUM_COLORS; i++) {
            selectionPanel[i].draw(game.batch);
        }

        // Draw board pieces (player guesses)
        for (int row = 0; row < MastermindGame.MAX_TURNS; row++) {
            for (int col = 0; col < MastermindGame.CODE_LENGTH; col++) {
                if (boardPieces[row][col] != null) {
                    boardPieces[row][col].draw(game.batch);
                }
            }
        }

        // Draw blinking cursor for current position
        if (!gameLogic.isGameOver()) {
            int currentTurn = gameLogic.getCurrentTurn();
            int currentPos = gameLogic.getCurrentPosition();

            if (currentPos < MastermindGame.CODE_LENGTH) {
                // Calculate blink alpha
                float alpha = (float) Math.abs(Math.sin(blinkTimers[currentTurn] * Math.PI));

                float x = gridToPixelX(currentPos + 1);
                float y = gridToPixelYBoard(13 - currentTurn);

                game.batch.setColor(1, 1, 1, alpha * 0.5f);
                game.batch.draw(ballTextures[0], x, y, BALL_SIZE * BALL_SCALE, BALL_SIZE * BALL_SCALE);
                game.batch.setColor(1, 1, 1, 1);
            }
        }

        // Draw feedback pegs
        for (int row = 0; row < MastermindGame.MAX_TURNS; row++) {
            if (feedbackPegs[row] != null) {
                feedbackPegs[row].draw(game.batch);
            }
        }

        // Draw game over screen
        if (gameLogic.isGameOver()) {
            if (!gameLogic.didPlayerWin()) {
                float x = MastermindHDGame.GAME_WIDTH / 2f - 256;
                float y = MastermindHDGame.GAME_HEIGHT / 2f - 64;
                game.batch.draw(gameoverLoseTexture, x, y);
            }
        }

        game.batch.end();
    }

    /**
     * Handle touch input for color selection
     */
    private void handleInput() {
        if (gameLogic.isGameOver()) {
            // Could add restart functionality here
            return;
        }

        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            // Check if any panel color was touched
            for (int i = 0; i < MastermindGame.NUM_COLORS; i++) {
                if (panelBounds[i].contains(touchPoint.x, touchPoint.y)) {
                    handleColorSelection(i);
                    break;
                }
            }
        }
    }

    /**
     * Handle color selection from the panel
     */
    private void handleColorSelection(int colorIndex) {
        try {
            int currentTurn = gameLogic.getCurrentTurn();
            int currentPos = gameLogic.getCurrentPosition();

            if (currentPos >= MastermindGame.CODE_LENGTH) {
                return; // Row already complete
            }

            // Create sprite for the board
            float x = gridToPixelX(currentPos + 1);
            float y = gridToPixelYBoard(13 - currentTurn);

            boardPieces[currentTurn][currentPos] = new Sprite(ballTextures[colorIndex]);
            boardPieces[currentTurn][currentPos].setPosition(x, y);
            boardPieces[currentTurn][currentPos].setSize(BALL_SIZE * BALL_SCALE, BALL_SIZE * BALL_SCALE);

            // Make the move in game logic
            MastermindGame.Feedback feedback = gameLogic.makeMove(colorIndex);

            // If row is complete, display feedback
            if (feedback != null) {
                displayFeedback(currentTurn, feedback);
                rowsComplete[currentTurn] = true;

                Gdx.app.log("GameScreen", "Turn " + (currentTurn + 1) + " - " + feedback);

                // Check game over
                if (gameLogic.isGameOver()) {
                    if (gameLogic.didPlayerWin()) {
                        Gdx.app.log("GameScreen", "YOU WIN!");
                    } else {
                        Gdx.app.log("GameScreen", "GAME OVER - You Lose");
                    }
                }
            }

        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error handling color selection", e);
        }
    }

    /**
     * Display feedback pegs for a completed row
     */
    private void displayFeedback(int row, MastermindGame.Feedback feedback) {
        int pegIndex = getPegTextureIndex(feedback.blackPegs, feedback.whitePegs);

        if (pegIndex >= 0) {
            float x = gridToPixelX(5);
            float y = gridToPixelYBoard(13 - row);

            feedbackPegs[row] = new Sprite(pegTextures[pegIndex]);
            feedbackPegs[row].setPosition(x, y);
            feedbackPegs[row].setSize(BALL_SIZE * 0.8f, BALL_SIZE * 0.8f);
        }
    }

    /**
     * Map black/white peg counts to texture index
     * This matches your original mapping logic
     */
    private int getPegTextureIndex(int black, int white) {
        if (black == 0 && white == 0) return -1;
        if (black == 1 && white == 0) return 0;
        if (black == 1 && white == 2) return 1;
        if (black == 1 && white == 3) return 2;
        if (black == 0 && white == 1) return 3;
        if (black == 2 && white == 0) return 4;
        if (black == 2 && white == 2) return 5;
        if (black == 0 && white == 2) return 6;
        if (black == 3 && white == 0) return 7;
        if (black == 3 && white == 1) return 8;
        if (black == 0 && white == 3) return 9;
        if (black == 4 && white == 0) return 10;
        if (black == 0 && white == 4) return 11;
        return -1;
    }

    /**
     * Update blink timers for cursor animation
     */
    private void updateBlinkTimers(float delta) {
        for (int i = 0; i < blinkTimers.length; i++) {
            blinkTimers[i] += delta;
            if (blinkTimers[i] > BLINK_DURATION * 2) {
                blinkTimers[i] = 0;
            }
        }
    }

    // Grid coordinate conversion methods (matching your original logic)

    private float gridToPixelX(int gridX) {
        return gridX * (MastermindHDGame.GAME_WIDTH / 8f + 2) - 32;
    }

    private float gridToPixelY(int gridY) {
        return gridY * (MastermindHDGame.GAME_HEIGHT / 10f) - 32;
    }

    private float gridToPixelYBoard(int gridY) {
        return gridY * (MastermindHDGame.GAME_HEIGHT / 15f) - 32;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 0);
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "Game paused");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "Game resumed");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "Screen hidden");
    }

    @Override
    public void dispose() {
        // Dispose of all textures
        if (backgroundTexture != null) backgroundTexture.dispose();

        for (Texture texture : ballTextures) {
            if (texture != null) texture.dispose();
        }

        for (Texture texture : pegTextures) {
            if (texture != null) texture.dispose();
        }

        if (gameoverLoseTexture != null) gameoverLoseTexture.dispose();

        Gdx.app.log("GameScreen", "Screen disposed");
    }
}
