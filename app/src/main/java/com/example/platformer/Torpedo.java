package com.example.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class Torpedo {

    private Bitmap bitmap;
    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int padding;

    private int currentFrame;
    private double frameTime;
    private double timeForCurrentFrame;

    private double x;
    private double y;
    private double fx;
    private double fy;
    private double velocityX;
    private double velocityY;

    private double speed = 10;

    private Joystick joystick;
    double stepX;
    public Torpedo(Bitmap bitmap, double positionX, double positionY, double finX, double finY, Rect initialFrame) {
        Log.d("My", "Тест");
        this.bitmap = bitmap;
        x = positionX;
        y = positionY;
        fx=finX;
        fy=finY;

        double dx = finX - x;
        double dy = finY - y;
         stepX= dx/dy;
        // на 1 пиксель по y будет приходиться y*stepX пикселей по х

        velocityX = 0;
        velocityY = 0;
        frames = new ArrayList<>();
        frames.add(initialFrame);
        this.timeForCurrentFrame = 0.0;
        this.currentFrame = 0;
        this.padding = 20;
        this.frameTime = 0.1;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
    }

    public void update(){
        x = x+ (stepX);
        y = y;
        if (x==fx&&y==fy){
            Log.i("LofUpdate", "дошло");
        }
    }


    public void draw(Canvas canvas){
        Paint paint = new Paint();
        Log.i("LofUpdate", String.valueOf(frames.size()));
        Rect destination = new Rect((int)555, (int)555, (int)(55+55), (int)(55+55));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, paint);
        canvas.drawCircle(50, 50, 20, paint);
    }

    public Rect getBoundingBoxRect(){
        return new Rect((int)(x+padding), (int)(y+padding),
                (int)(x+frameWidth-2*padding), (int)(y+frameHeight-2*padding));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<Rect> getFrames() {
        return frames;
    }

    public void setFrames(List<Rect> frames) {
        this.frames = frames;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public double getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(double frameTime) {
        this.frameTime = frameTime;
    }

    public double getTimeForCurrentFrame() {
        return timeForCurrentFrame;
    }

    public void setTimeForCurrentFrame(double timeForCurrentFrame) {
        this.timeForCurrentFrame = timeForCurrentFrame;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }
}
