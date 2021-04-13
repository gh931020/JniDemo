package com.example.jnidemo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jnidemo.util.Constans;

import java.util.Random;

public class TemperatureService extends IntentService {
    private static final String TAG = "TemperatureService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TemperatureService() {
        super("temperatureService");
    }

    /**
     * 开启服务后会执行此方法进行数据的处理
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent():" + android.os.Process.myTid());
        if (intent != null) {
            String action = intent.getAction();
            if (Constans.ACTION_FOR_TEMPERATURE.equals(action)) {
                Log.i(TAG, "result: " + action);
                handleTemperature();
            }
        }
    }

    /**
     * 处理温度数据
     */
    private void handleTemperature() {
        double temperature;
        while (true) {
            try {
                //模拟计算耗时
                Thread.sleep(2000);
                temperature = getTemperature();
//                temperature = JTempereature.getTempature();
                Intent intent = new Intent(Constans.ACTION_FOR_TEMPERATURE);
                intent.putExtra(Constans.TEMPERATURE, temperature);
                sendBroadcast(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 随机获取温度值
     * @return 温度值
     */
    private double getTemperature() {
        Random rand = new Random();
        double MAX = 40.00;
        double MIN = 20.00;
        double result = 0;
        result = MIN + (rand.nextDouble() * (MAX - MIN));
        result = (double) Math.round(result * 100) / 100;
        Log.i(TAG, "getTemperature: " + result);
        return result;
    }

    /**
     * 启动服务
     * @param context 上下文
     */
    public static void startMyIntentService(Context context) {
        Intent intent = new Intent(context, TemperatureService.class);
        intent.setAction(Constans.ACTION_FOR_TEMPERATURE);
        context.startService(intent);
    }
}
