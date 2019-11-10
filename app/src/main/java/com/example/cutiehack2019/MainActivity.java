package com.example.cutiehack2019;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int screenWidth;
    private int screenHeight;

    private float vx = 0;
    private float vy = 0;
    private int turtleDiameter = 80;
    private boolean _gameOver = false;
    private int score = 0;

    private ImageView[] trash = new ImageView[9];
    private ImageView[] walls = new ImageView[22];
    private ImageView[] jellys = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        TextView gameOverText = findViewById(R.id.textView2);
        TextView winnerText = findViewById(R.id.textView3);
        Button gameOverButton1 = findViewById(R.id.button2);
        Button gameOverButton2 = findViewById(R.id.button3);
        winnerText.setX(20000);
        gameOverText.setX(20000);
        gameOverButton1.setX(20000);
        gameOverButton2.setX(20000);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Timer timer = new Timer();
        final int FPS = 60;
        TimerTask updateFrame = new UpdateFrame();
        timer.scheduleAtFixedRate(updateFrame, 0, 1000/FPS);

        createLevel();
    }

    public void retry(View view) {
        ImageView turtle = findViewById(R.id.turtle);
        TextView winnerText = findViewById(R.id.textView3);
        TextView gameOverText = findViewById(R.id.textView2);
        Button gameOverButton1 = findViewById(R.id.button2);
        Button gameOverButton2 = findViewById(R.id.button3);

        int startingX = 0;
        int startingY = 0;
        vx = 0;
        vy = 0;

        turtle.setX(startingX);
        turtle.setY(startingY);

        winnerText.setX(20000);
        gameOverText.setX(20000);
        gameOverButton1.setX(20000);
        gameOverButton2.setX(20000);

        _gameOver = false;
    }

    public void gameOver() {
        _gameOver = true;
        TextView gameOverText = findViewById(R.id.textView2);
        Button gameOverButton1 = findViewById(R.id.button2);
        Button gameOverButton2 = findViewById(R.id.button3);
        gameOverText.setX(1080/2 - 125);
        gameOverButton1.setX(1080/3 - 125);
        gameOverButton2.setX(1080 * 2/3 - 125);

        gameOverText.setY(1920/5);
        gameOverButton1.setY(1920 * 3/5);
        gameOverButton2.setY(1920 * 3/5);
    }

    public void win() {
        _gameOver = true;
        TextView gameOverText = findViewById(R.id.textView3);
        Button gameOverButton1 = findViewById(R.id.button2);
        Button gameOverButton2 = findViewById(R.id.button3);
        gameOverText.setX(1080/2 - 125);
        gameOverButton1.setX(1080/3 - 125);
        gameOverButton2.setX(1080 * 2/3 - 125);

        gameOverText.setY(1920/5);
        gameOverButton1.setY(1920 * 3/5);
        gameOverButton2.setY(1920 * 3/5);
    }
    public void quit(View view){
        System.exit(0);
    }

    public void createLevel() {
        trash[0] = findViewById(R.id.trash1);
        trash[1] = findViewById(R.id.trash2);
        trash[2] = findViewById(R.id.trash3);
        trash[3] = findViewById(R.id.trash4);
        trash[4] = findViewById(R.id.trash5);
        trash[5] = findViewById(R.id.trash6);
        trash[6] = findViewById(R.id.trash7);
        trash[7] = findViewById(R.id.trash8);
        trash[8] = findViewById(R.id.trash9);

//        for(int i = 1; i <= trash.length; i++) {
//            int id = getResources().getIdentifier("trash" + i, "id", getPackageName());
//            trash[i-1] = (ImageView) findViewById(id);
//        }

        for(int i = 1; i <= 22; i++) {
            int id = getResources().getIdentifier("wall" + i, "id", getPackageName());
            walls[i-1] = (ImageView) findViewById(id);
        }
//        walls[0] = findViewById(R.id.wall1);
//        walls[1] = findViewById(R.id.wall2);
//        walls[2] = findViewById(R.id.wall3);
//        walls[3] = findViewById(R.id.wall4);
//        walls[4] = findViewById(R.id.wall5);
//        walls[5] = findViewById(R.id.wall6);
//        walls[6] = findViewById(R.id.wall7);
//        walls[7] = findViewById(R.id.wall8);
//        walls[8] = findViewById(R.id.wall9);
//        walls[9] = findViewById(R.id.wall10);
//        walls[10] = findViewById(R.id.wall11);
//        walls[11] = findViewById(R.id.wall12);
//        walls[12] = findViewById(R.id.wall13);
//        walls[13] = findViewById(R.id.wall14);
//        walls[14] = findViewById(R.id.wall15);
//        walls[15] = findViewById(R.id.wall16);
//        walls[16] = findViewById(R.id.wall17);
//        walls[17] = findViewById(R.id.wall18);
//        walls[18] = findViewById(R.id.wall19);
//        walls[19] = findViewById(R.id.wall20);
//        walls[20] = findViewById(R.id.wall21);
//        walls[21] = findViewById(R.id.wall22);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        vx -= sensorEvent.values[0]/2;
        vy += sensorEvent.values[1]/2;
    }

    public class UpdateFrame extends TimerTask {
        public void run(){

            if(_gameOver){
                return;
            }

            ImageView turtle = findViewById(R.id.turtle);

            if(turtle.getX() + vx < 0 || turtle.getX() + vx + turtleDiameter > screenWidth){
                vx *= -0.5;
            }
            if(turtle.getY() + vy < 0 || turtle.getY() + vy + turtleDiameter > screenHeight) {
                vy *= -0.5;
            }
            if(turtle.getY() + vy > screenHeight - 200){
                win();
            }
            for(int i=0; i < trash.length; i++) {
                if (turtle.getX() + turtleDiameter >= trash[i].getX() && turtle.getX() <= trash[i].getX() + 80){
                    if (turtle.getY() + turtleDiameter >= trash[i].getY() && turtle.getY() <= trash[i].getY() + 80) {
                        if (vx != 0) {
                            gameOver();
                        }
                    }
                }
            }
            for(int i = 0; i < jellys.length; i++) {
                if (turtle.getX() + turtleDiameter >= jellys[i].getX() && turtle.getX() <= jellys[i].getX() + 80){
                    if (turtle.getY() + turtleDiameter >= jellys[i].getY() && turtle.getY() <= jellys[i].getY() + 80) {
                        if (vx != 0) {
                            
                        }
                    }
                }
            }

            try {
                int threshold = 10;
                for (int i = 0; i < walls.length; i++) {
                    if (turtle.getX() + vx + turtleDiameter >= walls[i].getX() && turtle.getX() + vx <= walls[i].getX() + 100) {
                        if (turtle.getY() + vy + turtleDiameter >= walls[i].getY() && turtle.getY() + vy <= walls[i].getY() + 100) {
                            if (turtle.getX() + turtleDiameter <= walls[i].getX() && turtle.getX() + vx + turtleDiameter >= walls[i].getX() ||
                                    turtle.getX() >= walls[i].getX() + 100 && turtle.getX() + vx <= walls[i].getX() + 100) {
                                vx *= -0.5;
                            }
                            if (turtle.getY() + turtleDiameter <= walls[i].getY() && turtle.getY() + vy + turtleDiameter >= walls[i].getY() ||
                                    turtle.getY() >= walls[i].getY() + 100 && turtle.getY() + vy <= walls[i].getY() + 100) {
                                vy *= -0.5;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return;
            }

            turtle.setX(turtle.getX() + vx);
            turtle.setY(turtle.getY() + vy);

            int rotation = (int)((180/Math.PI)*Math.atan(vy/vx));
            if(vx < 0){
                rotation += 180;
            }
            turtle.setRotation(rotation - 90);
        }
    }
}
