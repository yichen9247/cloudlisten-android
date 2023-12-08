package com.cloudlisten.music;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.simple.spiderman.SpiderMan;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            SpiderMan.show(e);
        }
    }

}
