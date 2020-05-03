package com.lee.sensordemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestAcceleration extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private SQLiteDatabase mSQLiteDatabase;
    private SharedPreferences mSharedPreferences;    // 保存IP地址和端口号的文件
    private SharedPreferences.Editor editor;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);
        mSharedPreferences = getSharedPreferences("ip_setting", MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        findViewById(R.id.start_sample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDBFile();
                Toast.makeText(TestAcceleration.this, "开始采集传感器数据", Toast.LENGTH_LONG).show();
                // 为加速度传感器注册监听器
                mSensorManager.registerListener(TestAcceleration.this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
                // 为线性加速度传感器注册监听器
                mSensorManager.registerListener(TestAcceleration.this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
                // 为磁场传感器注册监听器
                mSensorManager.registerListener(TestAcceleration.this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
                // 为方向传感器注册监听器
                // mSensorManager.registerListener(TestAcceleration.this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
            }
        });
        findViewById(R.id.stop_sample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.unregisterListener(TestAcceleration.this);
                uploadData();
                mSQLiteDatabase.close();
            }
        });
        findViewById(R.id.ip_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IpSetting();
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    private void IpSetting() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.ip_setting, null);
        new AlertDialog.Builder(TestAcceleration.this).setTitle("设置IP地址和端口号").setView(tableLayout)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 注意调用findViewById的View是tableLayout，不能省略，否则会奔溃！
                        EditText et1 = tableLayout.findViewById(R.id.ip);
                        EditText et2 = tableLayout.findViewById(R.id.port);
                        String ip = et1.getText().toString();
                        String PORT = et2.getText().toString();
                        editor.putString("IP", ip);
                        editor.putString("PORT", PORT);
                        editor.commit();
                        Toast.makeText(TestAcceleration.this, "设置成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TestAcceleration.this, "您取消了操作", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mCount++;    // 采样次数加一
        mSQLiteDatabase.beginTransaction(); // 开启事务，加快存储速度
        ContentValues contentValues = new ContentValues();
        contentValues.put("count", mCount);
        contentValues.put("sensorSampleTime", event.timestamp);
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                contentValues.put("accX", event.values[0]);
                contentValues.put("accY", event.values[1]);
                contentValues.put("accZ", event.values[2]);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                contentValues.put("linear_accX", event.values[0]);
                contentValues.put("linear_accY", event.values[1]);
                contentValues.put("linear_accZ", event.values[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                contentValues.put("magnetX", event.values[0]);
                contentValues.put("magnetY", event.values[1]);
                contentValues.put("magnetZ", event.values[2]);
                break;
            /*case Sensor.TYPE_ORIENTATION:
                contentValues.put("oriX", event.values[0]);
                contentValues.put("oriY", event.values[1]);
                contentValues.put("oriZ", event.values[2]);
                break;*/
        }


        mSQLiteDatabase.insert("sensorInfo", null, contentValues);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void createDBFile() {
        try {
            // 先判断是否有SD卡以及是否有读取SD卡的权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + "sensor_info.db";
                mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileName, null);
                mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS sensorInfo");
                mSQLiteDatabase.execSQL("CREATE TABLE sensorInfo(count INTEGER, sensorSampleTime VARCHAR," +
                                                "accX FLOAT,accY FLOAT,accZ FLOAT," +
                                                "linear_accX FLOAT,linear_accY FLOAT,linear_accZ FLOAT," +
                                                "magnetX FLOAT,magnetY FLOAT, magnetZ FLOAT," +
                                                "oriX FLOAT,oriY FLOAT,oriZ FLOAT)");
            } else {
                Toast.makeText(TestAcceleration.this, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 上传信息到服务器
    private void uploadData() {
        final String IP = mSharedPreferences.getString("IP", null);
        final String PORT = mSharedPreferences.getString("PORT", null);
        if (IP != null && PORT != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        // 建立连接到远程服务器端的Socket
                        Socket socket = new Socket(IP, Integer.valueOf(PORT));
                        String fileName = "sensor_info.db";
                        String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + fileName;
                        File file = new File(filePath);
                        FileInputStream fis = new FileInputStream(file);
                        OutputStream out = socket.getOutputStream();

                        String file_info = fileName + "|" + file.length();
                        out.write(file_info.getBytes("utf-8"));

                        byte[] buf = new byte[1024];
                        while (fis.read(buf) != -1) {
                            out.write(buf);
                        }
                        socket.close();
                        showToastMsg("数据上传成功！");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastMsg("网络通信失败");
                    }
                }
            }.start();
        } else {
            showToastMsg("请先设置IP地址和端口号");
        }
    }

    private void showToastMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TestAcceleration.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}