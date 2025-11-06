/*********************************************************
 * GAME TITLE: Mastermind HD - Core Game Logic
 * AUTHOR: Dara Ouk (Modernized)
 * VERSION: 2.0
 * DESCRIPTION: Framework-independent Mastermind game logic
 *     This class contains all the core game rules and state
 *     management, separated from UI/rendering concerns.
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import java.util.Arrays;
import java.util.Random;

/**
 * Core Mastermind game logic.
 * Handles code generation, move validation, scoring, and game state.
 */
public class MastermindGame {

    // Game Constants
    public static final int NUM_COLORS = 8;
    public static final int CODE_LENGTH = 4;
    public static final int MAX_TURNS = 10;

    // Color indices (matches your original mapping)
    public static final int RED = 0;
    public static final int BLUE = 1;
    public static final int GREEN = 2;
    public static final int PURPLE = 3;
    public static final int YELLOW = 4;
    public static final int ORANGE = 5;
    public static final int BLACK = 6;
    public static final int WHITE = 7;

    // Game State
    private int[] secretCode;
    private boolean allowDuplicates;
    private int currentTurn;
    private boolean gameOver;
    private boolean playerWon;
    private int[][] guessHistory;     // Stores all guesses made
    private Feedback[] feedbackHistory; // Stores all feedback

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

        public boolean isWin() {
            return blackPegs == CODE_LENGTH;
        }

        @Override
        public String toString() {
            return "Feedback{blackPegs=" + blackPegs + ", whitePegs=" + whitePegs + "}";
        }
    }

    /**
     * Creates a new Mastermind game with default settings (no duplicates)
     */
    public MastermindGame() {
        this(false);
    }

    /**
     * Creates a new Mastermind game
     * @param allowDuplicates whether the secret code can contain duplicate colors
     */
    public MastermindGame(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
        this.guessHistory = new int[MAX_TURNS][CODE_LENGTH];
        this.feedbackHistory = new Feedback[MAX_TURNS];
        startNewGame();
    }

    /**
     * Starts a new game by generating a random secret code
     */
    public void startNewGame() {
        this.secretCode = generateSecretCode();
        this.currentTurn = 0;
        this.gameOver = false;
        this.playerWon = false;

        // Clear history
        for (int i = 0; i < MAX_TURNS; i++) {
            Arrays.fill(guessHistory[i], -1);
            feedbackHistory[i] = null;
        }
    }

    /**
     * Generates a random secret code
     * @return array of COLOR_LENGTH integers representing colors
     */
    private int[] generateSecretCode() {
        int[] code = new int[CODE_LENGTH];
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int color = random.nextInt(NUM_COLORS);

            // If duplicates not allowed, ensure uniqueness
            if (!allowDuplicates) {
                boolean isDuplicate = false;
                for (int j = 0; j < i; j++) {
                    if (code[j] == color) {
                        isDuplicate = true;
                        break;
                    }
                }

                // If duplicate found, try again
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
     * Makes a move by guessing a single color in the current position
     * @param colorIndex the color index (0-7)
     * @return Feedback object if the row is complete, null if more colors needed
     * @throws IllegalStateException if game is over
     * @throws IllegalArgumentException if colorIndex is invalid
     */
    public Feedback makeMove(int colorIndex) {
        if (gameOver) {
            throw new IllegalStateException("Game is over. Start a new game.");
        }

        if (colorIndex < 0 || colorIndex >= NUM_COLORS) {
            throw new IllegalArgumentException("Invalid color index: " + colorIndex);
        }

        // Find the current position in the row
        int position = 0;
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guessHistory[currentTurn][i] == -1) {
                position = i;
                break;
            }
        }

        // Store the guess
        guessHistory[currentTurn][position] = colorIndex;

        // Check if row is complete
        if (position == CODE_LENGTH - 1) {
            // Calculate feedback
            Feedback feedback = calculateFeedback(guessHistory[currentTurn]);
            feedbackHistory[currentTurn] = feedback;

            // Check win condition
            if (feedback.isWin()) {
                gameOver = true;
                playerWon = true;
            }

            // Check lose condition (used all turns)
            currentTurn++;
            if (currentTurn >= MAX_TURNS && !playerWon) {
                gameOver = true;
                playerWon = false;
            }

            return feedback;
        }

        return null; // Row not complete yet
    }

    /**
     * Makes a complete guess of all 4 colors at once
     * @param guess array of 4 color indices
     * @return Feedback object with black and white peg counts
     * @throws IllegalStateException if game is over
     * @throws IllegalArgumentException if guess is invalid
     */
    public Feedback makeGuess(int[] guess) {
        if (gameOver) {
            throw new IllegalStateException("Game is over. Start a new game.");
        }

        if (guess == null || guess.length != CODE_LENGTH) {
            throw new IllegalArgumentException("Guess must contain exactly " + CODE_LENGTH + " colors");
        }

        // Validate all color indices
        for (int color : guess) {
            if (color < 0 || color >= NUM_COLORS) {
                throw new IllegalArgumentException("Invalid color index: " + color);
            }
        }

        // Store the guess
        System.arraycopy(guess, 0, guessHistory[currentTurn], 0, CODE_LENGTH);

        // Calculate feedback
        Feedback feedback = calculateFeedback(guess);
        feedbackHistory[currentTurn] = feedback;

        // Check win condition
        if (feedback.isWin()) {
            gameOver = true;
            playerWon = true;
        }

        // Check lose condition
        currentTurn++;
        if (currentTurn >= MAX_TURNS && !playerWon) {
            gameOver = true;
            playerWon = false;
        }

        return feedback;
    }

    /**
     * Calculates feedback (black and white pegs) for a guess
     * This is the core Mastermind scoring algorithm
     *
     * @param guess the player's guess
     * @return Feedback object with peg counts
     */
    private Feedback calculateFeedback(int[] guess) {
        int blackPegs = 0;
        int whitePegs = 0;

        // Create working copies to avoid modifying state
        int[] codeCopy = secretCode.clone();
        int[] guessCopy = guess.clone();

        // Use a sentinel value to mark processed positions
        final int PROCESSED = 999;

        // First pass: Count black pegs (exact matches)
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guessCopy[i] == codeCopy[i]) {
                blackPegs++;
                // Mark as processed so we don't count it again
                codeCopy[i] = PROCESSED;
                guessCopy[i] = PROCESSED;
            }
        }

        // Second pass: Count white pegs (color matches in wrong position)
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guessCopy[i] != PROCESSED) {
                // Check if this color exists elsewhere in the code
                for (int j = 0; j < CODE_LENGTH; j++) {
                    if (codeCopy[j] == guessCopy[i]) {
                        whitePegs++;
                        // Mark as processed
                        codeCopy[j] = PROCESSED;
                        break;
                    }
                }
            }
        }

        return new Feedback(blackPegs, whitePegs);
    }

    // Getters

    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean didPlayerWin() {
        return playerWon;
    }

    public int[] getSecretCode() {
        // Only reveal if game is over
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

        for (int i = 0; i < CODE_LENGTH; i++) {
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

        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guessHistory[currentTurn][i] == -1) {
                return i;
            }
        }
        return CODE_LENGTH; // Row is complete
    }

    public boolean getAllowDuplicates() {
        return allowDuplicates;
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
