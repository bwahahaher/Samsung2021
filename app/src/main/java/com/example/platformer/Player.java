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

public class Player {

    private Bitmap bitmap;
    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int padding;
 private double playerSpeed = 3;
    private int currentFrame;
    private double frameTime;
    private double timeForCurrentFrame;

    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    private Joystick joystick;

    public Player(Bitmap bitmap, Joystick joystick, double positionX, double positionY, Rect initialFrame) {
        this.joystick = joystick;
        this.bitmap = bitmap;
        x = positionX;
        y = positionY;
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

    public void update(int ms){

        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime){
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }


        x = x + playerSpeed*joystick.getActuatorX();
        y = y + playerSpeed*joystick.getActuatorY();
        currentFrame = (currentFrame + 1) % frames.size();
    }

    public Torpedo shoot(MotionEvent event, double startX, double startY){
        Bitmap bitmapTorpedo = Bitmap.createBitmap(10, 10,Bitmap.Config.ARGB_8888);
        Rect rectTorpedo = new Rect(0, 0, 10, 10);
        Torpedo torpedo = new Torpedo(bitmapTorpedo, startX, startY, event.getX(), event.getY(), rectTorpedo);
       return torpedo;

    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        Rect destination = new Rect((int)x, (int)y, (int)(x+frameWidth), (int)(y+frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, paint);
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
    public void setSide(int width, int height){
        if (x>width){
            x=0;
        }
        else if (x<0){
            x=width;
        }
        if (y>height){
            y=0;
        }
        else if (y<0){
            y=height;
        }
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
