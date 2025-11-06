/*********************************************************
 * GAME TITLE: Mastermind HD - About Screen
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: About and credits screen
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
 * About and credits screen
 */
public class AboutScreen implements Screen {

    private final MastermindHDGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private BitmapFont font;
    private BitmapFont titleFont;
    private BitmapFont smallFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    private Rectangle backButton;
    private float scrollY = 0;

    public AboutScreen(MastermindHDGame game) {
        this.game = game;

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
        titleFont.getData().setScale(3.5f);

        smallFont = new BitmapFont();
        smallFont.setColor(Color.LIGHT_GRAY);
        smallFont.getData().setScale(1.3f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        backButton = new Rectangle(20, MastermindHDGame.GAME_HEIGHT - 70, 100, 50);
    }

    @Override
    public void show() {}

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

        // Draw back button
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Back button text
        font.getData().setScale(1.5f);
        layout.setText(font, "BACK");
        font.draw(game.batch, "BACK",
                backButton.x + (backButton.width - layout.width) / 2,
                backButton.y + (backButton.height + layout.height) / 2);

        // Title
        titleFont.getData().setScale(3.0f);
        layout.setText(titleFont, "ABOUT");
        titleFont.draw(game.batch, "ABOUT",
                (MastermindHDGame.GAME_WIDTH - layout.width) / 2,
                MastermindHDGame.GAME_HEIGHT - 30);

        // Content
        float y = 650;
        float centerX = MastermindHDGame.GAME_WIDTH / 2f;

        // Game name and version
        font.getData().setScale(2.2f);
        font.setColor(Color.GOLD);
        drawCentered("MASTERMIND HD", centerX, y);
        y -= 40;

        smallFont.getData().setScale(1.5f);
        smallFont.setColor(Color.YELLOW);
        drawCentered("Version 2.0 Beta", centerX, y);
        y -= 60;

        // Description
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        drawCentered("A complete puzzle game with", centerX, y);
        y -= 30;
        drawCentered("100 challenging levels", centerX, y);
        y -= 50;

        // Credits
        smallFont.getData().setScale(1.8f);
        smallFont.setColor(Color.GOLD);
        drawCentered("CREATED BY", centerX, y);
        y -= 35;

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);
        drawCentered("Dara Ouk", centerX, y);
        y -= 50;

        // Tech
        smallFont.getData().setScale(1.3f);
        smallFont.setColor(Color.LIGHT_GRAY);
        drawCentered("Built with libGDX", centerX, y);
        y -= 25;
        drawCentered("Java + OpenGL ES", centerX, y);
        y -= 50;

        // Stats
        font.getData().setScale(1.4f);
        font.setColor(Color.CYAN);
        drawCentered("100 Unique Levels", centerX, y);
        y -= 28;
        drawCentered("6 Difficulty Tiers", centerX, y);
        y -= 28;
        drawCentered("300 Stars to Collect", centerX, y);
        y -= 28;
        drawCentered("2,840 Lines of Code", centerX, y);
        y -= 50;

        // Beta notice
        smallFont.getData().setScale(1.5f);
        smallFont.setColor(Color.ORANGE);
        drawCentered("BETA VERSION", centerX, y);
        y -= 28;
        smallFont.getData().setScale(1.2f);
        smallFont.setColor(Color.LIGHT_GRAY);
        drawCentered("Thank you for testing!", centerX, y);
        y -= 40;

        // Copyright
        smallFont.getData().setScale(1.0f);
        smallFont.setColor(Color.DARK_GRAY);
        drawCentered("© 2025 Dara Ouk", centerX, y);
        y -= 20;
        drawCentered("Educational/Personal Project", centerX, y);

        game.batch.end();
    }

    private void drawCentered(String text, float centerX, float y) {
        layout.setText(font, text);
        float x = centerX - layout.width / 2;
        font.draw(game.batch, text, x, y);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            if (backButton.contains(touchPoint.x, touchPoint.y)) {
                SoundManager.getInstance().playButton();
                game.setScreen(new SettingsScreen(game));
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
        if (smallFont != null) smallFont.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
