package com.lee.sensordemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ListAllSensorsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_sensors);

        // 从系统服务中获得传感器管理器
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        // 保存每个传感器的信息
        StringBuilder sensorsInfo = new StringBuilder("手机有" + allSensors.size() + "个传感器，分别是：\n\n");
        for (Sensor s : allSensors) {
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    sensorsInfo.append("加速度传感器");
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    sensorsInfo.append("线性加速度传感器");
                    break;
                case Sensor.TYPE_GRAVITY:
                    sensorsInfo.append("重力传感器");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sensorsInfo.append("方向传感器");
                    break;
                case Sensor.TYPE_MOTION_DETECT:
                    sensorsInfo.append("运动状态检测传感器");
                    break;
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    sensorsInfo.append("剧烈运动检测传感器");
                    break;
                case Sensor.TYPE_STATIONARY_DETECT:
                    sensorsInfo.append("静止状态检测传感器");
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    sensorsInfo.append("步数计数器");
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    sensorsInfo.append("步伐检测器");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sensorsInfo.append("陀螺仪传感器");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sensorsInfo.append("电磁场传感器");
                    break;
                case Sensor.TYPE_LIGHT:
                    sensorsInfo.append("环境光线传感器");
                    break;
                case Sensor.TYPE_PRESSURE:
                    sensorsInfo.append("压力传感器");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sensorsInfo.append("距离传感器");
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sensorsInfo.append("温度传感器");
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    sensorsInfo.append("相对湿度传感器");
                    break;
                default:
                    sensorsInfo.append("未知传感器");
                    break;
            }
            sensorsInfo.append("\n设备类型码：").append(s.getType())
                    .append("\n设备名称：").append(s.getName())
                    .append("\n设备版本：").append(s.getVersion())
                    .append("\n供应商：").append(s.getVendor())
                    .append("\n\n");
        }
        // 显示所有传感器的信息
        TextView textView = findViewById(R.id.sensors_list_tv);
        textView.setText(sensorsInfo);
    }
}