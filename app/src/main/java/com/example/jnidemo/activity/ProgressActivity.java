package com.example.jnidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jnidemo.R;
import com.example.jnidemo.view.ArcSeekBar;
import com.example.jnidemo.view.ArcSeekBarPlus;
import com.example.jnidemo.view.DashBoardProgressView;

public class ProgressActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ProgressActivity.class);
        context.startActivity(starter);
    }
    private ArcSeekBarPlus arcSeekBar ;
    private EditText valueEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        arcSeekBar = findViewById(R.id.arcSeekBar1);

        valueEt = findViewById(R.id.valueEt);

        findViewById(R.id.addBtn).setOnClickListener(v->{
            arcSeekBar.addProcess();
        });
        findViewById(R.id.reduceBtn).setOnClickListener(v->{
            arcSeekBar.reduceProcess();
        });

        findViewById(R.id.ensureBtn).setOnClickListener(v->{
            String value = valueEt.getText().toString();
            arcSeekBar.setProgressByValue(Integer.parseInt(value));
        });

        arcSeekBar.setOnChangeListener(new ArcSeekBarPlus.OnChangeListener() {
            @Override
            public void onStartTrackingTouch(boolean isCanDrag) {

            }

            @Override
            public void onProgressChanged(float progress, float max, int currentValue, boolean fromUser) {
                valueEt.setText(currentValue+"");
            }

            @Override
            public void onStopTrackingTouch(boolean isCanDrag) {

            }

            @Override
            public void onSingleTapUp() {

            }
        });

    }
}
