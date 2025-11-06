/*********************************************************
 * GAME TITLE: Mastermind HD - Sound Manager
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Centralized sound effect management
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all game sounds and music
 */
public class SoundManager {

    private static SoundManager instance;

    private Map<String, Sound> sounds;
    private Music backgroundMusic;

    private boolean soundEnabled = true;
    private boolean musicEnabled = true;

    // Volume levels
    private float soundVolume = 0.7f;
    private float musicVolume = 0.5f;

    private SoundManager() {
        sounds = new HashMap<>();
        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Load all sound effects
     */
    private void loadSounds() {
        try {
            // Load sound effects (create placeholder if files don't exist)
            loadSound("tap", "sounds/tap.mp3");
            loadSound("select", "sounds/select.mp3");
            loadSound("hint", "sounds/hint.mp3");
            loadSound("place", "sounds/place.mp3");
            loadSound("correct", "sounds/correct.mp3");
            loadSound("wrong", "sounds/wrong.mp3");
            loadSound("win", "sounds/win.mp3");
            loadSound("lose", "sounds/lose.mp3");
            loadSound("unlock", "sounds/unlock.mp3");
            loadSound("star", "sounds/star.mp3");
            loadSound("button", "sounds/button.mp3");
            loadSound("whoosh", "sounds/whoosh.mp3");
            loadSound("tick", "sounds/tick.mp3");
            loadSound("complete", "sounds/complete.mp3");

            // Load background music
            if (Gdx.files.internal("music/bgm.mp3").exists()) {
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/bgm.mp3"));
                backgroundMusic.setLooping(true);
                backgroundMusic.setVolume(musicVolume);
            }

            Gdx.app.log("SoundManager", "Sounds loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("SoundManager", "Error loading sounds", e);
        }
    }

    /**
     * Load a single sound effect
     */
    private void loadSound(String name, String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
                sounds.put(name, sound);
            } else {
                Gdx.app.log("SoundManager", "Sound file not found: " + path + " (will be silent)");
            }
        } catch (Exception e) {
            Gdx.app.error("SoundManager", "Error loading sound: " + name, e);
        }
    }

    /**
     * Play a sound effect
     */
    public void playSound(String name) {
        if (!soundEnabled) return;

        Sound sound = sounds.get(name);
        if (sound != null) {
            sound.play(soundVolume);
        }
    }

    /**
     * Play a sound with custom volume
     */
    public void playSound(String name, float volume) {
        if (!soundEnabled) return;

        Sound sound = sounds.get(name);
        if (sound != null) {
            sound.play(volume * soundVolume);
        }
    }

    /**
     * Play background music
     */
    public void playMusic() {
        if (musicEnabled && backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    /**
     * Stop background music
     */
    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    /**
     * Pause background music
     */
    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    /**
     * Resume background music
     */
    public void resumeMusic() {
        if (musicEnabled && backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    // Settings

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        GameProgress.getInstance().setSoundEnabled(enabled);
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        GameProgress.getInstance().setMusicEnabled(enabled);

        if (enabled) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = Math.max(0, Math.min(1, volume));
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume);
        }
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Load settings from preferences
     */
    public void loadSettings() {
        GameProgress progress = GameProgress.getInstance();
        soundEnabled = progress.isSoundEnabled();
        musicEnabled = progress.isMusicEnabled();

        if (musicEnabled) {
            playMusic();
        }
    }

    /**
     * Dispose of all sounds
     */
    public void dispose() {
        for (Sound sound : sounds.values()) {
            if (sound != null) {
                sound.dispose();
            }
        }
        sounds.clear();

        if (backgroundMusic != null) {
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }

    // Convenience methods for common sound effects

    public void playTap() { playSound("tap"); }
    public void playSelect() { playSound("select"); }
    public void playHint() { playSound("hint"); }
    public void playPlace() { playSound("place"); }
    public void playCorrect() { playSound("correct"); }
    public void playWrong() { playSound("wrong"); }
    public void playWin() { playSound("win"); }
    public void playLose() { playSound("lose"); }
    public void playUnlock() { playSound("unlock"); }
    public void playStar() { playSound("star"); }
    public void playButton() { playSound("button"); }
    public void playWhoosh() { playSound("whoosh"); }
    public void playTick() { playSound("tick"); }
    public void playComplete() { playSound("complete"); }
}
