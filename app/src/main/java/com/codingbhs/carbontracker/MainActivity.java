package com.codingbhs.carbontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        start = true;

        final TextView titleText = findViewById(R.id.title_text);
        final Button startBtn = findViewById(R.id.start_btn);

        ImageView deadImage = findViewById(R.id.deadImageView);
        double average = new TableControllerEntries(this).average();
        float opaque = (float)(1 / (1 + Math.exp((float)(-0.35) * (average-19))))*2-1;
        deadImage.setAlpha(opaque);

        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        titleText.setAnimation(uptodown);
        startBtn.setAnimation(downtoup);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
                start = false;
                titleText.setVisibility(View.INVISIBLE);
                startBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        final TextView titleText = findViewById(R.id.title_text);
        final Button startBtn = findViewById(R.id.start_btn);

        if (!start) {
            titleText.setAnimation(null);
            startBtn.setAnimation(null);

            this.recreate();
        }
        else {
            titleText.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.VISIBLE);
        }
    }
}
