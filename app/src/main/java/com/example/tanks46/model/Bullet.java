package com.example.tanks46.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import com.example.tanks46.R;

public class Bullet {
    private Bitmap bitmap;
    private Bitmap[] fireBitmaps;
    private float x, y;
    private float speed = 35;
    private float rotation;
    private int currentFrame = 0;
    private long lastFrameTime;
    private boolean isAnimating = false;

    public Bullet(Context context, float x, float y, float rotation, int width, int height) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;

        bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet),
                width, height, true
        );

        fireBitmaps = new Bitmap[] {
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fire), width, height, true),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fire2), width, height, true)
        };

        lastFrameTime = System.nanoTime();
    }

    public void update(float deltaTime) {
        x += speed * Math.cos(Math.toRadians(rotation));
        y += speed * Math.sin(Math.toRadians(rotation));

        if (isAnimating) {
            long currentTime = System.nanoTime();
            if (currentTime - lastFrameTime > 100_000_000) {
                currentFrame = (currentFrame + 1) % fireBitmaps.length;
                lastFrameTime = currentTime;
            }
        }
    }

    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        matrix.postTranslate(x - bitmap.getWidth() / 2f, y - bitmap.getHeight() / 2f);

        canvas.drawBitmap(isAnimating ? fireBitmaps[currentFrame] : bitmap, matrix, null);
    }

    public void startAnimation() {
        isAnimating = true;
    }

    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }
}
