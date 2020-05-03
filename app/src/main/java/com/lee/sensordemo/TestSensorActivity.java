package com.lee.sensordemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class TestSensorActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView mTextViewAcceleration;
    private TextView mTextViewLinearAcceleration;
    private TextView mTextViewGravity;
    private TextView mTextViewGyroscope;
    private TextView mTextViewMagneticField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sensor);

        mTextViewAcceleration = findViewById(R.id.accelerometer_tv);
        mTextViewLinearAcceleration = findViewById(R.id.linear_accelerometer_tv);
        mTextViewGravity = findViewById(R.id.gravity_tv);
        mTextViewGyroscope = findViewById(R.id.gyroscope_tv);
        mTextViewMagneticField = findViewById(R.id.magnetic_field_tv);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  // 获取SensorManager服务
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 为加速度传感器注册监听器
        mSensorManager.registerListener(TestSensorActivity.this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                                        SensorManager.SENSOR_DELAY_UI);
        // 为线性加速度传感器注册监听器
        mSensorManager.registerListener(TestSensorActivity.this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                                        SensorManager.SENSOR_DELAY_UI);
        // 为重力加速度传感器注册监听器
        mSensorManager.registerListener(TestSensorActivity.this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                                        SensorManager.SENSOR_DELAY_UI);
        // 为陀螺仪传感器注册监听器
        mSensorManager.registerListener(TestSensorActivity.this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                                        SensorManager.SENSOR_DELAY_UI);
        // 为电磁场传感器注册监听器
        mSensorManager.registerListener(TestSensorActivity.this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                                        SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册所有传感器
        mSensorManager.unregisterListener(TestSensorActivity.this);
    }

    // 当传感器的值改变时回调此方法
    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder str = new StringBuilder();
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                str.append("X方向上的加速度：");
                str.append(event.values[0]);
                str.append("\nY方向上的加速度：");
                str.append(event.values[1]);
                str.append("\nZ方向上的加速度：");
                str.append(event.values[2]);
                mTextViewAcceleration.setText(str);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                str.append("X方向上的线性加速度：");
                str.append(event.values[0]);
                str.append("\nY方向上的线性加速度：");
                str.append(event.values[1]);
                str.append("\nZ方向上的线性加速度：");
                str.append(event.values[2]);
                mTextViewLinearAcceleration.setText(str);
                break;
            case Sensor.TYPE_GRAVITY:
                str.append("X方向上的重力加速度：");
                str.append(event.values[0]);
                str.append("\nY方向上的重力加速度：");
                str.append(event.values[1]);
                str.append("\nZ方向上的重力加速度：");
                str.append(event.values[2]);
                mTextViewGravity.setText(str);
                break;
            case Sensor.TYPE_GYROSCOPE:
                str.append("绕X轴旋转的角速度：");
                str.append(event.values[0]);
                str.append("\n绕Y轴旋转的角速度：");
                str.append(event.values[1]);
                str.append("\n绕Z轴旋转的角速度：");
                str.append(event.values[2]);
                mTextViewGyroscope.setText(str);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                str.append("X方向上的磁感应强度：");
                str.append(event.values[0]);
                str.append("\nY方向上的磁感应强度：");
                str.append(event.values[1]);
                str.append("\nZ方向上的磁感应强度：");
                str.append(event.values[2]);
                mTextViewMagneticField.setText(str);
                break;
        }
    }

    // 当传感器精度改变时回调此方法
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}