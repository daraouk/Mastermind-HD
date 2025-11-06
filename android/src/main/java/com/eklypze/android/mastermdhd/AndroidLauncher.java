package com.eklypze.android.mastermdhd;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.eklypze.android.mastermdhd.core.MastermindHDGame;

/**
 * Android launcher for Mastermind HD
 * This is the entry point for the Android application
 */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useGyroscope = false;
        config.useWakelock = true;

        initialize(new MastermindHDGame(), config);
    }
}
