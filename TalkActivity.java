package com.example.facialrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class TalkActivity extends AppCompatActivity implements View.OnClickListener {

    private TTS tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);
        Button talkButton = findViewById(R.id.talkButton);
        talkButton.setOnClickListener(this);

        tts = TTS.getInstance(this);
        tts.start();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
            case R.id.talkButton:
                Bundle b = new Bundle();
                b.putString("LM", "Hello");
                if (tts.handler != null) {
                    Message msg = tts.handler.obtainMessage(0);
                    msg.setData(b);
                    tts.handler.sendMessage(msg);
                }
        }
    }
}
