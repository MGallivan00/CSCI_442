package com.example.facialrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TTS tts;
    String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button practice = findViewById(R.id.Tutorial);
        practice.setOnClickListener(this);

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.Tutorial:
                Toast.makeText(this,"Hello", Toast.LENGTH_LONG).show();
                startSecondActivity();
                break;
        }
    }

    public void startSecondActivity(){
        Intent secondActivity = new Intent(this, TalkActivity.class);
        startActivity(secondActivity);
//        TTS talk = TTS.getInstance(this);
//        talk.start();

//        Bundle b = new Bundle();
//        b.putString("LM", "Test");
//        if (tts.handler != null){
//            Message msg = tts.handler.obtainMessage(0);
//            msg.setData(b);
//            tts.handler.sendMessage(msg);
//        }
    }

    protected void onResume(){
        super.onResume();
    }

    protected void onDelete(){
        super.onDestroy();
    }
}
