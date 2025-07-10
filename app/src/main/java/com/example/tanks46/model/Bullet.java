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

        // Загружаем и масштабируем изображение пули
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        bitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

        // Загружаем и масштабируем изображения анимации пули
        Bitmap fireBitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fire);
        Bitmap fireBitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fire2);
        fireBitmaps = new Bitmap[]{
                Bitmap.createScaledBitmap(fireBitmap1, width, height, true),
                Bitmap.createScaledBitmap(fireBitmap2, width, height, true)
        };

        lastFrameTime = System.nanoTime();
    }

    public void update(float deltaTime) {
        x += (float) (speed * Math.cos(Math.toRadians(rotation)));
        y += (float) (speed * Math.sin(Math.toRadians(rotation)));

        if (isAnimating) {
            long currentTime = System.nanoTime();
            if (currentTime - lastFrameTime > 100000000) { // Смена кадра каждые 100 мс
                currentFrame = (currentFrame + 1) % fireBitmaps.length;
                lastFrameTime = currentTime;
            }
        }
    }

    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        matrix.postTranslate(x - bitmap.getWidth() / 2f, y - bitmap.getHeight() / 2f);

        if (isAnimating) {
            canvas.drawBitmap(fireBitmaps[currentFrame], matrix, null);
        } else {
            canvas.drawBitmap(bitmap, matrix, null);
        }
    }

    public void startAnimation() {
        isAnimating = true;
    }

    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }
}
