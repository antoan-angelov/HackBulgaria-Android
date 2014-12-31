package com.hackbulgaria.antoan.flappy;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hackbulgaria.antoan.flappy.managers.AudioManager;
import com.hackbulgaria.antoan.flappy.views.DrawingView;


public class GameActivity extends FragmentActivity {
    private DrawingView mDrawingView;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mDrawingView = (DrawingView) findViewById(R.id.drawing);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        mDrawingView.resume();
        mAudioManager = new AudioManager(this);
        mDrawingView.setAudioManager(mAudioManager);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDrawingView.pause(true);

        mAudioManager.dispose();
        mAudioManager = null;
    }
}
