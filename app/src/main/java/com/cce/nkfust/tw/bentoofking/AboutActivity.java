package com.cce.nkfust.tw.bentoofking;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        imgLogo = findViewById(R.id.imgLogo);
        Handler handler = new Handler();
        imgLogo.setOnClickListener(handler);
    }
    class Handler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
