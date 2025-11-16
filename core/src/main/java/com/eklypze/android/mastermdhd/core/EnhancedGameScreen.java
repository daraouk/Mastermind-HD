/*********************************************************
 * GAME TITLE: Mastermind HD - Enhanced Game Screen
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Full-featured game screen with all enhancements
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Enhanced game screen with all features
 */
public class EnhancedGameScreen implements Screen {

    private final MastermindHDGame game;
    private final Level level;
    private final MastermindGame gameLogic;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    // Textures
    private Texture backgroundTexture;
    private Texture[] ballTextures;
    private Texture[] pegTextures;
    private Sprite backgroundSprite;

    // UI
    private BitmapFont font;
    private BitmapFont smallFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    // Game elements
    private Sprite[] selectionPanel;
    private Rectangle[] panelBounds;
    private Sprite[][] boardPieces;
    private Sprite[] feedbackPegs;

    // UI Buttons
    private Rectangle hintButton;
    private Rectangle pauseButton;
    private Rectangle backButton;

    // State
    private float blinkTimer = 0;
    private boolean isPaused = false;
    private SoundManager soundManager;

    private static final float BALL_SIZE = 64f;
    private static final float BALL_SCALE = 0.75f;

    public EnhancedGameScreen(MastermindHDGame game, Level level) {
        this.game = game;
        this.level = level;
        this.gameLogic = new MastermindGame(level);
        this.soundManager = SoundManager.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT, camera);
        camera.position.set(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 0);
        touchPoint = new Vector3();

        selectionPanel = new Sprite[8];
        panelBounds = new Rectangle[8];
        boardPieces = new Sprite[level.getMaxTurns()][level.getCodeLength()];
        feedbackPegs = new Sprite[level.getMaxTurns()];

        loadAssets();
        setupUI();
    }

    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("gfx/wood_bg.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setPosition(0, 0);
        backgroundSprite.setSize(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT);

        String[] ballFiles = {
                "gfx/red_ball.png", "gfx/blue_ball.png", "gfx/green_ball.png",
                "gfx/purple_ball.png", "gfx/yellow_ball.png", "gfx/orange_ball.png",
                "gfx/black_ball.png", "gfx/white_ball.png"
        };

        ballTextures = new Texture[8];
        for (int i = 0; i < 8; i++) {
            ballTextures[i] = new Texture(Gdx.files.internal(ballFiles[i]));
        }

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

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        smallFont = new BitmapFont();
        smallFont.setColor(Color.YELLOW);
        smallFont.getData().setScale(1.2f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();
    }

    private void setupUI() {
        // Selection panel
        int panelColumn = 7;
        int panelRowStart = 2;

        int colorsToShow = Math.min(level.getNumColors(), 8);
        for (int i = 0; i < colorsToShow; i++) {
            float x = gridToPixelX(panelColumn);
            float y = gridToPixelY(panelRowStart + i);

            selectionPanel[i] = new Sprite(ballTextures[i]);
            selectionPanel[i].setPosition(x, y);
            selectionPanel[i].setSize(BALL_SIZE, BALL_SIZE);

            panelBounds[i] = new Rectangle(x, y, BALL_SIZE, BALL_SIZE);
        }

        // Buttons
        hintButton = new Rectangle(10, MastermindHDGame.GAME_HEIGHT - 120, 120, 50);
        pauseButton = new Rectangle(10, MastermindHDGame.GAME_HEIGHT - 180, 120, 50);
        backButton = new Rectangle(10, MastermindHDGame.GAME_HEIGHT - 60, 120, 50);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Update game time
        if (!isPaused && !gameLogic.isGameOver()) {
            if (gameLogic.updateTime(delta)) {
                // Time expired - go to lose screen
                game.setScreen(new LoseScreen(game, level));
                return;
            }
        }

        blinkTimer += delta;

        Gdx.gl.glClearColor(0.4f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (!isPaused) {
            handleInput();
        }

        // Draw background
        game.batch.begin();
        backgroundSprite.draw(game.batch);
        game.batch.end();

        // Draw selection panel
        game.batch.begin();
        int colorsToShow = Math.min(level.getNumColors(), 8);
        for (int i = 0; i < colorsToShow; i++) {
            selectionPanel[i].draw(game.batch);
        }
        game.batch.end();

        // Draw board pieces
        game.batch.begin();
        for (int row = 0; row < level.getMaxTurns(); row++) {
            for (int col = 0; col < level.getCodeLength(); col++) {
                if (boardPieces[row][col] != null) {
                    boardPieces[row][col].draw(game.batch);
                }
            }
        }

        // Draw blinking cursor
        if (!gameLogic.isGameOver()) {
            int currentTurn = gameLogic.getCurrentTurn();
            int currentPos = gameLogic.getCurrentPosition();

            if (currentPos < level.getCodeLength()) {
                float alpha = (float) Math.abs(Math.sin(blinkTimer * Math.PI));
                float x = gridToPixelX(currentPos + 1);
                float y = gridToPixelYBoard(13 - currentTurn);

                game.batch.setColor(1, 1, 1, alpha * 0.5f);
                game.batch.draw(ballTextures[0], x, y, BALL_SIZE * BALL_SCALE, BALL_SIZE * BALL_SCALE);
                game.batch.setColor(1, 1, 1, 1);
            }
        }

        // Draw feedback pegs
        for (int row = 0; row < level.getMaxTurns(); row++) {
            if (feedbackPegs[row] != null) {
                feedbackPegs[row].draw(game.batch);
            }
        }
        game.batch.end();

        // Draw UI buttons
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.8f, 0.6f, 0.2f, 1f);
        shapeRenderer.rect(hintButton.x, hintButton.y, hintButton.width, hintButton.height);

        shapeRenderer.setColor(0.2f, 0.5f, 0.8f, 1f);
        shapeRenderer.rect(pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);

        shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);

        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Level info
        font.getData().setScale(1.3f);
        font.draw(game.batch, level.getName(), 10, MastermindHDGame.GAME_HEIGHT - 10);

        // Move counter
        String moves = "Move: " + (gameLogic.getCurrentTurn() + 1) + "/" + level.getMaxTurns();
        font.draw(game.batch, moves, MastermindHDGame.GAME_WIDTH - 150, MastermindHDGame.GAME_HEIGHT - 10);

        // Timer (if timed level)
        if (level.isTimed()) {
            int remaining = (int) gameLogic.getRemainingTime();
            int minutes = remaining / 60;
            int seconds = remaining % 60;
            String timeStr = String.format("⏱ %d:%02d", minutes, seconds);
            font.draw(game.batch, timeStr, MastermindHDGame.GAME_WIDTH / 2 - 50, MastermindHDGame.GAME_HEIGHT - 10);
        }

        // Button text
        font.getData().setScale(1.2f);
        drawCenteredText("Hint (" + gameLogic.getHintsRemaining() + ")", hintButton);
        drawCenteredText("Pause", pauseButton);
        drawCenteredText("Back", backButton);

        // Level info at bottom
        smallFont.getData().setScale(1.0f);
        String info = String.format("%d colors | %d pegs | %s",
                level.getNumColors(),
                level.getCodeLength(),
                level.allowsDuplicates() ? "Duplicates OK" : "No duplicates");
        layout.setText(smallFont, info);
        smallFont.draw(game.batch, info, (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 30);

        game.batch.end();

        // Check for game over
        if (gameLogic.isGameOver() && !isPaused) {
            if (gameLogic.didPlayerWin()) {
                int stars = gameLogic.getStarRating();
                GameProgress.getInstance().completeLevel(level.getLevelNumber(), stars);
                game.setScreen(new WinScreen(game, level, stars, gameLogic.getCurrentTurn()));
            } else {
                game.setScreen(new LoseScreen(game, level));
            }
        }
    }

    private void drawCenteredText(String text, Rectangle button) {
        layout.setText(font, text);
        float x = button.x + (button.width - layout.width) / 2;
        float y = button.y + (button.height + layout.height) / 2;
        font.draw(game.batch, text, x, y);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            // Check hint button
            if (hintButton.contains(touchPoint.x, touchPoint.y)) {
                int hintColor = gameLogic.useHint();
                if (hintColor >= 0) {
                    soundManager.playHint();
                    Gdx.app.log("GameScreen", "Hint: Color " + MastermindGame.getColorName(hintColor));
                    // Could show a visual hint here
                } else {
                    soundManager.playWrong();
                }
                return;
            }

            // Check pause button
            if (pauseButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playTap();
                isPaused = !isPaused;
                return;
            }

            // Check back button
            if (backButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new LevelSelectScreen(game));
                return;
            }

            // Check color selection
            int colorsToShow = Math.min(level.getNumColors(), 8);
            for (int i = 0; i < colorsToShow; i++) {
                if (panelBounds[i].contains(touchPoint.x, touchPoint.y)) {
                    handleColorSelection(i);
                    break;
                }
            }
        }
    }

    private void handleColorSelection(int colorIndex) {
        try {
            int currentTurn = gameLogic.getCurrentTurn();
            int currentPos = gameLogic.getCurrentPosition();

            if (currentPos >= level.getCodeLength()) {
                return;
            }

            soundManager.playPlace();

            float x = gridToPixelX(currentPos + 1);
            float y = gridToPixelYBoard(13 - currentTurn);

            boardPieces[currentTurn][currentPos] = new Sprite(ballTextures[colorIndex]);
            boardPieces[currentTurn][currentPos].setPosition(x, y);
            boardPieces[currentTurn][currentPos].setSize(BALL_SIZE * BALL_SCALE, BALL_SIZE * BALL_SCALE);

            MastermindGame.Feedback feedback = gameLogic.makeMove(colorIndex);

            if (feedback != null) {
                soundManager.playComplete();
                displayFeedback(currentTurn, feedback);

                // Play feedback sound
                if (feedback.blackPegs > 0) {
                    soundManager.playCorrect();
                } else if (feedback.whitePegs > 0) {
                    soundManager.playTap();
                }
            }
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error handling color selection", e);
        }
    }

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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        for (Texture texture : ballTextures) {
            if (texture != null) texture.dispose();
        }
        for (Texture texture : pegTextures) {
            if (texture != null) texture.dispose();
        }
        if (font != null) font.dispose();
        if (smallFont != null) smallFont.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
