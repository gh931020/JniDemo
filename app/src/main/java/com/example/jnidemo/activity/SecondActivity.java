package com.example.jnidemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jnidemo.R;
import com.example.jnidemo.view.CircleProgressView;
import com.example.jnidemo.view.CountTimeTextView;
import com.example.jnidemo.view.TemperatureView;

import java.text.DecimalFormat;

/**
 * @author : zhenjieZhang
 * @date : 2021/4/7
 * @apiNote : 页面2
 **/
public class SecondActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SecondActivity.class.getName();
    private ImageButton backIbtn;
    private ImageButton powrReduceIbtn;
    private ImageButton powrAddIbtn;
    private ImageButton temperatureReduceIbtn;
    private ImageButton temperatureAddIbtn;
    private CircleProgressView temperatureCpv;
    private CircleProgressView powerCpv;
    private ImageButton timeReduceIbtn;
    private ImageButton timeAddIbtn;
    private SeekBar timeSb;
    private TextView timeTv;
    private CountTimeTextView countTimeTv;
    private ImageButton mbiIbtn;
    private CheckBox standbyCb;
    private int currentProgress = 0;

    private TextView tempTv1, tempTv2, tempTv3, tempTv4, tempTv5, tempTv6, tempTv7, tempTv8;
    private TemperatureView tempView1, tempView2, tempView3, tempView4, tempView5, tempView6, tempView7, tempView8;
    private DecimalFormat mDecimalFormat;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void initView() {
        backIbtn = findViewById(R.id.backIbtn);
        powrReduceIbtn = findViewById(R.id.powrReduceIbtn);
        powrAddIbtn = findViewById(R.id.powrAddIbtn);
        temperatureReduceIbtn = findViewById(R.id.temperatureReduceIbtn);
        temperatureAddIbtn = findViewById(R.id.temperatureAddIbtn);
        temperatureCpv = findViewById(R.id.temperatureCpv);
        powerCpv = findViewById(R.id.powerCpv);

        timeReduceIbtn = findViewById(R.id.timeReduceIbtn);
        timeAddIbtn = findViewById(R.id.timeAddIbtn);
        timeSb = findViewById(R.id.timeSb);
        timeTv = findViewById(R.id.timeTv);
        countTimeTv = findViewById(R.id.countTimeTv);
        countTimeTv.setFormat(true);

        mbiIbtn = findViewById(R.id.mbiIbtn);
        standbyCb = findViewById(R.id.standbyCb);

        tempTv1 = findViewById(R.id.tempTv1);
        tempTv2 = findViewById(R.id.tempTv2);
        tempTv3 = findViewById(R.id.tempTv3);
        tempTv4 = findViewById(R.id.tempTv4);
        tempTv5 = findViewById(R.id.tempTv5);
        tempTv6 = findViewById(R.id.tempTv6);
        tempTv7 = findViewById(R.id.tempTv7);
        tempTv8 = findViewById(R.id.tempTv8);

        tempView1 = findViewById(R.id.tempView1);
        tempView2 = findViewById(R.id.tempView2);
        tempView3 = findViewById(R.id.tempView3);
        tempView4 = findViewById(R.id.tempView4);
        tempView5 = findViewById(R.id.tempView5);
        tempView6 = findViewById(R.id.tempView6);
        tempView7 = findViewById(R.id.tempView7);
        tempView8 = findViewById(R.id.tempView8);


        currentProgress = timeSb.getProgress();
        timeTv.setText(getResources().getString(R.string.string_time, currentProgress));
        //用于格式化double数值,保留1位小数
        mDecimalFormat = new DecimalFormat("#.0");
    }

    @Override
    protected void setListener() {
        backIbtn.setOnClickListener((v -> {
            SecondActivity.this.finish();
        }));

        powrReduceIbtn.setOnClickListener(this);
        powrAddIbtn.setOnClickListener(this);
        temperatureReduceIbtn.setOnClickListener(this);
        temperatureAddIbtn.setOnClickListener(this);
        timeReduceIbtn.setOnClickListener(this);
        timeAddIbtn.setOnClickListener(this);

        timeSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentProgress = progress;
                    timeTv.setText(getResources().getString(R.string.string_time, currentProgress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        standbyCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //点击开始时,根据当前设置的时间启动倒计时
            if (isChecked) {
                buttonView.setText(getResources().getString(R.string.cb_start));
                countTimeTv.setMinute(":");
                countTimeTv.setSecond("");
                countTimeTv.setCountTime(currentProgress * 60);
                countTimeTv.startCount();
            } else {
                //点击待机,关停倒计时
                buttonView.setText(getResources().getString(R.string.cb_stanby));
                countTimeTv.stopCount();
                countTimeTv.clear();
                countTimeTv.setText(getResources().getString(R.string.time_zero));
            }
        });

        countTimeTv.setOnCountOverListener(() -> {
            //倒计时结束,将checkBox状态设置为false
            Log.i(TAG, "onCountOver: ");
            countTimeTv.setText(getResources().getString(R.string.time_zero));
            standbyCb.setChecked(false);
        });
    }

    @Override
    protected void initData() {
        //开启子线程,调用c方法获取温度值
        new Thread() {
            @Override
            public void run() {
                getTemp();
            }
        }.start();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SecondActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.powrReduceIbtn:
                powerCpv.reduceProgress();
                break;
            case R.id.powrAddIbtn:
                powerCpv.addProgress();
                break;
            case R.id.temperatureReduceIbtn:
                temperatureCpv.reduceProgress();
                break;
            case R.id.temperatureAddIbtn:
                temperatureCpv.addProgress();
                break;
            case R.id.timeReduceIbtn:
                if (currentProgress <= 0) {
                    currentProgress = 0;
                } else {
                    currentProgress--;
                }
                timeSb.setProgress(currentProgress);
                timeTv.setText(getResources().getString(R.string.string_time, currentProgress));
                break;
            case R.id.timeAddIbtn:
                if (currentProgress >= timeSb.getMax()) {
                    currentProgress = timeSb.getMax();
                } else {
                    currentProgress++;
                }
                timeSb.setProgress(currentProgress);
                timeTv.setText(getResources().getString(R.string.string_time, currentProgress));
                break;
            default:
                break;
        }
    }

    /**
     * C方法调用返回温度通道和温度值
     *
     * @param num
     * @param temp
     */
    public void setTemp(int num, double temp) {
        //主线程更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleResult(num, temp);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopFromJNI();
    }

    /**
     * 处理温度值
     *
     * @param num  第几个通道
     * @param temp 温度值
     */
    private void handleResult(int num, double temp) {
        int color;
        if (temp > 26 && temp < 35) {
            color = Color.parseColor("#FFCC00");
        } else if (temp >= 35) {
            color = Color.parseColor("#FF6600");
        } else {
            color = Color.parseColor("#00000000");
        }
        String format = mDecimalFormat.format(temp);
        switch (num) {
            case 1:
                tempTv1.setText(format);
                tempView1.setColor(color);
                break;
            case 2:
                tempTv2.setText(format);
                tempView2.setColor(color);
                break;
            case 3:
                tempTv3.setText(format);
                tempView3.setColor(color);
                break;
            case 4:
                tempTv4.setText(format);
                tempView4.setColor(color);
                break;
            case 5:
                tempTv5.setText(format);
                tempView5.setColor(color);
                break;
            case 6:
                tempTv6.setText(format);
                tempView6.setColor(color);
                break;
            case 7:
                tempTv7.setText(format);
                tempView7.setColor(color);
                break;
            case 0:
                tempTv8.setText(format);
                tempView8.setColor(color);
                break;
            default:
                break;
        }
    }

    /**
     * 调用C获取温度值
     */
    public native void getTemp();

    /**
     * 停止C方法,改变循环条件为false
     */
    public native void stopFromJNI();

}