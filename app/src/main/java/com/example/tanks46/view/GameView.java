package com.example.tanks46.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.tanks46.R;
import com.example.tanks46.model.Joystick;
import com.example.tanks46.model.Tank;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private int _TankWidth = 500;
    private int _TankHeight = 500;
    private int _JoystickX = 200;
    private int _JoystickY;

    private Thread thread;
    private boolean isRunning = true;
    private Paint paint = new Paint();
    private Tank player;
    private Joystick joystick;
    private boolean joystickPressed = false;
    private long lastFrameTime;

    public GameView(Context context) {
        super(context);
        init(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        getHolder().addCallback(this);
        setFocusable(true);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameView);
            _TankWidth = a.getDimensionPixelSize(R.styleable.GameView_tankWidth, _TankWidth);
            _TankHeight = a.getDimensionPixelSize(R.styleable.GameView_tankHeight, _TankHeight);
            a.recycle();
            Log.d("GameView", "TankWidth: " + _TankWidth + ", TankHeight: " + _TankHeight); // Отладка
        }
        player = new Tank(context, _TankWidth, _TankHeight);

        _JoystickY = getResources().getDisplayMetrics().heightPixels - 200;
        joystick = new Joystick(_JoystickX, _JoystickY, 150, 75);
        lastFrameTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (isRunning) {
            if (!getHolder().getSurface().isValid()) continue;

            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f;
            lastFrameTime = currentTime;

            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                int screenWidth = canvas.getWidth(); // Динамическая ширина
                int screenHeight = canvas.getHeight(); // Динамическая высота
                player.update(joystick.getActuatorX(), joystick.getActuatorY(), deltaTime, screenWidth, screenHeight);
                player.draw(canvas);
                joystick.draw(canvas, paint);
                getHolder().unlockCanvasAndPost(canvas);
            }

            try {
                Thread.sleep(17);
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