/*********************************************************
 * GAME TITLE: Mastermind HD - Particle Manager
 * AUTHOR: Dara Ouk
 * VERSION: 2.0
 * DESCRIPTION: Particle effects for celebrations and polish
 *********************************************************/

package com.eklypze.android.mastermdhd.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Manages particle effects for visual polish
 */
public class ParticleManager {

    private Array<Particle> particles;
    private boolean enabled = true;

    public ParticleManager() {
        particles = new Array<>();
    }

    /**
     * Individual particle
     */
    private static class Particle {
        float x, y;
        float vx, vy;
        float life;
        float maxLife;
        float size;
        Color color;
        boolean active;

        Particle(float x, float y, float vx, float vy, float life, float size, Color color) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.life = life;
            this.maxLife = life;
            this.size = size;
            this.color = color;
            this.active = true;
        }

        void update(float delta) {
            if (!active) return;

            x += vx * delta;
            y += vy * delta;
            vy -= 200 * delta; // Gravity

            life -= delta;
            if (life <= 0) {
                active = false;
            }
        }

        float getAlpha() {
            return Math.max(0, life / maxLife);
        }
    }

    /**
     * Create a confetti explosion effect
     */
    public void createConfetti(float x, float y, int count) {
        if (!enabled) return;

        Color[] confettiColors = {
            Color.GOLD, Color.YELLOW, Color.ORANGE,
            Color.RED, Color.PINK, Color.PURPLE,
            Color.CYAN, Color.SKY, Color.LIME
        };

        for (int i = 0; i < count; i++) {
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(200f, 400f);
            float vx = MathUtils.cosDeg(angle) * speed;
            float vy = MathUtils.sinDeg(angle) * speed;

            float life = MathUtils.random(1f, 2f);
            float size = MathUtils.random(8f, 16f);
            Color color = confettiColors[MathUtils.random(confettiColors.length - 1)];

            particles.add(new Particle(x, y, vx, vy, life, size, color));
        }

        Gdx.app.log("ParticleManager", "Created " + count + " confetti particles at (" + x + ", " + y + ")");
    }

    /**
     * Create fireworks effect
     */
    public void createFireworks(float x, float y) {
        if (!enabled) return;

        int count = 50;
        for (int i = 0; i < count; i++) {
            float angle = (360f / count) * i;
            float speed = MathUtils.random(150f, 300f);
            float vx = MathUtils.cosDeg(angle) * speed;
            float vy = MathUtils.sinDeg(angle) * speed;

            float life = MathUtils.random(1.5f, 2.5f);
            float size = MathUtils.random(6f, 12f);

            Color color = new Color(
                MathUtils.random(0.5f, 1f),
                MathUtils.random(0.5f, 1f),
                MathUtils.random(0.5f, 1f),
                1f
            );

            particles.add(new Particle(x, y, vx, vy, life, size, color));
        }
    }

    /**
     * Create star burst effect
     */
    public void createStarBurst(float x, float y, Color color) {
        if (!enabled) return;

        int rays = 8;
        int particlesPerRay = 3;

        for (int i = 0; i < rays; i++) {
            float angle = (360f / rays) * i;

            for (int j = 0; j < particlesPerRay; j++) {
                float speed = 150f + (j * 100f);
                float vx = MathUtils.cosDeg(angle) * speed;
                float vy = MathUtils.sinDeg(angle) * speed;

                float life = 1f + (j * 0.3f);
                float size = 10f - (j * 2f);

                particles.add(new Particle(x, y, vx, vy, life, size, color));
            }
        }
    }

    /**
     * Create sparkle effect (small subtle particles)
     */
    public void createSparkle(float x, float y) {
        if (!enabled) return;

        int count = 10;
        for (int i = 0; i < count; i++) {
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(50f, 150f);
            float vx = MathUtils.cosDeg(angle) * speed;
            float vy = MathUtils.sinDeg(angle) * speed;

            float life = MathUtils.random(0.5f, 1f);
            float size = MathUtils.random(3f, 6f);

            particles.add(new Particle(x, y, vx, vy, life, size, Color.YELLOW));
        }
    }

    /**
     * Update all particles
     */
    public void update(float delta) {
        if (!enabled) return;

        // Update particles
        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(delta);

            if (!p.active) {
                particles.removeIndex(i);
            }
        }
    }

    /**
     * Render all particles using ShapeRenderer (no texture needed)
     */
    public void render(SpriteBatch batch, com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer) {
        if (!enabled || particles.size == 0) return;

        // End batch to use ShapeRenderer
        batch.end();

        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        for (Particle p : particles) {
            if (!p.active) continue;

            float alpha = p.getAlpha();
            shapeRenderer.setColor(p.color.r, p.color.g, p.color.b, alpha);

            // Draw particle as filled circle
            shapeRenderer.circle(p.x, p.y, p.size / 2, 8);
        }

        shapeRenderer.end();

        // Resume batch
        batch.begin();
    }

    /**
     * Clear all particles
     */
    public void clear() {
        particles.clear();
    }

    /**
     * Get active particle count
     */
    public int getParticleCount() {
        return particles.size;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            clear();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Dispose resources
     */
    public void dispose() {
        clear();
    }
}
