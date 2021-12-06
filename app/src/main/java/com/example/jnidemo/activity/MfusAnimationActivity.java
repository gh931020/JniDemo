package com.example.jnidemo.activity;

import android.view.View;
import android.widget.ImageView;

import com.example.jnidemo.R;
import com.example.jnidemo.view.AnimDrawable;
import com.example.jnidemo.view.AnimView;
import com.example.jnidemo.view.WifiView;

public class MfusAnimationActivity extends BaseActivity{

    private WifiView mWifiIv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mfus;
    }
    private AnimView mAnimView;

    @Override
    protected void initView() {
        // AnimDrawable animDrawable = new AnimDrawable();
        // findViewById(R.id.ivAnim).setBackground(animDrawable);

        mAnimView = findViewById(R.id.avCircle);

        findViewById(R.id.btnAdd).setOnClickListener(v->{
            mAnimView.addShow();
        });

        findViewById(R.id.btnStop).setOnClickListener(v->{
            mAnimView.stopAnim();
        });

        mWifiIv = findViewById(R.id.wifiIv);
        mWifiIv.registReceiver();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWifiIv.unRegistReceiver();
    }
}
