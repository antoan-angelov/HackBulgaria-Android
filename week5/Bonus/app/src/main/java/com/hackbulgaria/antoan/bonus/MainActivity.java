package com.hackbulgaria.antoan.bonus;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private boolean mLighOn = false;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private final Timer timer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;
        PackageManager pm = context.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no mCamera!");
            return;
        }

        mCamera = Camera.open();
        mParameters = mCamera.getParameters();
    }

    @Override
    protected void onResume() {
        super.onResume();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mLighOn) {
                    Log.v("tag", "STARTING FLASH");
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(mParameters);
                    mCamera.startPreview();
                    mLighOn = true;
                }
            }
        }, 5000);


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLighOn) {
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParameters);
            mCamera.stopPreview();
            mLighOn = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCamera != null) {
            mCamera.release();
        }
    }
}
