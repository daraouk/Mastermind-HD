/*********************************************************
 * GAME TITLE: Mastermind HD - Lose Screen
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Game over screen with retry option
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
 * Lose screen with retry and menu options
 */
public class LoseScreen implements Screen {

    private final MastermindHDGame game;
    private final Level level;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    private Texture backgroundTexture;
    private Texture gameoverTexture;
    private Sprite backgroundSprite;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    private Rectangle retryButton;
    private Rectangle menuButton;
    private SoundManager soundManager;

    public LoseScreen(MastermindHDGame game, Level level) {
        this.game = game;
        this.level = level;
        this.soundManager = SoundManager.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT, camera);
        camera.position.set(MastermindHDGame.GAME_WIDTH / 2f, MastermindHDGame.GAME_HEIGHT / 2f, 0);
        touchPoint = new Vector3();

        backgroundTexture = new Texture(Gdx.files.internal("gfx/wood_bg.jpg"));
        gameoverTexture = new Texture(Gdx.files.internal("gfx/gameover_lose.png"));

        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setPosition(0, 0);
        backgroundSprite.setSize(MastermindHDGame.GAME_WIDTH, MastermindHDGame.GAME_HEIGHT);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);

        titleFont = new BitmapFont();
        titleFont.setColor(Color.RED);
        titleFont.getData().setScale(3.5f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        // Buttons
        float buttonWidth = 250;
        float buttonHeight = 70;
        float centerX = MastermindHDGame.GAME_WIDTH / 2f - buttonWidth / 2;

        retryButton = new Rectangle(centerX, 200, buttonWidth, buttonHeight);
        menuButton = new Rectangle(centerX, 110, buttonWidth, buttonHeight);
    }

    @Override
    public void show() {
        Gdx.app.log("LoseScreen", "Level " + level.getLevelNumber() + " failed");
        soundManager.playLose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        handleInput();

        // Draw background (dimmed)
        game.batch.begin();
        backgroundSprite.setColor(0.4f, 0.3f, 0.3f, 1f);
        backgroundSprite.draw(game.batch);
        backgroundSprite.setColor(1, 1, 1, 1);

        // Draw game over graphic
        float gameoverX = MastermindHDGame.GAME_WIDTH / 2f - 256;
        float gameoverY = 400;
        game.batch.draw(gameoverTexture, gameoverX, gameoverY);

        game.batch.end();

        // Draw buttons
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.7f, 0.5f, 0.2f, 1f);
        shapeRenderer.rect(retryButton.x, retryButton.y, retryButton.width, retryButton.height);

        shapeRenderer.setColor(0.2f, 0.2f, 0.7f, 1f);
        shapeRenderer.rect(menuButton.x, menuButton.y, menuButton.width, menuButton.height);

        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Level name
        font.getData().setScale(1.5f);
        font.setColor(Color.LIGHT_GRAY);
        layout.setText(font, level.getName());
        font.draw(game.batch, level.getName(), (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 360);

        // Encouragement
        font.setColor(Color.WHITE);
        String msg = "Try again! You can do it!";
        layout.setText(font, msg);
        font.draw(game.batch, msg, (MastermindHDGame.GAME_WIDTH - layout.width) / 2, 320);

        // Button text
        font.getData().setScale(2.0f);
        drawCenteredText("Retry", retryButton);
        drawCenteredText("Menu", menuButton);

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

            if (retryButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                // Retry current level
                game.setScreen(new EnhancedGameScreen(game, level));
            } else if (menuButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                // Back to level select
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
        if (gameoverTexture != null) gameoverTexture.dispose();
        if (font != null) font.dispose();
        if (titleFont != null) titleFont.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
