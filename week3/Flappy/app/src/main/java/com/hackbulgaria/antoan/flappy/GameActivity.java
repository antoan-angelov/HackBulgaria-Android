package com.hackbulgaria.antoan.flappy;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


public class GameActivity extends Activity {
    private DrawingView mDrawingView;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        mDrawingView = (DrawingView) findViewById(R.id.drawing);
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
