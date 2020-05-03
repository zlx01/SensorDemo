package com.lee.sensordemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class TestOrientation extends Activity {
    private SensorManager mSensorManager;
    private MySensorEventListener mListener;
    private TextView mTextViewOrientation1;
    private TextView mTextViewOrientation2;

    private float[] gravityValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private float[] orientationValues = new float[3];
    private float[] rotationMatrix = new float[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);

        mTextViewOrientation1 = findViewById(R.id.orientation_tv1);
        mTextViewOrientation2 = findViewById(R.id.orientation_tv2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  // 获取SensorManager服务
        mListener = new MySensorEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                                        SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                                        SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                                        SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册所有传感器
        mSensorManager.unregisterListener(mListener);
    }

    class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    StringBuilder str = new StringBuilder();
                    str.append("绕Z轴旋转的角度/方位角：");
                    str.append(event.values[0]);
                    str.append("\n绕X轴旋转的角度/倾斜角：");
                    str.append(event.values[1]);
                    str.append("\n绕Y轴旋转的角度/滚动角：");
                    str.append(event.values[2]);
                    mTextViewOrientation1.setText(str);
                    break;
                case Sensor.TYPE_GRAVITY:
                    gravityValues = event.values;
                    update();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticFieldValues = event.values;
                    update();
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        private void update() {
            SensorManager.getRotationMatrix(rotationMatrix, null,
                                            gravityValues, magneticFieldValues);
            SensorManager.getOrientation(rotationMatrix, orientationValues);
            orientationValues[0] = (float) Math.toDegrees(orientationValues[0]);
            orientationValues[1] = (float) Math.toDegrees(orientationValues[1]);
            orientationValues[2] = (float) Math.toDegrees(orientationValues[2]);
            StringBuilder str = new StringBuilder();
            str.append("绕Z轴旋转的角度/方位角：");
            str.append(orientationValues[0]);
            str.append("\n绕X轴旋转的角度/倾斜角：");
            str.append(orientationValues[1]);
            str.append("\n绕Y轴旋转的角度/滚动角：");
            str.append(orientationValues[2]);
            mTextViewOrientation2.setText(str);
        }
    }
}