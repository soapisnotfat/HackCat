package com.orz.hackcat.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.orz.hackcat.R;

public class ViewActivity extends AppCompatActivity {

    private TextView view_aud;
    private TextView view_pic;
    private String picture_string = "This is the lecture note:\n";
    private String voice_string = "This is transcription from Prof's lecture:\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        view_aud = (TextView) findViewById(R.id.view_audio);
        view_aud.setMovementMethod(new ScrollingMovementMethod());
        view_pic = (TextView) findViewById(R.id.view_pic);
        view_pic.setMovementMethod(new ScrollingMovementMethod());

        if (backingString.getBackinng_pic() != null) {
            picture_string += backingString.getBackinng_pic();
        }
        if (backingString.getBackinng_aud() != null) {
            voice_string += backingString.getBackinng_aud();
        }

        view_pic.setText(picture_string);
        view_aud.setText(voice_string);
    }
}
