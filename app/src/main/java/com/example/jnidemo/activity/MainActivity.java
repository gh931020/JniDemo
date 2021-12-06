package com.example.jnidemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.jnidemo.R;
import com.example.jnidemo.service.TemperatureService;
import com.example.jnidemo.util.Constans;
/**
 * @author : zhenjieZhang
 * @date : 2021/4/7
 * @apiNote :
 **/
public class MainActivity extends BaseActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = MainActivity.class.getName();

    private ImageButton firstIbtn;
    private ImageButton secondIbtn;
    private ImageButton thirdIbtn;
    private Button progressBarBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        firstIbtn = findViewById(R.id.firstIbtn);
        secondIbtn = findViewById(R.id.secondIbtn);
        thirdIbtn = findViewById(R.id.thirdIbtn);
        progressBarBtn = findViewById(R.id.progressBtn);
    }

    @Override
    public void setListener() {
        firstIbtn.setOnClickListener(v -> FirstActivity.start(MainActivity.this));
        secondIbtn.setOnClickListener(v -> SecondActivity.start(MainActivity.this));
        thirdIbtn.setOnClickListener(v -> ThirdActivity.start(MainActivity.this));
        progressBarBtn.setOnClickListener(v -> ProgressActivity.start(MainActivity.this));

    }

    @Override
    protected void initData() {

    }


}