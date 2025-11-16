/*********************************************************
 * GAME TITLE: Mastermind HD - Statistics Screen
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Display player statistics and achievements
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
 * Statistics screen showing player performance and achievements
 */
public class StatsScreen implements Screen {

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

    private Rectangle backButton;
    private Rectangle resetButton;
    private GameStats stats;
    private SoundManager soundManager;

    public StatsScreen(MastermindHDGame game) {
        this.game = game;
        this.stats = GameStats.getInstance();
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
        font.getData().setScale(1.8f);

        titleFont = new BitmapFont();
        titleFont.setColor(Color.GOLD);
        titleFont.getData().setScale(3.5f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        // Buttons
        backButton = new Rectangle(40, 50, 150, 60);
        resetButton = new Rectangle(290, 50, 150, 60);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw background
        game.batch.begin();
        backgroundSprite.draw(game.batch);
        game.batch.end();

        // Draw buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 0.6f, 0.3f, 1.0f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapeRenderer.setColor(0.8f, 0.3f, 0.3f, 1.0f);
        shapeRenderer.rect(resetButton.x, resetButton.y, resetButton.width, resetButton.height);
        shapeRenderer.end();

        // Draw text
        game.batch.begin();

        // Title
        layout.setText(titleFont, "STATISTICS");
        float titleX = (MastermindHDGame.GAME_WIDTH - layout.width) / 2;
        titleFont.draw(game.batch, "STATISTICS", titleX, 750);

        // Stats
        font.getData().setScale(1.8f);
        float leftX = 60;
        float y = 650;
        float lineHeight = 50;

        drawStat("Total Games:", String.valueOf(stats.getTotalGames()), leftX, y);
        y -= lineHeight;
        drawStat("Games Won:", String.valueOf(stats.getGamesWon()), leftX, y);
        y -= lineHeight;
        drawStat("Games Lost:", String.valueOf(stats.getGamesLost()), leftX, y);
        y -= lineHeight;
        drawStat("Win Rate:", stats.getWinRatePercentage() + "%", leftX, y);
        y -= lineHeight;
        drawStat("Avg Moves:", String.format("%.1f", stats.getAverageMoves()), leftX, y);
        y -= lineHeight;
        drawStat("Perfect Games:", String.valueOf(stats.getPerfectGames()), leftX, y);
        y -= lineHeight;
        drawStat("Current Streak:", String.valueOf(stats.getCurrentStreak()), leftX, y);
        y -= lineHeight;
        drawStat("Best Streak:", String.valueOf(stats.getBestStreak()), leftX, y);
        y -= lineHeight;

        float bestTime = stats.getBestTime();
        String bestTimeStr = bestTime > 0 ? String.format("%.1fs", bestTime) : "N/A";
        drawStat("Best Time:", bestTimeStr, leftX, y);

        // Achievements
        font.getData().setScale(2.2f);
        font.setColor(Color.CYAN);
        font.draw(game.batch, "ACHIEVEMENTS", leftX, 280);

        font.getData().setScale(1.5f);
        y = 230;
        lineHeight = 35;

        drawAchievement("First Victory", stats.hasPlayedFirstGame(), leftX, y);
        y -= lineHeight;
        drawAchievement("10 Wins", stats.hasWon10Games(), leftX, y);
        y -= lineHeight;
        drawAchievement("50 Wins", stats.hasWon50Games(), leftX, y);
        y -= lineHeight;
        drawAchievement("100 Wins", stats.hasWon100Games(), leftX, y);
        y -= lineHeight;
        drawAchievement("Perfect Game", stats.hasPerfectGame(), leftX, y);
        y -= lineHeight;
        drawAchievement("5 Win Streak", stats.has5WinStreak(), leftX, y);

        // Button text
        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);
        layout.setText(font, "BACK");
        font.draw(game.batch, "BACK",
                backButton.x + (backButton.width - layout.width) / 2,
                backButton.y + (backButton.height + layout.height) / 2);

        layout.setText(font, "RESET");
        font.draw(game.batch, "RESET",
                resetButton.x + (resetButton.width - layout.width) / 2,
                resetButton.y + (resetButton.height + layout.height) / 2);

        game.batch.end();
    }

    private void drawStat(String label, String value, float x, float y) {
        font.setColor(Color.LIGHT_GRAY);
        font.draw(game.batch, label, x, y);
        font.setColor(Color.WHITE);
        font.draw(game.batch, value, x + 220, y);
    }

    private void drawAchievement(String name, boolean unlocked, float x, float y) {
        if (unlocked) {
            font.setColor(Color.GOLD);
            font.draw(game.batch, "✓ " + name, x, y);
        } else {
            font.setColor(Color.DARK_GRAY);
            font.draw(game.batch, "✗ " + name, x, y);
        }
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            if (backButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                game.setScreen(new SettingsScreen(game));
            } else if (resetButton.contains(touchPoint.x, touchPoint.y)) {
                soundManager.playButton();
                stats.reset();
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
