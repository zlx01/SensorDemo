package com.lee.sensordemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    private static final int PERMISSIONS_REQUEST_CODE_READ_WRITE_STORAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRWstoragePermission();
    }

    /**
     * 获取读写内存的权限
     */
    private void getRWstoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CODE_READ_WRITE_STORAGE);
        }
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.list_all_sensors_btn:
                startActivity(new Intent(MainActivity.this, ListAllSensorsActivity.class));
                break;
            case R.id.test_sensor_btn:
                startActivity(new Intent(MainActivity.this, TestSensorActivity.class));
                break;
            case R.id.orientation_btn:
                startActivity(new Intent(MainActivity.this, TestOrientation.class));
                break;
            case R.id.accelerometer_btn:
                startActivity(new Intent(MainActivity.this, TestAcceleration.class));
                break;
            case R.id.test_step_btn:
                startActivity(new Intent(MainActivity.this, TestStepActivity.class));
                break;
        }
    }
}