package com.rachit.tripathi75.soundspot.classes;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LyraAnimationView extends View {

    private Paint circlePaint;
    private Paint particlePaint;
    private Paint linePaint;
    private Paint notePaint;
    private Paint glowPaint;

    private List<Particle> particles = new ArrayList<>();
    private int particleCount = 12;
    private float centerX, centerY, radius;
    private float time = 0;

    public LyraAnimationView(Context context) {
        super(context);
        init();
    }

    public LyraAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyraAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paints
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.parseColor("#8B5CF680")); // Purple with transparency
        circlePaint.setStrokeWidth(2f);

        particlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        particlePaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);

        notePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        notePaint.setColor(Color.parseColor("#8B5CF6E6")); // Purple
        notePaint.setTextSize(40f);
        notePaint.setTextAlign(Paint.Align.CENTER);

        glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowPaint.setStyle(Paint.Style.FILL);
        glowPaint.setColor(Color.parseColor("#8B5CF633")); // Purple with high transparency
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 2f - 10;

        // Create particles
        particles.clear();
        for (int i = 0; i < particleCount; i++) {
            float angle = (float) ((i / (float) particleCount) * Math.PI * 2);
            float x = centerX + (float) Math.cos(angle) * radius;
            float y = centerY + (float) Math.sin(angle) * radius;

            particles.add(new Particle(
                    x, y,
                    3f + (float) (Math.random() * 3),
                    angle,
                    x, y,
                    270 + (float) (Math.random() * 40) // Purple hues
            ));
        }

        // Start animation
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Update time
        time += 0.05f;

        // Draw glow effect
        canvas.drawCircle(centerX, centerY, radius + 10, glowPaint);

        // Draw outer circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Draw musical note in center
        canvas.drawText("♪", centerX, centerY + 15, notePaint);

        // Draw particles and connections
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);

            // Update particle position with oscillation
            particle.angle += 0.02f;
            float oscillation = (float) (Math.sin(time + i) * 5);
            particle.x = particle.originalX + (float) Math.cos(particle.angle) * oscillation;
            particle.y = particle.originalY + (float) Math.sin(particle.angle) * oscillation;

            // Draw particle
            particlePaint.setColor(Color.HSVToColor(new float[]{particle.hue, 1.0f, 0.8f}));
            canvas.drawCircle(particle.x, particle.y, particle.size, particlePaint);

            // Draw connections to adjacent particles
            int nextIndex = (i + 1) % particles.size();
            Particle nextParticle = particles.get(nextIndex);

            linePaint.setColor(Color.HSVToColor(128, new float[]{particle.hue, 1.0f, 0.8f}));
            canvas.drawLine(particle.x, particle.y, nextParticle.x, nextParticle.y, linePaint);

            // Draw connection to center with gradient
            LinearGradient gradient = new LinearGradient(
                    particle.x, particle.y, centerX, centerY,
                    Color.HSVToColor(204, new float[]{particle.hue, 1.0f, 0.8f}),
                    Color.HSVToColor(25, new float[]{particle.hue, 1.0f, 0.8f}),
                    Shader.TileMode.CLAMP
            );
            linePaint.setShader(gradient);
            canvas.drawLine(particle.x, particle.y, centerX, centerY, linePaint);
            linePaint.setShader(null);
        }

        // Continue animation
        postInvalidateOnAnimation();
    }

    private static class Particle {
        float x, y;
        float size;
        float angle;
        float originalX, originalY;
        float hue;

        Particle(float x, float y, float size, float angle, float originalX, float originalY, float hue) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.angle = angle;
            this.originalX = originalX;
            this.originalY = originalY;
            this.hue = hue;
        }
    }
}