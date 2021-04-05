package com.example.facialrecognition;



import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import android.widget.Toast;

import java.util.Locale;

public class TTS extends Thread implements TextToSpeech.OnInitListener {

    Context con;
    public Handler handler;
    public static TTS t;
    private TextToSpeech tts;
    String last = "";

    public static TTS getInstance(Context c) {
        if (t == null)
            t = new TTS(c);
        else
            t.con = c;
        return t;
    }


    private TTS(Context con) {
        this.con = con;
        tts = new TextToSpeech(con, this);
    }


    @Override
    public void run(){
        Looper.prepare();
        handler = new Handler() {
            public void handleMessage(Message msg){
                String Response = msg.getData().getString("LM");
                speakOut(Response);
                Toast.makeText(con, "Talking... " + Response, Toast.LENGTH_LONG).show();
            }
        };
        Looper.loop();
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(con, "Language is not supported", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(con, "Initialization Failed", Toast.LENGTH_SHORT).show();
        }

    }
    public void speakOut(String text) {
        Log.v("******SPEECH*******", text);

        if(last != text) {
            last = text;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            while (tts.isSpeaking()) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {

                }
            }
        }
    }
}
