package com.base;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.manager.ScreenLightManager;


/**
 * @desc 语音播放基类,主要实现靠近耳朵时屏幕变暗
 */
public abstract class BasePlayAudioActivity extends BaseActivity {
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    private SensorEventListener sensorEventListener;
    private ScreenLightManager screenLightManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAudioSensor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener, sensor);
        screenLightManager.clear();
    }

    /**
     * @Description 初始化AudioManager，用于访问控制音量和钤声模式
     */
    private void initAudioSensor() {
        screenLightManager = ScreenLightManager.getInstance(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                AudioPlayManager.getInstance().sensorChanged(BasePlayAudioActivity.this, sensor, sensorEvent,screenLightManager);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
