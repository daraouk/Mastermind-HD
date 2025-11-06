/*********************************************************
 * GAME TITLE: Mastermind HD - Level Selection
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Grid of 100 levels to choose from
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
 * Level selection screen with scrollable grid
 */
public class LevelSelectScreen implements Screen {

    private final MastermindHDGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPoint;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private BitmapFont font;
    private BitmapFont smallFont;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    private LevelManager levelManager;
    private GameProgress progress;

    // Grid configuration
    private static final int COLUMNS = 5;
    private static final int ROWS = 20;  // 100 levels / 5 columns
    private static final float LEVEL_SIZE = 70;
    private static final float LEVEL_SPACING = 10;
    private static final float START_X = 40;
    private static final float START_Y_OFFSET = 100;  // From top

    private Rectangle[] levelButtons;
    private Rectangle backButton;

    private float scrollY = 0;
    private float maxScroll;
    private float lastTouchY = 0;
    private boolean isDragging = false;

    public LevelSelectScreen(MastermindHDGame game) {
        this.game = game;
        this.levelManager = LevelManager.getInstance();
        this.progress = GameProgress.getInstance();

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
        font.getData().setScale(1.5f);

        smallFont = new BitmapFont();
        smallFont.setColor(Color.YELLOW);
        smallFont.getData().setScale(1.0f);

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        // Create level buttons
        levelButtons = new Rectangle[100];
        for (int i = 0; i < 100; i++) {
            int row = i / COLUMNS;
            int col = i % COLUMNS;

            float x = START_X + col * (LEVEL_SIZE + LEVEL_SPACING);
            float y = MastermindHDGame.GAME_HEIGHT - START_Y_OFFSET - row * (LEVEL_SIZE + LEVEL_SPACING);

            levelButtons[i] = new Rectangle(x, y, LEVEL_SIZE, LEVEL_SIZE);
        }

        // Calculate max scroll
        maxScroll = Math.max(0, ROWS * (LEVEL_SIZE + LEVEL_SPACING) - (MastermindHDGame.GAME_HEIGHT - 200));

        // Back button
        backButton = new Rectangle(20, MastermindHDGame.GAME_HEIGHT - 70, 100, 50);
    }

    @Override
    public void show() {
        Gdx.app.log("LevelSelect", "Level selection shown");
    }

    @Override
    public void render(float delta) {
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

        // Draw title and back button
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapeRenderer.end();

        game.batch.begin();
        layout.setText(font, "BACK");
        font.draw(game.batch, "BACK", backButton.x + (backButton.width - layout.width) / 2,
                backButton.y + (backButton.height + layout.height) / 2);

        // Title
        font.getData().setScale(2.5f);
        layout.setText(font, "SELECT LEVEL");
        font.draw(game.batch, "SELECT LEVEL", (MastermindHDGame.GAME_WIDTH - layout.width) / 2,
                MastermindHDGame.GAME_HEIGHT - 20);
        font.getData().setScale(1.5f);
        game.batch.end();

        // Draw level buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < 100; i++) {
            Rectangle btn = levelButtons[i];
            float adjustedY = btn.y + scrollY;

            // Only draw if visible
            if (adjustedY + LEVEL_SIZE < 50 || adjustedY > MastermindHDGame.GAME_HEIGHT) {
                continue;
            }

            Level level = levelManager.getLevel(i + 1);
            boolean unlocked = progress.isLevelUnlocked(i + 1);
            boolean completed = progress.isLevelCompleted(i + 1);
            int stars = progress.getLevelStars(i + 1);

            // Choose color based on difficulty and status
            if (!unlocked) {
                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);  // Locked - gray
            } else if (completed) {
                // Completed - different colors for star ratings
                if (stars == 3) {
                    shapeRenderer.setColor(1f, 0.84f, 0f, 1f);  // Gold - 3 stars
                } else if (stars == 2) {
                    shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 1f);  // Silver - 2 stars
                } else {
                    shapeRenderer.setColor(0.8f, 0.5f, 0.2f, 1f);  // Bronze - 1 star
                }
            } else {
                // Unlocked but not completed - color by difficulty
                switch (level.getDifficulty()) {
                    case TUTORIAL:
                        shapeRenderer.setColor(0.5f, 0.8f, 0.5f, 1f);  // Light green
                        break;
                    case EASY:
                        shapeRenderer.setColor(0.2f, 0.7f, 0.2f, 1f);  // Green
                        break;
                    case MEDIUM:
                        shapeRenderer.setColor(0.2f, 0.5f, 0.8f, 1f);  // Blue
                        break;
                    case HARD:
                        shapeRenderer.setColor(0.8f, 0.5f, 0.2f, 1f);  // Orange
                        break;
                    case EXPERT:
                        shapeRenderer.setColor(0.7f, 0.2f, 0.7f, 1f);  // Purple
                        break;
                    case MASTER:
                        shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1f);  // Red
                        break;
                }
            }

            shapeRenderer.rect(btn.x, adjustedY, btn.width, btn.height);
        }

        shapeRenderer.end();

        // Draw level numbers and stars
        game.batch.begin();
        for (int i = 0; i < 100; i++) {
            Rectangle btn = levelButtons[i];
            float adjustedY = btn.y + scrollY;

            // Only draw if visible
            if (adjustedY + LEVEL_SIZE < 50 || adjustedY > MastermindHDGame.GAME_HEIGHT) {
                continue;
            }

            boolean unlocked = progress.isLevelUnlocked(i + 1);
            int stars = progress.getLevelStars(i + 1);

            // Level number
            font.getData().setScale(2.0f);
            String levelText = unlocked ? String.valueOf(i + 1) : "🔒";
            layout.setText(font, levelText);
            font.draw(game.batch, levelText,
                    btn.x + (btn.width - layout.width) / 2,
                    adjustedY + btn.height / 2 + 15);

            // Stars
            if (stars > 0) {
                smallFont.getData().setScale(1.2f);
                String starText = "";
                for (int s = 0; s < stars; s++) {
                    starText += "⭐";
                }
                layout.setText(smallFont, starText);
                smallFont.draw(game.batch, starText,
                        btn.x + (btn.width - layout.width) / 2,
                        adjustedY + 15);
            }
        }
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);
            lastTouchY = touchPoint.y;
            isDragging = true;

            // Check back button
            if (backButton.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new MainMenuScreen(game));
                return;
            }

            // Check level buttons
            for (int i = 0; i < 100; i++) {
                Rectangle btn = levelButtons[i];
                float adjustedY = btn.y + scrollY;

                if (touchPoint.x >= btn.x && touchPoint.x <= btn.x + btn.width &&
                        touchPoint.y >= adjustedY && touchPoint.y <= adjustedY + btn.height) {

                    if (progress.isLevelUnlocked(i + 1)) {
                        // Start the level
                        Level level = levelManager.getLevel(i + 1);
                        game.setScreen(new EnhancedGameScreen(game, level));
                        return;
                    }
                }
            }
        }

        // Handle scrolling
        if (Gdx.input.isTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            if (isDragging) {
                float deltaY = touchPoint.y - lastTouchY;
                scrollY += deltaY;

                // Clamp scroll
                scrollY = Math.max(-maxScroll, Math.min(0, scrollY));

                lastTouchY = touchPoint.y;
            }
        } else {
            isDragging = false;
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
        if (smallFont != null) smallFont.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
