package com.example.jnidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jnidemo.R;
import com.example.jnidemo.util.Unit;
import com.example.jnidemo.view.SuitLines;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class SuitActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, SuitActivity.class);
        context.startActivity(starter);
    }

    private boolean add = true;

    private final Handler myHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (add){
                units.add(new Unit(new SecureRandom().nextInt(50), ""));

                if (units.size() >= 20){
                    units = units.subList(units.size() - 20, units.size());
                }

                mSuitLines.feed(units);
            }
            sendEmptyMessageDelayed(0, 1000);
        }
    };

    private SuitLines mSuitLines;

    List<Unit> units = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suit);

        mSuitLines = findViewById(R.id.suitLines);
        mSuitLines.setLineSize(3f);
        myHandler.sendEmptyMessage(0);

        findViewById(R.id.button).setOnClickListener(v ->{
            add = !add;
        });
    }
}
