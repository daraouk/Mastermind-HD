/*********************************************************
 * GAME TITLE: Mastermind HD - Level Configuration
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Level configuration system for 100 levels
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

/**
 * Represents a single level configuration
 */
public class Level {

    public enum Difficulty {
        TUTORIAL,   // Very easy, guided
        EASY,       // 4 colors, no duplicates
        MEDIUM,     // 6 colors, no duplicates or 4 colors with duplicates
        HARD,       // 8 colors, duplicates allowed
        EXPERT,     // 8+ colors, longer codes
        MASTER      // Ultimate challenge
    }

    private final int levelNumber;
    private final String name;
    private final Difficulty difficulty;
    private final int numColors;           // Number of colors available (4-8)
    private final int codeLength;          // Length of secret code (3-6)
    private final int maxTurns;            // Maximum attempts allowed
    private final boolean allowDuplicates; // Can code have duplicate colors?
    private final int hintsAvailable;      // Number of hints player can use
    private final boolean timed;           // Is this a timed challenge?
    private final int timeLimit;           // Time limit in seconds (0 = unlimited)

    // Star rating thresholds (moves needed for 3/2/1 stars)
    private final int threeStarMoves;
    private final int twoStarMoves;

    private Level(Builder builder) {
        this.levelNumber = builder.levelNumber;
        this.name = builder.name;
        this.difficulty = builder.difficulty;
        this.numColors = builder.numColors;
        this.codeLength = builder.codeLength;
        this.maxTurns = builder.maxTurns;
        this.allowDuplicates = builder.allowDuplicates;
        this.hintsAvailable = builder.hintsAvailable;
        this.timed = builder.timed;
        this.timeLimit = builder.timeLimit;
        this.threeStarMoves = builder.threeStarMoves;
        this.twoStarMoves = builder.twoStarMoves;
    }

    // Getters
    public int getLevelNumber() { return levelNumber; }
    public String getName() { return name; }
    public Difficulty getDifficulty() { return difficulty; }
    public int getNumColors() { return numColors; }
    public int getCodeLength() { return codeLength; }
    public int getMaxTurns() { return maxTurns; }
    public boolean allowsDuplicates() { return allowDuplicates; }
    public int getHintsAvailable() { return hintsAvailable; }
    public boolean isTimed() { return timed; }
    public int getTimeLimit() { return timeLimit; }
    public int getThreeStarMoves() { return threeStarMoves; }
    public int getTwoStarMoves() { return twoStarMoves; }

    /**
     * Calculate star rating based on number of moves used
     */
    public int getStarRating(int movesUsed) {
        if (movesUsed <= threeStarMoves) return 3;
        if (movesUsed <= twoStarMoves) return 2;
        return 1;
    }

    /**
     * Builder pattern for creating levels
     */
    public static class Builder {
        private int levelNumber;
        private String name = "Level";
        private Difficulty difficulty = Difficulty.EASY;
        private int numColors = 4;
        private int codeLength = 4;
        private int maxTurns = 10;
        private boolean allowDuplicates = false;
        private int hintsAvailable = 3;
        private boolean timed = false;
        private int timeLimit = 0;
        private int threeStarMoves = 5;
        private int twoStarMoves = 7;

        public Builder(int levelNumber) {
            this.levelNumber = levelNumber;
            this.name = "Level " + levelNumber;
        }

        public Builder name(String name) { this.name = name; return this; }
        public Builder difficulty(Difficulty difficulty) { this.difficulty = difficulty; return this; }
        public Builder numColors(int numColors) { this.numColors = numColors; return this; }
        public Builder codeLength(int codeLength) { this.codeLength = codeLength; return this; }
        public Builder maxTurns(int maxTurns) { this.maxTurns = maxTurns; return this; }
        public Builder allowDuplicates(boolean allow) { this.allowDuplicates = allow; return this; }
        public Builder hints(int hints) { this.hintsAvailable = hints; return this; }
        public Builder timed(boolean timed) { this.timed = timed; return this; }
        public Builder timeLimit(int seconds) { this.timeLimit = seconds; return this; }
        public Builder threeStars(int moves) { this.threeStarMoves = moves; return this; }
        public Builder twoStars(int moves) { this.twoStarMoves = moves; return this; }

        public Level build() {
            return new Level(this);
        }
    }
}
