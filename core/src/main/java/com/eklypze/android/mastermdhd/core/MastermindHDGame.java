/*********************************************************
 * GAME TITLE: Mastermind HD
 * AUTHOR: Dara Ouk (Modernized with libGDX)
 * VERSION: 2.0
 * DESCRIPTION: Main game class for Mastermind HD using libGDX
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class that manages the application lifecycle and screens
 */
public class MastermindHDGame extends Game {

    public static final int GAME_WIDTH = 480;
    public static final int GAME_HEIGHT = 800;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Start with the main menu
        setScreen(new MainMenuScreen(this));

        Gdx.app.log("MastermindHD", "Game initialized - v2.0 with 100 levels!");
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) {
            batch.dispose();
        }
    }
}
