package com.example.linkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nttdocomo.android.sdaiflib.DeviceInfo;
import com.nttdocomo.android.sdaiflib.GetDeviceInformation;
import com.nttdocomo.android.sdaiflib.SendOther;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 画面を描画する。
        setView();
    }

    private void setView() {
        // リニアレイアウトを描画する。
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);
        // ボタンを描画する。
        Button sendButton = new Button(this);
        sendButton.setText("LED3");
        linearLayout.addView(sendButton, new LinearLayout.LayoutParams(300, 300));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 信号を送信する。
                sendMessage();
            }
        });
    }

    static final byte LINKING_IF_VIB_PATTERN_ID = 0x10;
    static final byte LINKING_IF_LED_PATTERN_ID = 0x20;
    static final byte LINKING_IF_DURATION_ID = 0x10;

    private void sendMessage() {
        // Linkingデバイス情報を取得する。
        GetDeviceInformation deviceInformation = new GetDeviceInformation(this);
        List<DeviceInfo> deviceInfos = deviceInformation.getInformation();
        if (deviceInfos.size() == 0) {
            Toast.makeText(this, "ペアリングされたデバイスがありません", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "ペアリングされたデバイスが" + deviceInfos.size() + "個あります", Toast.LENGTH_SHORT).show();
        }
        for (DeviceInfo deviceInfo : deviceInfos) {
            // 送信情報を設定する。
            SendOther sendOther = new SendOther(this);
            // 送信先デバイスIDを設定する。
            sendOther.setDeviceID(deviceInfo.getModelId());
            System.out.print(deviceInfo.getModelId());
            // 振動パターンを設定する。（振動PATTERN2＝35）
            sendOther.setVibration(new byte[]{LINKING_IF_VIB_PATTERN_ID, 37});

            // LEDパターンを設定する。（振動PATTERN2＝35）
            sendOther.setIllumination(new byte[] {LINKING_IF_LED_PATTERN_ID, 35});

            // 動作時間を設定する。（振動時間5秒＝33）
            sendOther.setDuration(new byte[]{LINKING_IF_DURATION_ID, 33});

            sendOther.send();
        }
    }
}
