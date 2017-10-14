package com.orz.hachcat.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.orz.hachcat.R;


public class MainActivity extends AppCompatActivity {


    private Button go_funciton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        go_funciton = (Button) findViewById(R.id.go_function);


        go_funciton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent k = new Intent(MainActivity.this, FunctionActivity.class);
                    startActivity(k);
                } catch(Exception e) {
                }
            }
        });


    }


}
