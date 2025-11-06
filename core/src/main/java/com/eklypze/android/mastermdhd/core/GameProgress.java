/*********************************************************
 * GAME TITLE: Mastermind HD - Game Progress
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Track player progress through levels
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Manages player progress, level unlocking, and star ratings
 */
public class GameProgress {

    private static GameProgress instance;
    private final Preferences prefs;

    private static final String PREF_NAME = "MastermindHD_Progress";
    private static final String KEY_HIGHEST_UNLOCKED = "highest_unlocked";
    private static final String KEY_LEVEL_STARS = "level_%d_stars";
    private static final String KEY_LEVEL_COMPLETED = "level_%d_completed";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_MUSIC_ENABLED = "music_enabled";

    private GameProgress() {
        prefs = Gdx.app.getPreferences(PREF_NAME);

        // First time initialization - unlock level 1
        if (!prefs.contains(KEY_HIGHEST_UNLOCKED)) {
            prefs.putInteger(KEY_HIGHEST_UNLOCKED, 1);
            prefs.putBoolean(KEY_SOUND_ENABLED, true);
            prefs.putBoolean(KEY_MUSIC_ENABLED, true);
            prefs.flush();
        }
    }

    public static GameProgress getInstance() {
        if (instance == null) {
            instance = new GameProgress();
        }
        return instance;
    }

    /**
     * Get the highest unlocked level number
     */
    public int getHighestUnlockedLevel() {
        return prefs.getInteger(KEY_HIGHEST_UNLOCKED, 1);
    }

    /**
     * Check if a level is unlocked
     */
    public boolean isLevelUnlocked(int levelNumber) {
        return levelNumber <= getHighestUnlockedLevel();
    }

    /**
     * Unlock a level
     */
    public void unlockLevel(int levelNumber) {
        int currentHighest = getHighestUnlockedLevel();
        if (levelNumber > currentHighest) {
            prefs.putInteger(KEY_HIGHEST_UNLOCKED, levelNumber);
            prefs.flush();
        }
    }

    /**
     * Complete a level with a star rating
     * Automatically unlocks the next level
     */
    public void completeLevel(int levelNumber, int stars) {
        if (stars < 1 || stars > 3) {
            throw new IllegalArgumentException("Stars must be between 1 and 3");
        }

        String starsKey = String.format(KEY_LEVEL_STARS, levelNumber);
        String completedKey = String.format(KEY_LEVEL_COMPLETED, levelNumber);

        // Only update stars if it's better than previous
        int currentStars = prefs.getInteger(starsKey, 0);
        if (stars > currentStars) {
            prefs.putInteger(starsKey, stars);
        }

        prefs.putBoolean(completedKey, true);

        // Unlock next level (if not at max)
        if (levelNumber < 100) {
            unlockLevel(levelNumber + 1);
        }

        prefs.flush();
    }

    /**
     * Get stars earned for a level (0 if not completed)
     */
    public int getLevelStars(int levelNumber) {
        String key = String.format(KEY_LEVEL_STARS, levelNumber);
        return prefs.getInteger(key, 0);
    }

    /**
     * Check if a level has been completed
     */
    public boolean isLevelCompleted(int levelNumber) {
        String key = String.format(KEY_LEVEL_COMPLETED, levelNumber);
        return prefs.getBoolean(key, false);
    }

    /**
     * Get total stars earned across all levels
     */
    public int getTotalStars() {
        int total = 0;
        for (int i = 1; i <= 100; i++) {
            total += getLevelStars(i);
        }
        return total;
    }

    /**
     * Get total levels completed
     */
    public int getTotalLevelsCompleted() {
        int total = 0;
        for (int i = 1; i <= 100; i++) {
            if (isLevelCompleted(i)) {
                total++;
            }
        }
        return total;
    }

    /**
     * Reset all progress (for settings menu)
     */
    public void resetAllProgress() {
        // Keep sound/music settings
        boolean sound = isSoundEnabled();
        boolean music = isMusicEnabled();

        prefs.clear();
        prefs.putInteger(KEY_HIGHEST_UNLOCKED, 1);
        prefs.putBoolean(KEY_SOUND_ENABLED, sound);
        prefs.putBoolean(KEY_MUSIC_ENABLED, music);
        prefs.flush();
    }

    // Sound and music settings

    public boolean isSoundEnabled() {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.putBoolean(KEY_SOUND_ENABLED, enabled);
        prefs.flush();
    }

    public boolean isMusicEnabled() {
        return prefs.getBoolean(KEY_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean enabled) {
        prefs.putBoolean(KEY_MUSIC_ENABLED, enabled);
        prefs.flush();
    }

    /**
     * Get completion percentage (0-100)
     */
    public int getCompletionPercentage() {
        return (getTotalLevelsCompleted() * 100) / 100;
    }

    /**
     * Check if player has achieved 3 stars on all levels
     */
    public boolean isPerfectCompletion() {
        return getTotalStars() == 300; // 100 levels × 3 stars
    }
}
