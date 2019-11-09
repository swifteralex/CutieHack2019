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

    private ImageView[] trash = new ImageView[10];
    private ImageView[] walls = new ImageView[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

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

        int startingX = 0;
        int startingY = 0;
        vx = 0;
        vy = 0;

        turtle.setX(startingX);
        turtle.setY(startingY);

        _gameOver = false;
    }

    public void gameOver() {
        _gameOver = true;
    }

    public void win() {
        _gameOver = true;
    }

    public void createLevel() {
        trash[0] = findViewById(R.id.trash1);
        trash[1] = findViewById(R.id.trash2);
        trash[2] = findViewById(R.id.trash3);
        trash[3] = findViewById(R.id.trash4);
        trash[4] = findViewById(R.id.trash5);
        trash[5] = findViewById(R.id.trash6);
        trash[6] = findViewById(R.id.trash7);
        trash[7] = findViewById(R.id.trash9);


        walls[0] = findViewById(R.id.wall1);
        walls[1] = findViewById(R.id.wall2);
        walls[2] = findViewById(R.id.wall3);
        walls[3] = findViewById(R.id.wall4);
        walls[4] = findViewById(R.id.wall5);
        walls[5] = findViewById(R.id.wall6);
        walls[6] = findViewById(R.id.wall7);
        walls[7] = findViewById(R.id.wall8);
        walls[8] = findViewById(R.id.wall9);
        walls[9] = findViewById(R.id.wall10);
        walls[10] = findViewById(R.id.wall11);
        walls[11] = findViewById(R.id.wall12);
        walls[12] = findViewById(R.id.wall13);
        walls[13] = findViewById(R.id.wall14);
        walls[14] = findViewById(R.id.wall15);
        walls[15] = findViewById(R.id.wall16);
        walls[16] = findViewById(R.id.wall17);
        walls[17] = findViewById(R.id.wall18);
        walls[18] = findViewById(R.id.wall19);
        walls[19] = findViewById(R.id.wall20);
        walls[20] = findViewById(R.id.wall21);
        walls[21] = findViewById(R.id.wall24);
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
            for(int i=0; i<8; i++) {
                if (turtle.getX() + turtleDiameter >= trash[i].getX() && turtle.getX() <= trash[i].getX() + 80){
                    if (turtle.getY() + turtleDiameter >= trash[i].getY() && turtle.getY() <= trash[i].getY() + 80) {
                        if (vx != 0) {
                            gameOver();
                        }
                    }
                }
            }

            try {
                int threshold = 10;
                for (int i = 0; i < 22; i++) {
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
