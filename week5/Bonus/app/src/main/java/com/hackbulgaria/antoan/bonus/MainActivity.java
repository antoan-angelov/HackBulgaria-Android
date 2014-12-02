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
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCamera = Camera.open();
        mCamera.startPreview();
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
            }
        });

        if (!mLighOn) {
            Log.v("tag", "STARTING FLASH");

            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParameters);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mParameters = mCamera.getParameters();
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(mParameters);
                }
            }, 1000);


            mLighOn = true;


        }
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

        if (mCamera != null) {
            mCamera.release();
        }
    }
}
