package com.decorista.anas.decorista;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import java.util.Random;

public class SplashScreen extends AppCompatActivity {

    Thread splashTread;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = (ImageView)findViewById(R.id.imageViewSplash);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        splashTread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                    }
                }
                catch(InterruptedException ex){
                }
                finish();

                Intent intent = new Intent();
                intent.setClass(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };

        splashTread.start();

    }




}
