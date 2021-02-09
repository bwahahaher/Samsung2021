package com.example.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Joystick joystick;
    private final Player player;
    protected SurfaceHolder holder;

    public Game(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        joystick = new Joystick(275, 1000, 70, 40);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_run);
        int w = bitmap.getWidth()/6; // в изображении 6 кадров анимации
        int h = bitmap.getHeight();
        Rect rect = new Rect(0, 0, w, h);
        player = new Player(bitmap, joystick, 0, 0, rect);

        for (int i = 1; i < 6; i++){
            player.getFrames().add(new Rect(i*w, 0, i*w+w, h));
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        MyThread thread = new MyThread(); // создаем и запускаем поток, делать именно в этом методе
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: // если каснулся
                if (joystick.getIsPressed()) {
                    player.jump();
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystick.setIsPressed(true);
                } else {
                    player.jump();
                }
                return true;
            case MotionEvent.ACTION_MOVE: // если палец движется по экрану
                if (joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP: // палец поднят

                joystick.setIsPressed(false);
                joystick.resetActuator();

                return true;
        }

        return super.onTouchEvent(event);
    }

    class MyThread extends Thread{ //поток для отрисовки

        boolean work = true;

        @Override
        public void run() {
            Canvas canvas;

            int viewWidth = getHolder().getSurfaceFrame().right; // получаем размеры экрана на всякий случай
            int viewHeight = getHolder().getSurfaceFrame().bottom;

            while (work){
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.BLUE);
                joystick.update();
                player.update(1);
                player.draw(canvas);
                joystick.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
