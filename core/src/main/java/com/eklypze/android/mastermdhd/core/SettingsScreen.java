/*********************************************************
 * GAME TITLE: Mastermind HD - Settings Screen
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Settings and preferences
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
 * Settings screen for game preferences
 */
public class SettingsScreen implements Screen {

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

    private SoundManager soundManager;
    private GameProgress progress;

    // UI elements
    private Rectangle backButton;
    private Rectangle soundToggle;
    private Rectangle musicToggle;
    private Rectangle resetButton;
    private Rectangle aboutButton;
    private Rectangle feedbackButton;

    public SettingsScreen(MastermindHDGame game) {
        this.game = game;
        this.soundManager = SoundManager.getInstance();
        this.progress = GameProgress.getInstance();

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
        font.getData().setScale(1.8f);

        titleFont = new BitmapFont();
        titleFont.setColor(Color.GOLD);
        titleFont.getData().setScale(3.0f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        // Create UI elements
        float buttonWidth = 300;
        float buttonHeight = 60;
        float centerX = MastermindHDGame.GAME_WIDTH / 2f - buttonWidth / 2;
        float startY = 550;
        float spacing = 80;

        backButton = new Rectangle(20, MastermindHDGame.GAME_HEIGHT - 70, 100, 50);
        soundToggle = new Rectangle(centerX, startY, buttonWidth, buttonHeight);
        musicToggle = new Rectangle(centerX, startY - spacing, buttonWidth, buttonHeight);
        aboutButton = new Rectangle(centerX, startY - spacing * 2, buttonWidth, buttonHeight);
        feedbackButton = new Rectangle(centerX, startY - spacing * 3, buttonWidth, buttonHeight);
        resetButton = new Rectangle(centerX, startY - spacing * 4, buttonWidth, buttonHeight);
    }

    @Override
    public void show() {
        Gdx.app.log("SettingsScreen", "Settings shown");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.15f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        handleInput();

        // Draw background
        game.batch.begin();
        backgroundSprite.draw(game.batch);
        game.batch.end();

        // Draw buttons
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Back button
        shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);

        // Sound toggle
        shapeRenderer.setColor(soundManager.isSoundEnabled() ? 0.2f : 0.5f,
                               soundManager.isSoundEnabled() ? 0.7f : 0.3f,
                               0.2f, 1f);
        shapeRenderer.rect(soundToggle.x, soundToggle.y, soundToggle.width, soundToggle.height);

        // Music toggle
        shapeRenderer.setColor(soundManager.isMusicEnabled() ? 0.2f : 0.5f,
                               soundManager.isMusicEnabled() ? 0.7f : 0.3f,
                               0.2f, 1f);
        shapeRenderer.rect(musicToggle.x, musicToggle.y, musicToggle.width, musicToggle.height);

        // About button
        shapeRenderer.setColor(0.2f, 0.5f, 0.8f, 1f);
        shapeRenderer.rect(aboutButton.x, aboutButton.y, aboutButton.width, aboutButton.height);

        // Feedback button
        shapeRenderer.setColor(0.5f, 0.2f, 0.8f, 1f);
        shapeRenderer.rect(feedbackButton.x, feedbackButton.y, feedbackButton.width, feedbackButton.height);

        // Reset button (red)
        shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(resetButton.x, resetButton.y, resetButton.width, resetButton.height);

        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Title
        titleFont.getData().setScale(3.0f);
        layout.setText(titleFont, "SETTINGS");
        titleFont.draw(game.batch, "SETTINGS",
                (MastermindHDGame.GAME_WIDTH - layout.width) / 2,
                MastermindHDGame.GAME_HEIGHT - 30);

        // Back button
        font.getData().setScale(1.5f);
        layout.setText(font, "BACK");
        font.draw(game.batch, "BACK",
                backButton.x + (backButton.width - layout.width) / 2,
                backButton.y + (backButton.height + layout.height) / 2);

        // Button text
        font.getData().setScale(2.0f);
        String soundText = "Sound: " + (soundManager.isSoundEnabled() ? "ON" : "OFF");
        drawCenteredText(soundText, soundToggle);

        String musicText = "Music: " + (soundManager.isMusicEnabled() ? "ON" : "OFF");
        drawCenteredText(musicText, musicToggle);

        drawCenteredText("About / Credits", aboutButton);
        drawCenteredText("Send Feedback", feedbackButton);

        font.getData().setScale(1.8f);
        drawCenteredText("Reset All Progress", resetButton);

        // Stats
        font.getData().setScale(1.3f);
        font.setColor(Color.LIGHT_GRAY);
        String stats = String.format("Progress: %d/%d levels | %d/300 stars | %d%% complete",
                progress.getTotalLevelsCompleted(),
                100,
                progress.getTotalStars(),
                progress.getCompletionPercentage());
        layout.setText(font, stats);
        font.draw(game.batch, stats, (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 80);

        font.setColor(Color.WHITE);

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

            if (backButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new MainMenuScreen(game));
            } else if (soundToggle.contains(touchPoint.x, touchPoint.y)) {
                soundManager.setSoundEnabled(!soundManager.isSoundEnabled());
                soundManager.playTap();
            } else if (musicToggle.contains(touchPoint.x, touchPoint.y)) {
                soundManager.setMusicEnabled(!soundManager.isMusicEnabled());
                soundManager.playTap();
            } else if (aboutButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new AboutScreen(game));
            } else if (feedbackButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                // Open feedback (email or web form)
                openFeedback();
            } else if (resetButton.contains(touchPoint.x, touchPoint.y)) {
                // Confirm reset
                confirmReset();
            }
        }
    }

    private void openFeedback() {
        // In a real app, this would open an email client or web form
        String email = "feedback@mastermind-hd.example";
        String subject = "Mastermind HD Beta Feedback";
        String body = "App Version: 2.0 Beta\n\nFeedback:\n";

        Gdx.app.log("Feedback", "Would open email to: " + email);
        // Gdx.net.openURI("mailto:" + email + "?subject=" + subject + "&body=" + body);
    }

    private void confirmReset() {
        // In a real app, show confirmation dialog
        // For now, just log
        Gdx.app.log("Settings", "Reset requested - would show confirmation dialog");

        // Example reset (commented out for safety):
        // progress.resetAllProgress();
        // soundManager.playWhoosh();
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
