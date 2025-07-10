package com.example.tanks46.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import com.example.tanks46.R;

public class Tank {
    private Bitmap bitmap;
    public float x, y;
    private float speed = 5;
    private float rotation = 0;
    private float rotationSpeed = 180.0f;

    public Tank(Context context, int width, int height) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank_player);
        if (originalBitmap != null) {
            bitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        } else {
            throw new IllegalStateException("Resource tank_player not found");
        }
        x = 500;
        y = 500;
    }

    public void update(float dx, float dy, float deltaTime, int screenWidth, int screenHeight) {
        x += dx * speed;
        y += dy * speed;

        x = Math.max(0, Math.min(x, screenWidth - bitmap.getWidth()));
        y = Math.max(0, Math.min(y, screenHeight - bitmap.getHeight()));

        if (dx != 0 || dy != 0) {
            float targetRotation = (float) Math.toDegrees(Math.atan2(dy, dx));
            float deltaRotation = targetRotation - rotation;
            while (deltaRotation > 180) deltaRotation -= 360;
            while (deltaRotation < -180) deltaRotation += 360;

            float maxRotationStep = rotationSpeed * deltaTime;
            if (Math.abs(deltaRotation) > maxRotationStep) {
                deltaRotation = Math.signum(deltaRotation) * maxRotationStep;
            }

            rotation += deltaRotation;
            while (rotation >= 360) rotation -= 360;
            while (rotation < 0) rotation += 360;
        }
    }

    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        matrix.postTranslate(x, y);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}