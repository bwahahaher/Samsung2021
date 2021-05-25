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
    private double angle = 0;
    private double x;
    double lenghY;
    double lenghX;
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
         lenghX = fx-x;
         lenghY = fy-y;
    }

    public void update(){
//        double gip = Math.sqrt((fy-y)*(fy-y)+(fx-x)*(fx-x));
//        if (angle==0){
//        angle= Math.tan((fy-y)/(fx-x));}
        speed=1/(Math.abs(lenghX)+Math.abs(lenghY))*20;

        x = x+lenghX*speed;
        y = y+lenghY*speed+10;
        Log.i("LofUpdate", "speed"+speed);
        if (x==fx&&y==fy){
            Log.i("LofUpdate", "дошло");
        }
    }


    public void draw(Canvas canvas){
        Paint paint = new Paint();
        Rect destination = new Rect((int)2, (int)2, (int)(2), (int)(2));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, paint);
        if (x!=fx){
            canvas.drawCircle((float)x, (float)y, 20, paint);
        }
        else{
            canvas.drawCircle((float)x, (float)y, 90, paint);
        }
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
    public double getFx() {
        return fx;
    }
    public double getFy() {
        return fy;
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
