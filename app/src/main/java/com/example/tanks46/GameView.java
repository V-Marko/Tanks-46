package com.example.tanks46;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread thread;
    private boolean isRunning = true;
    private Paint paint = new Paint();
    private Tank player;
    private Joystick joystick;
    private boolean joystickPressed = false;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(this);
        setFocusable(true);
        player = new Tank(context);
        joystick = new Joystick(200, getResources().getDisplayMetrics().heightPixels - 200, 150, 75);
    }

    @Override
    public void run() {
        while (isRunning) {
            if (!getHolder().getSurface().isValid()) continue;

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.BLACK);

            player.update(joystick.getActuatorX(), joystick.getActuatorY());

            player.draw(canvas);
            joystick.draw(canvas, paint);

            getHolder().unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(17); // ~60 fps
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point touch = new Point((int)event.getX(), (int)event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                joystickPressed = joystick.isPressed(touch);
                if (joystickPressed) {
                    joystick.update(touch);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (joystickPressed) {
                    joystick.update(touch);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (joystickPressed) {
                    joystick.reset();
                    joystickPressed = false;
                }
                break;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
}