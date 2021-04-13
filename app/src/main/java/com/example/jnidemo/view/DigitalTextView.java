package com.example.jnidemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author : zhenjieZhang
 * @date : 2021/4/7
 * @apiNote : 时钟字体TextView
 **/
@SuppressLint("AppCompatCustomView")
public class DigitalTextView extends TextView {
    public DigitalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        String file = "digital.ttf";
        AssetManager assets = context.getAssets();
        Typeface font = Typeface.createFromAsset(assets, file);
        setTypeface(font);
    }
}
