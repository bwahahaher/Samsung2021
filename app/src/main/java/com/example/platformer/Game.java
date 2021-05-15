package com.example.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Joystick joystick;
    private final Player player;
    private final Background background,background1;
    protected SurfaceHolder holder;
    ArrayList<Torpedo> torpedoList;

    public Game(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
       torpedoList = new ArrayList<Torpedo>();

        joystick = new Joystick(displaymetrics.widthPixels/12*10, displaymetrics.heightPixels/12*10, 70, 40);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_run);
        Bitmap bitmapBackFirst = BitmapFactory.decodeResource(getResources(), R.drawable.map1);
        Bitmap bitmapBackSecond = BitmapFactory.decodeResource(getResources(), R.drawable.map2);
        int w = displaymetrics.widthPixels*100/1920;
        int h = displaymetrics.heightPixels*100/1080;
        Rect rect = new Rect(0, 0, w, h);
        Rect rectBackFirst = new Rect(0, 0, displaymetrics.widthPixels, displaymetrics.heightPixels);
        Rect rectBackSecond = new Rect(0, 0, displaymetrics.widthPixels, displaymetrics.heightPixels);
        Bitmap bmHalf = Bitmap.createScaledBitmap(bitmap, w,
                h, false);
        player = new Player(bmHalf, joystick, displaymetrics.widthPixels/2, displaymetrics.heightPixels/2, rect);
       //
        background = new Background(bitmapBackFirst,  0, 0, rectBackFirst);
        background1 = new Background(bitmapBackSecond,  0, bitmapBackFirst.getHeight(), rectBackSecond);

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
                    Bitmap bitmapTorpedo =BitmapFactory.decodeResource(getResources(), R.drawable.player_run);
                    Rect rectTorpedo = new Rect(0, 0, 90, 300);
                    Torpedo torpedo = new Torpedo(bitmapTorpedo, player.getX(), player.getY(), event.getX(), event.getY(), rectTorpedo);
                    torpedoList.add(torpedo);
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystick.setIsPressed(true);
                } else {
                    torpedoList.add(player.shoot(event, player.getX(), player.getY()));
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
            String TAG = "MyApp";
            int viewWidth = getHolder().getSurfaceFrame().right; // получаем размеры экрана на всякий случай
            int viewHeight = getHolder().getSurfaceFrame().bottom;
            Log.i(TAG, ""+viewHeight+" "+viewWidth);
            while (work){
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.BLUE);
                background.update(1, viewHeight);
                background.draw(canvas);
                background1.update(1,viewHeight);
                background1.draw(canvas);
                joystick.update();
                for (int i=0; i<torpedoList.size(); i++){
                    Log.i("LofUpdate", "update");
                    torpedoList.get(i).update();
                }
                player.update(1);
                for (int i=0; i<torpedoList.size(); i++){
                    torpedoList.get(i).draw(canvas);
                    Log.i("LofUpdate", "draw");
                }
                player.draw(canvas);
                joystick.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
