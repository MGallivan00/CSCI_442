package com.example.facialrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TTS tts;
    String msg;
    private TextView tv;
    private List<Face> fac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button practice = findViewById(R.id.talkButton);
        practice.setOnClickListener(this);
        tv = findViewById(R.id.textView);
        InputStream stream = getResources().openRawResource(R.raw.image02);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        FaceDetector detector = FaceDetection.getClient();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.talkButton:
                Task<List<Face>> result =
                        detector.process(image).addOnSuccessListener(

                                new OnSuccessListener<List<face>>(){
                                    @Override
                                    public void onSuccess(List<Face> faces){
                                        fac = faces;
                                        if(fac == null)
                                            Log.v("***DRAW***", "Null");
                                        FaceView overlay = (FaceView) findViewById(R.id.faceView);
                                        overlay.setContent(bitmap, fac);
                                        //Success
                                        tv.setText(faces.size() + "Faces Seen");
                                        startSecondActivity();
                                    }
                                }
                        );
                break;
        }
    }

    public void startSecondActivity(){
        TTS talk = TTS.getInstance(this);
        talk.start();

        Bundle b = new Bundle();
        b.putString("LM", tv.toString());
        if (tts.handler != null){
            Message msg = tts.handler.obtainMessage(0);
            msg.setData(b);
            tts.handler.sendMessage(msg);
        }
    }

    protected void onResume(){
        super.onResume();
    }

    protected void onDelete(){
        super.onDestroy();
    }
}