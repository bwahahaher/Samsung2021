package com.example.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Joystick joystick;
    private final Player player;
    private final Background background,background1;
    protected SurfaceHolder holder;
    ArrayList<Torpedo> torpedoList;
    ArrayList<Enemy> enemyArrayList;
    Bitmap enemyBitmap, bangB;
    int score =0;
    public Game(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
       torpedoList = new ArrayList<Torpedo>();
        enemyArrayList=new ArrayList<Enemy>();
        joystick = new Joystick(displaymetrics.widthPixels/12*10, displaymetrics.heightPixels/12*10, 70, 40);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_run);


        Bitmap bitmapBackFirst = BitmapFactory.decodeResource(getResources(), R.drawable.map1);
        Bitmap bitmapBackSecond = BitmapFactory.decodeResource(getResources(), R.drawable.map2);
        int w = displaymetrics.widthPixels*100/1920;
        int h = displaymetrics.heightPixels*100/1080;
        enemyBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.player_run);
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap, w,
                h, false);
        Rect rect = new Rect(0, 0, w, h);
        Rect rectBackFirst = new Rect(0, 0, displaymetrics.widthPixels, displaymetrics.heightPixels);
        Rect rectBackSecond = new Rect(0, 0, displaymetrics.widthPixels, displaymetrics.heightPixels);
        Bitmap bmHalf = Bitmap.createScaledBitmap(bitmap, w,
                h, false);
        player = new Player(bmHalf, joystick, displaymetrics.widthPixels/2, displaymetrics.heightPixels/2, rect);
       //
        bangB= BitmapFactory.decodeResource(getResources(), R.drawable.bang);
        bangB = Bitmap.createScaledBitmap(bangB, w,
                h, false);
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

                    if (torpedoList.size()>=10){
                        Toast.makeText(getContext(),"Происходит перезарядка", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        torpedoList.add(player.shoot(event, player.getX(), player.getY()));
                    }
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystick.setIsPressed(true);

                } else {
                    if (torpedoList.size()>=5){
                        Toast.makeText(getContext(),"Происходит перезарядка", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        torpedoList.add(player.shoot(event, player.getX(), player.getY()));
                    }
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
        int time = 0;
        @Override
        public void run() {
            Canvas canvas;
            String TAG = "MyApp";
            int viewWidth = getHolder().getSurfaceFrame().right; // получаем размеры экрана на всякий случай
            int viewHeight = getHolder().getSurfaceFrame().bottom;
            Log.i(TAG, ""+viewHeight+" "+viewWidth);
            int time=0;
            Enemy enemy=new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.player_run), 100,0, new Rect(0, 0, 90, 300));
            while (work){
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.BLUE);
                background.update(1, viewHeight);
                background.draw(canvas);
                background1.update(1,viewHeight);
                background1.draw(canvas);
                joystick.update();
                for (int i=0; i<torpedoList.size(); i++){
                    Torpedo torp = torpedoList.get(i);
                    torp.update();
                    if (torp.getX()==torp.getFx()){
                        torpedoList.remove(i);
                        }
                    else if(torp.getX()>viewWidth||torp.getY()>viewHeight){
                        torpedoList.remove(i);
                    }
                    else if(torp.getX()<0||torp.getY()<0){
                        torpedoList.remove(i);
                    }
                    for (int l=0;l<enemyArrayList.size(); l++) {
                        if (enemyArrayList.get(l).getX() < torp.getX() && enemyArrayList.get(l).getX()+enemyArrayList.get(l).getBitmap().getWidth() > torp.getX()) {
                            if (enemyArrayList.get(l).getY()<torp.getY() && enemyArrayList.get(l).getY()+enemyArrayList.get(l).getBitmap().getHeight()> torp.getY() ) {
                                bangSound();
                                Bitmap bitmasp =  BitmapFactory.decodeResource(getResources(), R.drawable.bang);
                                bitmasp = Bitmap.createScaledBitmap(bitmasp, viewWidth*200/1080,
                                        viewHeight*200/1920, false);
                                Paint paint = new Paint();
                                canvas.drawBitmap(bitmasp, (int)(enemyArrayList.get(l).getX()), (int)(enemyArrayList.get(l).getY()), paint);
                                enemyArrayList.remove(l);
                                torpedoList.remove(i);
                                score+=1;
                                bangSound();




                            }
                        }
                    }
                }
                player.update(1);
                for (int i=0; i<torpedoList.size(); i++){
                    torpedoList.get(i).draw(canvas);
                }
                player.setSide(viewWidth, viewHeight);
                player.draw(canvas);
                joystick.draw(canvas);
                 if (time>=60){
                     enemyArrayList.add(new Enemy(enemyBitmap, Math.random()*viewWidth,0, new Rect(0, 0, 10, 10)));
                     time=0;
                 }
                for (int i=0; i<enemyArrayList.size(); i++){
                    enemyArrayList.get(i).update();
                    if (enemyArrayList.get(i).getX() < player.getX() && enemyArrayList.get(i).getX()+enemyArrayList.get(i).getBitmap().getWidth() > player.getX()) {
                        if (enemyArrayList.get(i).getY() < player.getY() && enemyArrayList.get(i).getY() + enemyArrayList.get(i).getBitmap().getHeight() > player.getY()) {
                            score = 0;
                            bangSound();
                            player.setX(viewWidth / 2);
                            player.setY(viewHeight / 2);
                            enemyArrayList.remove(i);
//                            Paint paint = new Paint();
//                            paint.setColor(Color.BLACK);
//                            paint.setTextSize(200);
//                            canvas.drawText("Поражение", viewWidth/2-200, viewHeight/2, paint);
                        }
                    }
                }
                for (int i=0; i<enemyArrayList.size(); i++){
                    enemyArrayList.get(i).draw(canvas);
                }Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(60);
                canvas.drawText(""+score, 60, viewHeight-60, paint);

                holder.unlockCanvasAndPost(canvas);
                time+=1;

                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void bangSound() {
                    Thread a = new Thread(){
                        public void run(){
                            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bang);
                            mp.start();
                        }
                    };
                    a.start();
        }
    }
}
