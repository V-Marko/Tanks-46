package com.example.tanks46;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Joystick {
    private final int baseRadius;
    private final int hatRadius;
    private final Point center;
    private Point actuator = new Point(0, 0);
    private Point current = new Point(0, 0);
    private boolean isActive = false;

    public Joystick(int centerX, int centerY, int baseRadius, int hatRadius) {
        center = new Point(centerX, centerY);
        this.baseRadius = baseRadius;
        this.hatRadius = hatRadius;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.GRAY);
        canvas.drawCircle(center.x, center.y, baseRadius, paint);

        paint.setColor(Color.BLUE);
        canvas.drawCircle(current.x, current.y, hatRadius, paint);
    }

    public void update(Point touchPoint) {
        double dx = touchPoint.x - center.x;
        double dy = touchPoint.y - center.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < baseRadius) {
            current.set(touchPoint.x, touchPoint.y);
        } else {
            double ratio = baseRadius / distance;
            current.set((int)(center.x + dx * ratio), (int)(center.y + dy * ratio));
        }

        actuator.set((int)dx, (int)dy);
    }

    public void reset() {
        current.set(center.x, center.y);
        actuator.set(0, 0);
        isActive = false;
    }

    public float getActuatorX() {
        double distance = Math.sqrt(actuator.x * actuator.x + actuator.y * actuator.y);
        if (distance == 0) return 0;
        return (float)(actuator.x / distance);
    }

    public float getActuatorY() {
        double distance = Math.sqrt(actuator.x * actuator.x + actuator.y * actuator.y);
        if (distance == 0) return 0;
        return (float)(actuator.y / distance);
    }

    public boolean isPressed(Point touch) {
        if (!isActive) {
            boolean withinBase = Math.hypot(touch.x - center.x, touch.y - center.y) < baseRadius;
            if (withinBase) {
                isActive = true;
            }
            return withinBase;
        }
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}