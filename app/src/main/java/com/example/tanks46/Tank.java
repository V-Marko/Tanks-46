package com.example.tanks46;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Tank {
    private Bitmap bitmap;
    public float x, y;
    private float speed = 5;
    private float rotation = 0; 

    public Tank(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank_player);
        x = 500;
        y = 500;
    }

    public void update(float dx, float dy) {
        x += dx * speed;
        y += dy * speed;

        if (dx != 0 || dy != 0) {
            rotation = (float) Math.toDegrees(Math.atan2(dy, dx));
        }
    }

    public void draw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        matrix.postTranslate(x, y);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}