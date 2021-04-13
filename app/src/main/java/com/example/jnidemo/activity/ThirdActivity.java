package com.example.jnidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jnidemo.R;
import com.example.jnidemo.util.OrderUtil;

import java.util.Arrays;
import java.util.List;
/**
 * @author : zhenjieZhang
 * @date : 2021/4/13
 * @apiNote : 页面三,用于接收发送指令
 **/
public class ThirdActivity extends BaseActivity {
    private static final String TAG = ThirdActivity.class.getName();
    private ImageButton backIbtn;

    private Button receiveBtn, sendBtn,dataBtn, cleanBtn;
    private TextView msgTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_third;
    }

    @Override
    protected void initView() {
        backIbtn = findViewById(R.id.backIbtn);

        receiveBtn = findViewById(R.id.receiveBtn);
        sendBtn = findViewById(R.id.sendBtn);
        dataBtn = findViewById(R.id.dataBtn);
        msgTv = findViewById(R.id.msgTv);
        cleanBtn = findViewById(R.id.cleanBtn);
    }

    @Override
    protected void setListener() {
        backIbtn.setOnClickListener(v -> ThirdActivity.this.finish());

        //接收指令
        receiveBtn.setOnClickListener(v -> new Thread(this::startReceive).start());

        dataBtn.setOnClickListener(v -> new Thread(this::getOrderData).start());

        sendBtn.setOnClickListener(v -> new Thread(()->{
            char[] orders = getSendOrder();
            runOnUiThread(()->{
                StringBuilder sb = new StringBuilder();
                String[] order = getString(orders);
                sb.append("发送: ");
                for (int i = 0; i < order.length; i++) {
                    sb.append(order[i].toUpperCase() + " ");
                }
                msgTv.append("\r\n"+ sb.toString());
            });
            sendOrder(orders, 4);
        }).start());

        cleanBtn.setOnClickListener(v -> msgTv.setText(""));
    }

    /**
     * 获取发送数据
     * @return 发送数据数组char[]
     */
    private char[] getSendOrder() {
        return new char[]{0x02, 0x01, 0x01, 0x02};
    }

    /**
     * 接收指令
     */
    private synchronized void receiveOrder(char[] data) {

        runOnUiThread(()->{
            StringBuilder sb = new StringBuilder();
            String[] order = getString(data);
            sb.append("收到指令数据: ");
            for (int i = 0; i < order.length; i++) {
                sb.append(order[i].toUpperCase() + " ");
            }
            msgTv.append("\r\n"+ sb.toString());
        });
    }

    @Override
    protected void initData() {
        Log.i(TAG, "initData: HexString --> "+Integer.toHexString(30));
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ThirdActivity.class);
        context.startActivity(starter);
    }

    /**
     * 开始接收指令
     */
    public native void startReceive();

    public native void getOrderData();

    public native void sendOrder(char[] chars, int length);


    /**
     * 将char转换为十六进制字符串
     * @param chars
     * @return 十六进制字符串
     */
    public static String[] getString(char[] chars) {
        String[] order = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            order[i] = Integer.toHexString(chars[i]);
        }
        return order;
    }
}
