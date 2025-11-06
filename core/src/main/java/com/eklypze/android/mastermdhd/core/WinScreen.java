/*********************************************************
 * GAME TITLE: Mastermind HD - Win Screen
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Celebration screen when player wins
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
import com.badlogic.gdx.math.MathUtils;

/**
 * Win screen with stars and progression
 */
public class WinScreen implements Screen {

    private final MastermindHDGame game;
    private final Level level;
    private final int stars;
    private final int movesUsed;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    private Rectangle nextButton;
    private Rectangle retryButton;
    private Rectangle menuButton;

    private float time = 0;
    private SoundManager soundManager;
    private ParticleManager particleManager;
    private boolean celebrationStarted = false;

    public WinScreen(MastermindHDGame game, Level level, int stars, int movesUsed) {
        this.game = game;
        this.level = level;
        this.stars = stars;
        this.movesUsed = movesUsed;
        this.soundManager = SoundManager.getInstance();
        this.particleManager = new ParticleManager();

        camera = new OrthographicCamera();
        viewport = new FitViewport(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT, camera);
        camera.position.set(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 0);
        touchPoint = new Vector3();

        backgroundTexture = new Texture(Gdx.files.internal("gfx/wood_bg.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setPosition(0, 0);
        backgroundSprite.setSize(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);

        titleFont = new BitmapFont();
        titleFont.setColor(Color.GOLD);
        titleFont.getData().setScale(4.0f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        // Buttons
        float buttonWidth = 250;
        float buttonHeight = 70;
        float centerX = MastermindHDGame.GAME_WIDTH / 2f - buttonWidth / 2;

        nextButton = new Rectangle(centerX, 250, buttonWidth, buttonHeight);
        retryButton = new Rectangle(centerX, 160, buttonWidth, buttonHeight);
        menuButton = new Rectangle(centerX, 70, buttonWidth, buttonHeight);
    }

    @Override
    public void show() {
        Gdx.app.log("WinScreen", "Level " + level.getLevelNumber() + " completed with " + stars + " stars!");
        soundManager.playWin();
        soundManager.playComplete();
    }

    @Override
    public void render(float delta) {
        time += delta;

        // Start celebration effects
        if (!celebrationStarted && time > 0.5f) {
            celebrationStarted = true;
            // Create confetti burst at center
            particleManager.createConfetti(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 100);
            particleManager.createFireworks(MastermindHDGame.GAME_WIDTH / 2f, 500);
        }

        // Continue adding sparkles periodically
        if (time % 0.3f < delta) {
            particleManager.createSparkle(
                MathUtils.random(100, MastermindHDGame.GAME_WIDTH - 100),
                MathUtils.random(200, MastermindHDGame.GAME_HEIGHT - 200)
            );
        }

        particleManager.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        handleInput();

        // Draw background (dimmed)
        game.batch.begin();
        backgroundSprite.setColor(0.5f, 0.5f, 0.5f, 1f);
        backgroundSprite.draw(game.batch);
        backgroundSprite.setColor(1, 1, 1, 1);
        game.batch.end();

        // Draw buttons
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.2f, 0.7f, 0.2f, 1f);
        shapeRenderer.rect(nextButton.x, nextButton.y, nextButton.width, nextButton.height);

        shapeRenderer.setColor(0.7f, 0.5f, 0.2f, 1f);
        shapeRenderer.rect(retryButton.x, retryButton.y, retryButton.width, retryButton.height);

        shapeRenderer.setColor(0.2f, 0.2f, 0.7f, 1f);
        shapeRenderer.rect(menuButton.x, menuButton.y, menuButton.width, menuButton.height);

        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Title with animation
        float titleScale = 4.0f + (float)Math.sin(time * 3) * 0.4f;
        titleFont.getData().setScale(titleScale);
        layout.setText(titleFont, "VICTORY!");
        float titleX = (MastermindHDGame.GAME_WIDTH - layout.width) / 2;
        titleFont.draw(game.batch, "VICTORY!", titleX, 650);

        // Level name
        font.getData().setScale(1.8f);
        layout.setText(font, level.getName());
        font.draw(game.batch, level.getName(), (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 580);

        // Stars with animation
        font.getData().setScale(3.0f + (float)Math.sin(time * 4) * 0.3f);
        font.setColor(Color.GOLD);
        String starText = "";
        for (int i = 0; i < stars; i++) {
            starText += "⭐";
        }
        layout.setText(font, starText);
        font.draw(game.batch, starText, (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 500);

        // Stats
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        String stats = "Solved in " + movesUsed + " moves!";
        layout.setText(font, stats);
        font.draw(game.batch, stats, (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 400);

        // Button text
        font.getData().setScale(2.0f);
        drawCenteredText("Next Level", nextButton);
        drawCenteredText("Retry", retryButton);
        drawCenteredText("Menu", menuButton);

        // Draw particles on top
        particleManager.render(game.batch);

        game.batch.end();
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

            if (nextButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                soundManager.playUnlock();
                // Go to next level (if available)
                if (level.getLevelNumber() < 100) {
                    Level nextLevel = LevelManager.getInstance().getLevel(level.getLevelNumber() + 1);
                    game.setScreen(new EnhancedGameScreen(game, nextLevel));
                } else {
                    // All levels complete!
                    game.setScreen(new LevelSelectScreen(game));
                }
            } else if (retryButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new EnhancedGameScreen(game, level));
            } else if (menuButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new LevelSelectScreen(game));
            }
        }
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
        if (font != null) font.dispose();
        if (titleFont != null) titleFont.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (particleManager != null) particleManager.dispose();
    }
}
