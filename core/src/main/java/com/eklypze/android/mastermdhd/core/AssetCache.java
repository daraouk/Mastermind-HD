/*********************************************************
 * GAME TITLE: Mastermind HD - Asset Cache
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Singleton for efficient asset loading and caching
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages texture loading and caching for better performance
 */
public class AssetCache {
    private static AssetCache instance;
    private final Map<String, Texture> textureCache;
    private boolean initialized = false;

    private AssetCache() {
        textureCache = new HashMap<>();
    }

    public static AssetCache getInstance() {
        if (instance == null) {
            instance = new AssetCache();
        }
        return instance;
    }

    /**
     * Preload all game assets on startup
     */
    public void initialize() {
        if (initialized) return;

        try {
            // Load background
            loadTexture("gfx/wood_bg.jpg");

            // Load all ball colors
            loadTexture("gfx/red_ball.png");
            loadTexture("gfx/blue_ball.png");
            loadTexture("gfx/green_ball.png");
            loadTexture("gfx/purple_ball.png");
            loadTexture("gfx/yellow_ball.png");
            loadTexture("gfx/orange_ball.png");
            loadTexture("gfx/black_ball.png");
            loadTexture("gfx/white_ball.png");

            // Load all peg textures
            loadTexture("gfx/peg_1B.png");
            loadTexture("gfx/peg_1B2W.png");
            loadTexture("gfx/peg_1B3W.png");
            loadTexture("gfx/peg_1W.png");
            loadTexture("gfx/peg_2B.png");
            loadTexture("gfx/peg_2B2W.png");
            loadTexture("gfx/peg_2W.png");
            loadTexture("gfx/peg_3B.png");
            loadTexture("gfx/peg_3B1W.png");
            loadTexture("gfx/peg_3W.png");
            loadTexture("gfx/peg_4B.png");
            loadTexture("gfx/peg_4W.png");

            // Load game over texture
            loadTexture("gfx/gameover_lose.png");

            initialized = true;
            Gdx.app.log("AssetCache", "Initialized successfully - " + textureCache.size() + " textures loaded");
        } catch (Exception e) {
            Gdx.app.error("AssetCache", "Failed to initialize assets", e);
        }
    }

    /**
     * Load a texture into cache
     */
    private void loadTexture(String path) {
        if (!textureCache.containsKey(path)) {
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                textureCache.put(path, texture);
            } catch (Exception e) {
                Gdx.app.error("AssetCache", "Failed to load texture: " + path, e);
            }
        }
    }

    /**
     * Get a cached texture
     */
    public Texture getTexture(String path) {
        if (!textureCache.containsKey(path)) {
            loadTexture(path);
        }
        return textureCache.get(path);
    }

    /**
     * Check if a texture exists in cache
     */
    public boolean hasTexture(String path) {
        return textureCache.containsKey(path);
    }

    /**
     * Get loading progress (0.0 to 1.0)
     */
    public float getLoadingProgress() {
        if (!initialized) return 0.5f;
        return 1.0f;
    }

    /**
     * Dispose all cached textures
     */
    public void dispose() {
        for (Texture texture : textureCache.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        textureCache.clear();
        initialized = false;
        Gdx.app.log("AssetCache", "Disposed");
    }

    /**
     * Get cache statistics
     */
    public String getStats() {
        return String.format("Cached textures: %d | Initialized: %b",
                           textureCache.size(), initialized);
    }
}
