/*********************************************************
 * GAME TITLE: Mastermind HD - Level Manager
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Manages all 100 levels
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all game levels and their configurations
 */
public class LevelManager {

    private static LevelManager instance;
    private final List<Level> levels;

    private LevelManager() {
        levels = new ArrayList<>();
        initializeLevels();
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    /**
     * Initialize all 100 levels with progressive difficulty
     */
    private void initializeLevels() {
        // TUTORIAL LEVELS (1-5): Learn the basics
        levels.add(new Level.Builder(1)
                .name("First Steps")
                .difficulty(Level.Difficulty.TUTORIAL)
                .numColors(3)
                .codeLength(3)
                .maxTurns(10)
                .hints(5)
                .threeStars(4).twoStars(6)
                .build());

        levels.add(new Level.Builder(2)
                .name("Getting Warmer")
                .difficulty(Level.Difficulty.TUTORIAL)
                .numColors(4)
                .codeLength(3)
                .maxTurns(10)
                .hints(4)
                .threeStars(5).twoStars(7)
                .build());

        levels.add(new Level.Builder(3)
                .name("Four Is More")
                .difficulty(Level.Difficulty.TUTORIAL)
                .numColors(4)
                .codeLength(4)
                .maxTurns(10)
                .hints(3)
                .threeStars(5).twoStars(7)
                .build());

        levels.add(new Level.Builder(4)
                .name("Color Burst")
                .difficulty(Level.Difficulty.TUTORIAL)
                .numColors(5)
                .codeLength(4)
                .maxTurns(10)
                .hints(3)
                .threeStars(6).twoStars(8)
                .build());

        levels.add(new Level.Builder(5)
                .name("Tutorial Complete")
                .difficulty(Level.Difficulty.TUTORIAL)
                .numColors(6)
                .codeLength(4)
                .maxTurns(10)
                .hints(2)
                .threeStars(6).twoStars(8)
                .build());

        // EASY LEVELS (6-25): Build confidence
        for (int i = 6; i <= 25; i++) {
            levels.add(new Level.Builder(i)
                    .name("Easy " + (i - 5))
                    .difficulty(Level.Difficulty.EASY)
                    .numColors(4 + (i % 3))  // 4-6 colors
                    .codeLength(4)
                    .maxTurns(10)
                    .hints(2)
                    .threeStars(5).twoStars(7)
                    .build());
        }

        // MEDIUM LEVELS (26-50): Introduce duplicates and more colors
        for (int i = 26; i <= 50; i++) {
            boolean useDuplicates = i % 2 == 0;
            levels.add(new Level.Builder(i)
                    .name("Medium " + (i - 25))
                    .difficulty(Level.Difficulty.MEDIUM)
                    .numColors(5 + (i % 4))  // 5-8 colors
                    .codeLength(4)
                    .maxTurns(10)
                    .allowDuplicates(useDuplicates)
                    .hints(useDuplicates ? 2 : 1)
                    .threeStars(6).twoStars(8)
                    .build());
        }

        // HARD LEVELS (51-75): Longer codes and time pressure
        for (int i = 51; i <= 75; i++) {
            boolean isTimed = i % 5 == 0;
            int codeLen = 4 + (i % 2);  // 4 or 5 length codes
            levels.add(new Level.Builder(i)
                    .name("Hard " + (i - 50))
                    .difficulty(Level.Difficulty.HARD)
                    .numColors(6 + (i % 3))  // 6-8 colors
                    .codeLength(codeLen)
                    .maxTurns(codeLen == 5 ? 12 : 10)
                    .allowDuplicates(true)
                    .hints(1)
                    .timed(isTimed)
                    .timeLimit(isTimed ? 180 : 0)  // 3 minutes
                    .threeStars(codeLen == 5 ? 7 : 6).twoStars(codeLen == 5 ? 9 : 8)
                    .build());
        }

        // EXPERT LEVELS (76-90): Maximum difficulty
        for (int i = 76; i <= 90; i++) {
            boolean isTimed = i % 3 == 0;
            int codeLen = 4 + ((i - 75) / 5);  // Gradually increase to 6
            if (codeLen > 6) codeLen = 6;

            levels.add(new Level.Builder(i)
                    .name("Expert " + (i - 75))
                    .difficulty(Level.Difficulty.EXPERT)
                    .numColors(8)
                    .codeLength(codeLen)
                    .maxTurns(codeLen + 8)  // More turns for longer codes
                    .allowDuplicates(true)
                    .hints(codeLen >= 5 ? 1 : 0)
                    .timed(isTimed)
                    .timeLimit(isTimed ? 240 : 0)  // 4 minutes
                    .threeStars(7).twoStars(10)
                    .build());
        }

        // MASTER LEVELS (91-100): Ultimate challenges
        levels.add(new Level.Builder(91)
                .name("Master: Speed Run")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(4)
                .maxTurns(8)
                .allowDuplicates(true)
                .hints(0)
                .timed(true)
                .timeLimit(120)  // 2 minutes
                .threeStars(5).twoStars(6)
                .build());

        levels.add(new Level.Builder(92)
                .name("Master: Long Code")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(6)
                .maxTurns(15)
                .allowDuplicates(true)
                .hints(1)
                .threeStars(9).twoStars(12)
                .build());

        levels.add(new Level.Builder(93)
                .name("Master: No Hints")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(5)
                .maxTurns(12)
                .allowDuplicates(true)
                .hints(0)
                .threeStars(7).twoStars(10)
                .build());

        levels.add(new Level.Builder(94)
                .name("Master: Time Trial")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(7)
                .codeLength(4)
                .maxTurns(10)
                .allowDuplicates(true)
                .hints(0)
                .timed(true)
                .timeLimit(90)  // 90 seconds
                .threeStars(5).twoStars(7)
                .build());

        levels.add(new Level.Builder(95)
                .name("Master: Rainbow")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(5)
                .maxTurns(10)
                .allowDuplicates(false)  // No duplicates makes it harder
                .hints(0)
                .threeStars(6).twoStars(8)
                .build());

        levels.add(new Level.Builder(96)
                .name("Master: Precision")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(5)
                .maxTurns(8)  // Very few attempts
                .allowDuplicates(true)
                .hints(1)
                .threeStars(5).twoStars(6)
                .build());

        levels.add(new Level.Builder(97)
                .name("Master: Marathon")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(6)
                .maxTurns(20)
                .allowDuplicates(true)
                .hints(2)
                .timed(true)
                .timeLimit(300)  // 5 minutes
                .threeStars(10).twoStars(15)
                .build());

        levels.add(new Level.Builder(98)
                .name("Master: Ultimate")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(6)
                .maxTurns(12)
                .allowDuplicates(true)
                .hints(0)
                .threeStars(8).twoStars(10)
                .build());

        levels.add(new Level.Builder(99)
                .name("Master: Gauntlet")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(5)
                .maxTurns(10)
                .allowDuplicates(true)
                .hints(0)
                .timed(true)
                .timeLimit(150)  // 2.5 minutes
                .threeStars(6).twoStars(8)
                .build());

        levels.add(new Level.Builder(100)
                .name("MASTERMIND")
                .difficulty(Level.Difficulty.MASTER)
                .numColors(8)
                .codeLength(6)
                .maxTurns(10)
                .allowDuplicates(true)
                .hints(0)
                .timed(true)
                .timeLimit(180)  // 3 minutes
                .threeStars(6).twoStars(8)
                .build());
    }

    /**
     * Get a level by its number (1-100)
     */
    public Level getLevel(int levelNumber) {
        if (levelNumber < 1 || levelNumber > 100) {
            throw new IllegalArgumentException("Level number must be between 1 and 100");
        }
        return levels.get(levelNumber - 1);
    }

    /**
     * Get all levels
     */
    public List<Level> getAllLevels() {
        return new ArrayList<>(levels);
    }

    /**
     * Get total number of levels
     */
    public int getTotalLevels() {
        return levels.size();
    }
}
