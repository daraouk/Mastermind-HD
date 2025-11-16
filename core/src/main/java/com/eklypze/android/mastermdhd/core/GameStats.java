/*********************************************************
 * GAME TITLE: Mastermind HD - Game Statistics
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Tracks player statistics and achievements
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Singleton class for tracking player statistics
 */
public class GameStats {
    private static GameStats instance;
    private final Preferences prefs;

    // Keys for preferences
    private static final String KEY_TOTAL_GAMES = "stats_total_games";
    private static final String KEY_GAMES_WON = "stats_games_won";
    private static final String KEY_GAMES_LOST = "stats_games_lost";
    private static final String KEY_TOTAL_MOVES = "stats_total_moves";
    private static final String KEY_TOTAL_TIME = "stats_total_time";
    private static final String KEY_PERFECT_GAMES = "stats_perfect_games";
    private static final String KEY_HINTS_USED = "stats_hints_used";
    private static final String KEY_BEST_TIME = "stats_best_time";
    private static final String KEY_CURRENT_STREAK = "stats_current_streak";
    private static final String KEY_BEST_STREAK = "stats_best_streak";

    private GameStats() {
        prefs = Gdx.app.getPreferences("MastermindHD_Stats");
    }

    public static GameStats getInstance() {
        if (instance == null) {
            instance = new GameStats();
        }
        return instance;
    }

    /**
     * Record a completed game
     */
    public void recordGame(boolean won, int moves, float timeSeconds, int hintsUsed, boolean isPerfect) {
        int totalGames = prefs.getInteger(KEY_TOTAL_GAMES, 0);
        int gamesWon = prefs.getInteger(KEY_GAMES_WON, 0);
        int gamesLost = prefs.getInteger(KEY_GAMES_LOST, 0);
        int totalMoves = prefs.getInteger(KEY_TOTAL_MOVES, 0);
        float totalTime = prefs.getFloat(KEY_TOTAL_TIME, 0);
        int perfectGames = prefs.getInteger(KEY_PERFECT_GAMES, 0);
        int totalHints = prefs.getInteger(KEY_HINTS_USED, 0);
        float bestTime = prefs.getFloat(KEY_BEST_TIME, Float.MAX_VALUE);
        int currentStreak = prefs.getInteger(KEY_CURRENT_STREAK, 0);
        int bestStreak = prefs.getInteger(KEY_BEST_STREAK, 0);

        // Update totals
        prefs.putInteger(KEY_TOTAL_GAMES, totalGames + 1);
        prefs.putInteger(KEY_TOTAL_MOVES, totalMoves + moves);
        prefs.putFloat(KEY_TOTAL_TIME, totalTime + timeSeconds);
        prefs.putInteger(KEY_HINTS_USED, totalHints + hintsUsed);

        if (won) {
            prefs.putInteger(KEY_GAMES_WON, gamesWon + 1);

            // Update streak
            currentStreak++;
            prefs.putInteger(KEY_CURRENT_STREAK, currentStreak);
            if (currentStreak > bestStreak) {
                prefs.putInteger(KEY_BEST_STREAK, currentStreak);
            }

            // Perfect game (no hints, minimal moves)
            if (isPerfect) {
                prefs.putInteger(KEY_PERFECT_GAMES, perfectGames + 1);
            }

            // Best time
            if (timeSeconds > 0 && timeSeconds < bestTime) {
                prefs.putFloat(KEY_BEST_TIME, timeSeconds);
            }
        } else {
            prefs.putInteger(KEY_GAMES_LOST, gamesLost + 1);
            prefs.putInteger(KEY_CURRENT_STREAK, 0); // Reset streak on loss
        }

        prefs.flush();
    }

    // Getters
    public int getTotalGames() { return prefs.getInteger(KEY_TOTAL_GAMES, 0); }
    public int getGamesWon() { return prefs.getInteger(KEY_GAMES_WON, 0); }
    public int getGamesLost() { return prefs.getInteger(KEY_GAMES_LOST, 0); }
    public int getTotalMoves() { return prefs.getInteger(KEY_TOTAL_MOVES, 0); }
    public float getTotalTime() { return prefs.getFloat(KEY_TOTAL_TIME, 0); }
    public int getPerfectGames() { return prefs.getInteger(KEY_PERFECT_GAMES, 0); }
    public int getHintsUsed() { return prefs.getInteger(KEY_HINTS_USED, 0); }
    public float getBestTime() {
        float time = prefs.getFloat(KEY_BEST_TIME, Float.MAX_VALUE);
        return time == Float.MAX_VALUE ? 0 : time;
    }
    public int getCurrentStreak() { return prefs.getInteger(KEY_CURRENT_STREAK, 0); }
    public int getBestStreak() { return prefs.getInteger(KEY_BEST_STREAK, 0); }

    /**
     * Calculate win rate percentage
     */
    public int getWinRatePercentage() {
        int total = getTotalGames();
        if (total == 0) return 0;
        return (int) ((getGamesWon() / (float) total) * 100);
    }

    /**
     * Calculate average moves per game
     */
    public float getAverageMoves() {
        int total = getTotalGames();
        if (total == 0) return 0;
        return getTotalMoves() / (float) total;
    }

    /**
     * Calculate average time per game
     */
    public float getAverageTime() {
        int total = getTotalGames();
        if (total == 0) return 0;
        return getTotalTime() / (float) total;
    }

    /**
     * Reset all statistics
     */
    public void reset() {
        prefs.clear();
        prefs.flush();
        Gdx.app.log("GameStats", "Statistics reset");
    }

    /**
     * Get formatted stats summary
     */
    public String getSummary() {
        return String.format(
            "Games: %d | W: %d | L: %d | Win%%: %d%% | Avg Moves: %.1f | Streak: %d",
            getTotalGames(), getGamesWon(), getGamesLost(), getWinRatePercentage(),
            getAverageMoves(), getCurrentStreak()
        );
    }

    /**
     * Check if player has achievement unlocks
     */
    public boolean hasPlayedFirstGame() { return getTotalGames() >= 1; }
    public boolean hasWon10Games() { return getGamesWon() >= 10; }
    public boolean hasWon50Games() { return getGamesWon() >= 50; }
    public boolean hasWon100Games() { return getGamesWon() >= 100; }
    public boolean hasPerfectGame() { return getPerfectGames() >= 1; }
    public boolean has5WinStreak() { return getBestStreak() >= 5; }
    public boolean has10WinStreak() { return getBestStreak() >= 10; }
    public boolean hasSpeedDemon() { return getBestTime() > 0 && getBestTime() < 60; } // Under 1 minute
}
