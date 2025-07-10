package com.example.tanks46.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.tanks46.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tank {
    //Bullet
    public int _BulletWidth = 100;
    public int _BulletHeight = 100;


    private Context context; // Store the context
    private Bitmap bitmap;
    public float x, y;
    private float _speed = 10;
    private float rotation = 0;
    private float rotationSpeed = 180.0f;
    private List<Bullet> bullets = new ArrayList<>();

    public Tank(Context context, int width, int height) {
        this.context = context;
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
        x += dx * _speed;
        y += dy * _speed;
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

        // Use an iterator to safely remove bullets while iterating
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update(deltaTime);
            if (bullet.isOutOfBounds(screenWidth, screenHeight)) {
                iterator.remove(); // Safely remove the bullet
            }
        }
    }
    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        matrix.postTranslate(x, y);
        canvas.drawBitmap(bitmap, matrix, null);

        List<Bullet> bulletsCopy = new ArrayList<>(bullets);
        for (Bullet bullet : bulletsCopy) {
            bullet.draw(canvas);
        }
    }

    public void shoot() {
        float offsetX = (float) (22 * Math.cos(Math.toRadians(rotation)));
        float offsetY = (float) (22 * Math.sin(Math.toRadians(rotation)));

        Bullet bullet = new Bullet(
                context,
                x + bitmap.getWidth() / 2f + offsetX,
                y + bitmap.getHeight() / 2f + offsetY,
                rotation,
                _BulletWidth,
                _BulletHeight

        );
        bullet.startAnimation();
        bullets.add(bullet);
    }

}
