/*********************************************************
 * GAME TITLE: Mastermind HD - Core Game Logic
 * AUTHOR: Dara Ouk (Modernized)
 * VERSION: 2.0
 * DESCRIPTION: Framework-independent Mastermind game logic
 *     Enhanced to support multiple level configurations
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import java.util.Arrays;
import java.util.Random;

/**
 * Core Mastermind game logic.
 * Handles code generation, move validation, scoring, and game state.
 * Now supports configurable levels with different parameters.
 */
public class MastermindGame {

    // Default color indices (all 8 colors)
    public static final int RED = 0;
    public static final int BLUE = 1;
    public static final int GREEN = 2;
    public static final int PURPLE = 3;
    public static final int YELLOW = 4;
    public static final int ORANGE = 5;
    public static final int BLACK = 6;
    public static final int WHITE = 7;

    // Game Configuration (from level)
    private final Level level;
    private final int numColors;
    private final int codeLength;
    private final int maxTurns;
    private final boolean allowDuplicates;
    private int hintsRemaining;

    // Game State
    private int[] secretCode;
    private int currentTurn;
    private boolean gameOver;
    private boolean playerWon;
    private int[][] guessHistory;
    private Feedback[] feedbackHistory;
    private float elapsedTime;  // For timed levels

    /**
     * Represents feedback for a guess (black and white pegs)
     */
    public static class Feedback {
        public final int blackPegs;  // Correct color and position
        public final int whitePegs;  // Correct color, wrong position

        public Feedback(int blackPegs, int whitePegs) {
            this.blackPegs = blackPegs;
            this.whitePegs = whitePegs;
        }

        public boolean isWin(int codeLength) {
            return blackPegs == codeLength;
        }

        @Override
        public String toString() {
            return "Feedback{blackPegs=" + blackPegs + ", whitePegs=" + whitePegs + "}";
        }
    }

    /**
     * Creates a new game based on a level configuration
     */
    public MastermindGame(Level level) {
        this.level = level;
        this.numColors = level.getNumColors();
        this.codeLength = level.getCodeLength();
        this.maxTurns = level.getMaxTurns();
        this.allowDuplicates = level.allowsDuplicates();
        this.hintsRemaining = level.getHintsAvailable();

        this.guessHistory = new int[maxTurns][codeLength];
        this.feedbackHistory = new Feedback[maxTurns];

        startNewGame();
    }

    /**
     * Legacy constructor for backwards compatibility
     */
    public MastermindGame(boolean allowDuplicates) {
        // Create a default level
        this.level = new Level.Builder(0)
                .numColors(8)
                .codeLength(4)
                .maxTurns(10)
                .allowDuplicates(allowDuplicates)
                .hints(3)
                .build();

        this.numColors = 8;
        this.codeLength = 4;
        this.maxTurns = 10;
        this.allowDuplicates = allowDuplicates;
        this.hintsRemaining = 3;

        this.guessHistory = new int[maxTurns][codeLength];
        this.feedbackHistory = new Feedback[maxTurns];

        startNewGame();
    }

    /**
     * Legacy constructor (no duplicates)
     */
    public MastermindGame() {
        this(false);
    }

    /**
     * Starts a new game by generating a random secret code
     */
    public void startNewGame() {
        this.secretCode = generateSecretCode();
        this.currentTurn = 0;
        this.gameOver = false;
        this.playerWon = false;
        this.elapsedTime = 0;
        this.hintsRemaining = level != null ? level.getHintsAvailable() : 3;

        // Clear history
        for (int i = 0; i < maxTurns; i++) {
            Arrays.fill(guessHistory[i], -1);
            feedbackHistory[i] = null;
        }
    }

    /**
     * Generates a random secret code based on current settings
     */
    private int[] generateSecretCode() {
        int[] code = new int[codeLength];
        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int color = random.nextInt(numColors);

            // If duplicates not allowed, ensure uniqueness
            if (!allowDuplicates) {
                boolean isDuplicate = false;
                for (int j = 0; j < i; j++) {
                    if (code[j] == color) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    i--;
                    continue;
                }
            }

            code[i] = color;
        }

        return code;
    }

    /**
     * Update elapsed time (for timed levels)
     * @param delta time in seconds since last update
     * @return true if time limit exceeded
     */
    public boolean updateTime(float delta) {
        if (level != null && level.isTimed() && !gameOver) {
            elapsedTime += delta;
            if (elapsedTime >= level.getTimeLimit()) {
                gameOver = true;
                playerWon = false;
                return true;
            }
        }
        return false;
    }

    /**
     * Get remaining time in seconds (for timed levels)
     */
    public float getRemainingTime() {
        if (level != null && level.isTimed()) {
            return Math.max(0, level.getTimeLimit() - elapsedTime);
        }
        return 0;
    }

    /**
     * Use a hint - reveals one correct position
     * @return color index at a random unrevealed position, or -1 if no hints available
     */
    public int useHint() {
        if (hintsRemaining <= 0 || gameOver) {
            return -1;
        }

        hintsRemaining--;

        // Find positions not yet correctly guessed in current row
        int[] currentGuess = guessHistory[currentTurn];
        Random random = new Random();

        // Try to find a position that hasn't been filled or is incorrect
        for (int attempt = 0; attempt < 100; attempt++) {
            int pos = random.nextInt(codeLength);
            if (currentGuess[pos] == -1 || currentGuess[pos] != secretCode[pos]) {
                return secretCode[pos];  // Return the correct color for a position
            }
        }

        return secretCode[0];  // Fallback
    }

    /**
     * Makes a move by guessing a single color in the current position
     */
    public Feedback makeMove(int colorIndex) {
        if (gameOver) {
            throw new IllegalStateException("Game is over. Start a new game.");
        }

        if (colorIndex < 0 || colorIndex >= numColors) {
            throw new IllegalArgumentException("Invalid color index: " + colorIndex);
        }

        // Find the current position in the row
        int position = 0;
        for (int i = 0; i < codeLength; i++) {
            if (guessHistory[currentTurn][i] == -1) {
                position = i;
                break;
            }
        }

        // Store the guess
        guessHistory[currentTurn][position] = colorIndex;

        // Check if row is complete
        if (position == codeLength - 1) {
            // Calculate feedback
            Feedback feedback = calculateFeedback(guessHistory[currentTurn]);
            feedbackHistory[currentTurn] = feedback;

            // Check win condition
            if (feedback.isWin(codeLength)) {
                gameOver = true;
                playerWon = true;
            }

            // Check lose condition (used all turns)
            currentTurn++;
            if (currentTurn >= maxTurns && !playerWon) {
                gameOver = true;
                playerWon = false;
            }

            return feedback;
        }

        return null; // Row not complete yet
    }

    /**
     * Makes a complete guess of all colors at once
     */
    public Feedback makeGuess(int[] guess) {
        if (gameOver) {
            throw new IllegalStateException("Game is over. Start a new game.");
        }

        if (guess == null || guess.length != codeLength) {
            throw new IllegalArgumentException("Guess must contain exactly " + codeLength + " colors");
        }

        // Validate all color indices
        for (int color : guess) {
            if (color < 0 || color >= numColors) {
                throw new IllegalArgumentException("Invalid color index: " + color);
            }
        }

        // Store the guess
        System.arraycopy(guess, 0, guessHistory[currentTurn], 0, codeLength);

        // Calculate feedback
        Feedback feedback = calculateFeedback(guess);
        feedbackHistory[currentTurn] = feedback;

        // Check win condition
        if (feedback.isWin(codeLength)) {
            gameOver = true;
            playerWon = true;
        }

        // Check lose condition
        currentTurn++;
        if (currentTurn >= maxTurns && !playerWon) {
            gameOver = true;
            playerWon = false;
        }

        return feedback;
    }

    /**
     * Calculates feedback (black and white pegs) for a guess
     */
    private Feedback calculateFeedback(int[] guess) {
        int blackPegs = 0;
        int whitePegs = 0;

        int[] codeCopy = secretCode.clone();
        int[] guessCopy = guess.clone();

        final int PROCESSED = 999;

        // First pass: Count black pegs (exact matches)
        for (int i = 0; i < codeLength; i++) {
            if (guessCopy[i] == codeCopy[i]) {
                blackPegs++;
                codeCopy[i] = PROCESSED;
                guessCopy[i] = PROCESSED;
            }
        }

        // Second pass: Count white pegs (color matches in wrong position)
        for (int i = 0; i < codeLength; i++) {
            if (guessCopy[i] != PROCESSED) {
                for (int j = 0; j < codeLength; j++) {
                    if (codeCopy[j] == guessCopy[i]) {
                        whitePegs++;
                        codeCopy[j] = PROCESSED;
                        break;
                    }
                }
            }
        }

        return new Feedback(blackPegs, whitePegs);
    }

    // Getters

    public Level getLevel() { return level; }
    public int getNumColors() { return numColors; }
    public int getCodeLength() { return codeLength; }
    public int getMaxTurns() { return maxTurns; }
    public int getCurrentTurn() { return currentTurn; }
    public boolean isGameOver() { return gameOver; }
    public boolean didPlayerWin() { return playerWon; }
    public int getHintsRemaining() { return hintsRemaining; }
    public float getElapsedTime() { return elapsedTime; }

    public int[] getSecretCode() {
        return gameOver ? secretCode.clone() : null;
    }

    public int[] getGuess(int turn) {
        if (turn < 0 || turn >= currentTurn) {
            return null;
        }
        return guessHistory[turn].clone();
    }

    public Feedback getFeedback(int turn) {
        if (turn < 0 || turn >= currentTurn) {
            return null;
        }
        return feedbackHistory[turn];
    }

    public int[] getCurrentGuess() {
        if (gameOver) {
            return null;
        }
        return guessHistory[currentTurn].clone();
    }

    public boolean isCurrentRowComplete() {
        if (gameOver) {
            return false;
        }

        for (int i = 0; i < codeLength; i++) {
            if (guessHistory[currentTurn][i] == -1) {
                return false;
            }
        }
        return true;
    }

    public int getCurrentPosition() {
        if (gameOver) {
            return -1;
        }

        for (int i = 0; i < codeLength; i++) {
            if (guessHistory[currentTurn][i] == -1) {
                return i;
            }
        }
        return codeLength;
    }

    public boolean getAllowDuplicates() {
        return allowDuplicates;
    }

    /**
     * Get star rating for current game (if won)
     */
    public int getStarRating() {
        if (!playerWon || level == null) {
            return 0;
        }
        return level.getStarRating(currentTurn);
    }

    /**
     * Returns the color name for a given color index
     */
    public static String getColorName(int colorIndex) {
        switch (colorIndex) {
            case RED: return "Red";
            case BLUE: return "Blue";
            case GREEN: return "Green";
            case PURPLE: return "Purple";
            case YELLOW: return "Yellow";
            case ORANGE: return "Orange";
            case BLACK: return "Black";
            case WHITE: return "White";
            default: return "Unknown";
        }
    }
}
