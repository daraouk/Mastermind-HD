/*********************************************************
 * GAME TITLE: Mastermind HD - Main Menu
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Main menu screen with play, settings, quit
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
 * Main menu screen - first screen players see
 */
public class MainMenuScreen implements Screen {

    private final MastermindHDGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    // Menu buttons
    private Rectangle playButton;
    private Rectangle settingsButton;
    private Rectangle quitButton;

    private float time = 0;
    private SoundManager soundManager;

    public MainMenuScreen(MastermindHDGame game) {
        this.game = game;
        this.soundManager = SoundManager.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT, camera);
        camera.position.set(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 0);
        touchPoint = new Vector3();

        // Load assets
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

        // Create buttons
        float buttonWidth = 300;
        float buttonHeight = 80;
        float centerX = MastermindHDGame.GAME_WIDTH / 2f - buttonWidth / 2;
        float startY = 350;
        float spacing = 100;

        playButton = new Rectangle(centerX, startY, buttonWidth, buttonHeight);
        settingsButton = new Rectangle(centerX, startY - spacing, buttonWidth, buttonHeight);
        quitButton = new Rectangle(centerX, startY - spacing * 2, buttonWidth, buttonHeight);
    }

    @Override
    public void show() {
        Gdx.app.log("MainMenu", "Main menu shown");
        soundManager.loadSettings();
        soundManager.playMusic();
    }

    @Override
    public void render(float delta) {
        time += delta;

        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.15f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Handle input
        handleInput();

        // Draw background
        game.batch.begin();
        backgroundSprite.draw(game.batch);
        game.batch.end();

        // Draw buttons
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Play button (green)
        shapeRenderer.setColor(0.2f, 0.7f, 0.2f, 1f);
        shapeRenderer.rect(playButton.x, playButton.y, playButton.width, playButton.height);

        // Settings button (blue)
        shapeRenderer.setColor(0.2f, 0.2f, 0.7f, 1f);
        shapeRenderer.rect(settingsButton.x, settingsButton.y, settingsButton.width, settingsButton.height);

        // Quit button (red)
        shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);

        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Title with pulsing effect
        float titleScale = 4.0f + (float)Math.sin(time * 2) * 0.3f;
        titleFont.getData().setScale(titleScale);
        layout.setText(titleFont, "MASTERMIND HD");
        float titleX = (MastermindHDGame.GAME_WIDTH - layout.width) / 2;
        titleFont.draw(game.batch, "MASTERMIND HD", titleX, 650);

        // Button text
        font.getData().setScale(2.5f);
        drawCenteredText("PLAY", playButton);
        drawCenteredText("SETTINGS", settingsButton);
        drawCenteredText("QUIT", quitButton);

        // Progress info
        GameProgress progress = GameProgress.getInstance();
        font.getData().setScale(1.5f);
        String progressText = String.format("Level %d | %d⭐ | %d%% Complete",
                progress.getHighestUnlockedLevel(),
                progress.getTotalStars(),
                progress.getCompletionPercentage());
        layout.setText(font, progressText);
        font.draw(game.batch, progressText, (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 150);

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

            if (playButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                soundManager.playWhoosh();
                game.setScreen(new LevelSelectScreen(game));
            } else if (settingsButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new SettingsScreen(game));
            } else if (quitButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                Gdx.app.exit();
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
    }
}
