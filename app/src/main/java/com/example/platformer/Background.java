package com.example.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class Background {

    private Bitmap bitmap;
    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int padding;

    private int currentFrame;


    private double x;
    private double y;



    public Background(Bitmap bitmap, double positionX, double positionY, Rect initialFrame) {

        this.bitmap = bitmap;
        x = positionX;
        y = positionY;
        frames = new ArrayList<>();
        frames.add(initialFrame);
        this.padding = 20;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
    }

    public void update(int ms, int my){
        if (y>=my){
            y=-my;
        }
        else {y=y+10;}

    }
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        Rect destination = new Rect((int)x, (int)y, (int)(x+frameWidth), (int)(y+frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, paint);
    }



}
