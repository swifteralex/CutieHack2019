package com.example.cutiehack2019;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

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

    private ImageView[] trash = new ImageView[10];
    private ImageView[] walls = new ImageView[100];
    //private ImageView turtle = findViewById(R.id.turtle);

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

    public void gameOver() {
        
    }

    public void createLevel() {
        trash[0] = findViewById(R.id.trash1);
        trash[1] = findViewById(R.id.trash2);
        trash[2] = findViewById(R.id.trash3);
        trash[3] = findViewById(R.id.trash4);
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

            ImageView turtle = findViewById(R.id.turtle);

            if(turtle.getX() + vx < 0 || turtle.getX() + vx + turtleDiameter > screenWidth){
                vx *= -0.5;
            }
            if(turtle.getY() + vy < 0 || turtle.getY() + vy + turtleDiameter > screenHeight) {
                vy *= -0.5;
            }
            for(int i=0; i<4; i++) {
                if (turtle.getX() + turtleDiameter >= trash[i].getX() && turtle.getX() <= trash[i].getX() + 80){
                    if (turtle.getY() + turtleDiameter >= trash[i].getY() && turtle.getY() <= trash[i].getY() + 80){
                        vx *= 0;
                        vy *= 0;
                    }
                }
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
