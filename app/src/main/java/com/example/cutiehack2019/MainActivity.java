package com.example.cutiehack2019;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
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
    private int turtleDiameter = 100;

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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        vx -= sensorEvent.values[0]/3;
        vy += sensorEvent.values[1]/3;
    }

    public class UpdateFrame extends TimerTask {
        public void run(){
            ImageView circle = findViewById(R.id.imageView);

            if(circle.getX() + vx < 0 || circle.getX() + vx + turtleDiameter > screenWidth){
                vx *= -0.5;
            }
            if(circle.getY() + vy < 0 || circle.getY() + vy + turtleDiameter > screenHeight) {
                vy *= -0.5;
            }

            circle.setX(circle.getX() + vx);
            circle.setY(circle.getY() + vy);
        }
    }
}
