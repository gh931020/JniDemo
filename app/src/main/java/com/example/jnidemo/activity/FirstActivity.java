package com.example.jnidemo.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jnidemo.R;
import com.example.jnidemo.service.TemperatureService;
import com.example.jnidemo.util.Constans;

/**
 * @author : zhenjieZhang
 * @date : 2021/4/7
 * @apiNote : 页面1
 **/
public class FirstActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getName();
    private Button addBtn;
    private Button subtractBtn;
    private LinearLayout addLl;
    private RadioGroup radioGroup;
    private TextView powerTv;
    private TextView timeTv;
    private TextView depthTv;
    private TextView temperatureTv;
    private ImageButton homeIbtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_first;
    }

    /**
     * 开启服务
     */
    private void startTemperatureService() {
        TemperatureService.startMyIntentService(this);
    }

    @Override
    public void initView() {
        addBtn = findViewById(R.id.addBtn);
        subtractBtn = findViewById(R.id.subtractBtn);
        addLl = findViewById(R.id.addLl);
        powerTv = findViewById(R.id.powerTv);
        timeTv = findViewById(R.id.timeTv);
        depthTv = findViewById(R.id.depthTv);
        radioGroup = findViewById(R.id.radioGroup);
        temperatureTv = findViewById(R.id.temperatureTv);
        homeIbtn = findViewById(R.id.homeIbtn);
    }

    @Override
    protected void initData() {
//注册广播接收温度数据
//        registerBroadcast();
        //开启获取温度的服务
//        startTemperatureService();
        //开启子线程,调用c方法获取温度值
        new Thread() {
            @Override
            public void run() {
                temperatureFromJNI();
            }
        }.start();
    }

    @Override
    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    public void setListener() {
        addBtn.setOnTouchListener((v, event) -> {
            int temp;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.powerRbtn:
                            temp = Integer.parseInt(powerTv.getText().toString());
                            temp++;
                            powerTv.setText(String.valueOf(temp));
                            break;
                        case R.id.timeRbtn:
                            temp = Integer.parseInt(timeTv.getText().toString());
                            temp++;
                            timeTv.setText(String.valueOf(temp));
                            break;
                        case R.id.depthRbtn:
                            float tempf = Float.parseFloat(depthTv.getText().toString());
                            tempf++;
                            depthTv.setText(String.valueOf(tempf));
                            break;
                        default:
                            break;
                    }
                    addLl.setBackgroundResource(R.mipmap.icon_ap);
                    break;
                case MotionEvent.ACTION_BUTTON_PRESS:
                    addLl.setBackgroundResource(R.mipmap.icon_ap);
                    break;
                default:
                    addLl.setBackgroundResource(R.mipmap.icon_ad);
                    break;
            }
            return true;
        });

        subtractBtn.setOnTouchListener((v, event) -> {
            int temp;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.powerRbtn:
                            temp = Integer.parseInt(powerTv.getText().toString());
                            temp--;
                            powerTv.setText(String.valueOf(temp));
                            break;
                        case R.id.timeRbtn:
                            temp = Integer.parseInt(timeTv.getText().toString());
                            temp--;
                            timeTv.setText(String.valueOf(temp));
                            break;
                        case R.id.depthRbtn:
                            float tempf = Float.parseFloat(depthTv.getText().toString());
                            tempf--;
                            depthTv.setText(String.valueOf(tempf));
                            break;
                        default:
                            break;
                    }
                    addLl.setBackgroundResource(R.mipmap.icon_dp);
                    break;
                case MotionEvent.ACTION_BUTTON_PRESS:
                    addLl.setBackgroundResource(R.mipmap.icon_dp);
                    break;
                default:
                    addLl.setBackgroundResource(R.mipmap.icon_ad);
                    break;
            }
            return true;
        });

        homeIbtn.setOnClickListener((v)-> FirstActivity.this.finish());
    }

    /**
     * @param result 显示service返回的结果
     */
    public void handleResult(double result) {
        temperatureTv.setText(getResources().getString(R.string.string_temperature, result));
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constans.ACTION_FOR_TEMPERATURE);
        registerReceiver(temperatureReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mTemperatureThreade != null && mTemperatureThreade.isAlive()){
//            mTemperatureThreade.stop();
//        }
        //停止获取温度数据
        stopFromJNI();
//        unregisterReceiver(temperatureReceiver);
    }

    /**
     * 广播接收器
     */
    private final BroadcastReceiver temperatureReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive ()");
            if (intent.getAction().equals(Constans.ACTION_FOR_TEMPERATURE)) {
                double temperature = intent.getDoubleExtra(Constans.TEMPERATURE, 0);
                Log.i("TAG", "onReceive --temperature:" + temperature);
                handleResult(temperature);
            }
        }
    };

    /**
     * C会调用这个方法进行UI更新
     * @param temperature
     */
    public void setTemperature(double temperature){
        Log.i(TAG, "setTemperature: "+ temperature);
        Log.i(TAG, "setTemperature: thread"+ Thread.currentThread());
        //因为是在子线程中调用的C,因此这里需要回到主线程更新UI
        runOnUiThread(() -> handleResult(temperature));
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, FirstActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }


    /**
     * JNI方法,获取温度值
     */
    public native void temperatureFromJNI();

    public native void stopFromJNI();
}
