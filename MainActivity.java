package com.example.facialrecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import java.io.InputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<Face> fac;
    TextView tv;
    int count = 0;
    TTS tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        Button talkButton = findViewById(R.id.talkButton);
        talkButton.setOnClickListener(this);
        tts = TTS.getInstance(this);
        tts.start();

//        InputStream stream = getResources().openRawResource(R.raw.image01);
//        InputStream stream = getResources().openRawResource(R.raw.image02);
//        InputStream stream = getResources().openRawResource(R.raw.images05);
        InputStream stream = getResources().openRawResource(R.raw.japaneseflowers);



        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetector detector = FaceDetection.getClient();


        Task<List<Face>> result =
                detector.process(image).addOnSuccessListener(

                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                fac = faces;
                                if (fac == null)
                                    Log.v("***DRAW***", "Null");
                                FaceView overlay = (FaceView) findViewById(R.id.faceView);
                                overlay.setContent(bitmap, fac);
                                //Success
                                tv.setText(faces.size() + " Faces Seen");
                                count = faces.size();
//                                startSecondActivity();
                            }
                        }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }


                });
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.talkButton:
                startSecondActivity();
                break;
        }
    }

    public void startSecondActivity(){

        Bundle b = new Bundle();
        b.putString("LM", "There are " + count + " faces in the photo");
        if (tts.handler != null){
            Message msg = tts.handler.obtainMessage(0);
            msg.setData(b);
            tts.handler.sendMessage(msg);
        }
    }

}
