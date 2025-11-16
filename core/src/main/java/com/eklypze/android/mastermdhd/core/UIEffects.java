/*********************************************************
 * GAME TITLE: Mastermind HD - UI Effects
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Visual effects and animations for UI elements
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Utility class for UI visual effects and animations
 */
public class UIEffects {

    /**
     * Draw a button with optional hover/press effect
     */
    public static void drawButton(ShapeRenderer shapeRenderer, Rectangle button,
                                   boolean isHovered, boolean isPressed, Color baseColor) {
        float scale = 1.0f;
        Color color = baseColor.cpy();

        if (isPressed) {
            scale = 0.95f;
            color.mul(0.8f, 0.8f, 0.8f, 1.0f);
        } else if (isHovered) {
            scale = 1.05f;
            color.mul(1.2f, 1.2f, 1.2f, 1.0f);
        }

        float scaledWidth = button.width * scale;
        float scaledHeight = button.height * scale;
        float offsetX = (button.width - scaledWidth) / 2;
        float offsetY = (button.height - scaledHeight) / 2;

        // Shadow (offset slightly)
        shapeRenderer.setColor(0, 0, 0, 0.3f);
        shapeRenderer.rect(button.x + offsetX + 4, button.y + offsetY - 4, scaledWidth, scaledHeight);

        // Button background
        shapeRenderer.setColor(color);
        shapeRenderer.rect(button.x + offsetX, button.y + offsetY, scaledWidth, scaledHeight);

        // Border
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(button.x + offsetX, button.y + offsetY, scaledWidth, scaledHeight);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    }

    /**
     * Draw a pulsing effect for highlighted elements
     */
    public static float getPulseAlpha(float time, float speed) {
        return 0.5f + 0.5f * MathUtils.sin(time * speed);
    }

    /**
     * Draw a glowing border effect
     */
    public static void drawGlow(ShapeRenderer shapeRenderer, Rectangle bounds,
                                 float time, Color glowColor) {
        float alpha = getPulseAlpha(time, 3.0f);
        Color color = glowColor.cpy();
        color.a = alpha * 0.5f;

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(3);

        for (int i = 0; i < 3; i++) {
            float offset = i * 2;
            shapeRenderer.setColor(color.r, color.g, color.b, color.a / (i + 1));
            shapeRenderer.rect(bounds.x - offset, bounds.y - offset,
                             bounds.width + offset * 2, bounds.height + offset * 2);
        }

        Gdx.gl.glLineWidth(1);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    }

    /**
     * Calculate screen fade alpha for transitions
     */
    public static float getFadeAlpha(float fadeTime, float fadeLength, boolean fadeIn) {
        float progress = MathUtils.clamp(fadeTime / fadeLength, 0, 1);
        return fadeIn ? progress : 1 - progress;
    }

    /**
     * Draw a fade overlay
     */
    public static void drawFade(ShapeRenderer shapeRenderer, float alpha, int width, int height) {
        if (alpha <= 0) return;

        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, width, height);
    }

    /**
     * Calculate bounce animation scale
     */
    public static float getBounceScale(float time, float bounceDuration) {
        if (time >= bounceDuration) return 1.0f;

        float progress = time / bounceDuration;
        // Elastic bounce effect
        return 1.0f + (float) Math.sin(progress * Math.PI * 4) * (1 - progress) * 0.3f;
    }

    /**
     * Draw a shiny highlight on a button
     */
    public static void drawShine(ShapeRenderer shapeRenderer, Rectangle bounds, float time) {
        float shinePos = (time % 3.0f) / 3.0f; // Move across every 3 seconds
        float shineX = bounds.x + bounds.width * shinePos;
        float shineWidth = 20;

        Color shine = new Color(1, 1, 1, 0.3f);
        shapeRenderer.setColor(shine);

        // Draw diagonal shine
        for (int i = 0; i < 3; i++) {
            float x = shineX - shineWidth / 2 + i * 2;
            if (x >= bounds.x && x <= bounds.x + bounds.width) {
                shapeRenderer.rect(x, bounds.y, shineWidth / 3, bounds.height);
            }
        }
    }

    /**
     * Smooth lerp for animations
     */
    public static float smoothLerp(float start, float end, float progress) {
        // Smooth step interpolation
        progress = MathUtils.clamp(progress, 0, 1);
        float smoothProgress = progress * progress * (3 - 2 * progress);
        return start + (end - start) * smoothProgress;
    }

    /**
     * Check if a point is inside a rectangle (for touch detection)
     */
    public static boolean contains(Rectangle rect, float x, float y) {
        return rect.contains(x, y);
    }

    /**
     * Create a color with pulsing brightness
     */
    public static Color getPulsingColor(Color baseColor, float time, float speed) {
        float brightness = 0.8f + 0.2f * MathUtils.sin(time * speed);
        return new Color(
            baseColor.r * brightness,
            baseColor.g * brightness,
            baseColor.b * brightness,
            baseColor.a
        );
    }

    /**
     * Draw a progress bar with style
     */
    public static void drawProgressBar(ShapeRenderer shapeRenderer, float x, float y,
                                        float width, float height, float progress,
                                        Color bgColor, Color fillColor) {
        // Background
        shapeRenderer.setColor(bgColor);
        shapeRenderer.rect(x, y, width, height);

        // Fill
        if (progress > 0) {
            shapeRenderer.setColor(fillColor);
            shapeRenderer.rect(x + 2, y + 2, (width - 4) * progress, height - 4);
        }

        // Border
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    }

    /**
     * Import statements needed for the class
     */
    private static class Gdx {
        public static GL gl = new GL();
        public static class GL {
            public void glLineWidth(float width) {
                com.badlogic.gdx.Gdx.gl.glLineWidth(width);
            }
        }
    }
}
