package com.reins.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;

    TextView step_text;
    private String TAG = "TAG";
    Integer steps = 0;
    Boolean counter_running = Boolean.FALSE;
    Button change_button;
    Button reset_button;
    Button pause_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        step_text = (TextView)findViewById(R.id.counter_text);
        change_button = (Button)findViewById(R.id.button_change);
        reset_button = (Button)findViewById(R.id.button_stop);
        pause_button = (Button)findViewById(R.id.button_pause);
        startSensor();

    }

    public void counter_change(View view) {
        if (! counter_running) counter_running = true;
        else counter_running = false;
        change_button.setVisibility(View.INVISIBLE);
        reset_button.setVisibility(View.INVISIBLE);
        pause_button.setVisibility(View.VISIBLE);
    }

    public void counter_pause(View view) {
        if (! counter_running) counter_running = true;
        else counter_running = false;
        change_button.setText("GO ON");
        change_button.setVisibility(View.VISIBLE);
        reset_button.setVisibility(View.VISIBLE);
        pause_button.setVisibility(View.INVISIBLE);
    }

    public void reset_counter(View view) {
        counter_running = Boolean.FALSE;
        steps = 0;
        step_text.setText(String.valueOf(steps));
        change_button.setText("Start");
    }

    private void startSensor(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        //Sensor mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //mStepCounterSensor == null ||
        if (mSensorManager == null ||  mStepDetectorSensor == null) {
            throw new UnsupportedOperationException("设备不支持");
        }

        //mSensorManager.registerListener(mSensorEventListener, mStepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorEventListener, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }


    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float stepValid = 0;
            /**
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                step = sensorEvent.values[0];
                Log.d(TAG, "STEP_COUNTER:" + step);
                step_text.setText( String.valueOf(step));
            }
             */
            if(counter_running == Boolean.TRUE){
                // check the stepDetector sensor data is valid or not
                if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                    stepValid = sensorEvent.values[0];
                    Log.d(TAG, "STEP_DETECTOR:" + stepValid);
                    if (stepValid == 1.0) {
                        Log.d(TAG, "一次有效的步行");
                        steps += 1;
                        step_text.setText(String.valueOf(steps));
                    }
                }

            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mSensorEventListener);
    }



}
