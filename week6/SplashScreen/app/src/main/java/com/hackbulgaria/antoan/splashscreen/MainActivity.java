package com.hackbulgaria.antoan.splashscreen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        final Handler handler = new Handler(Looper.getMainLooper());
        final ViewGroupOverlay overlay = root.getOverlay();

        Drawable drawable = getResources().getDrawable(R.drawable.splash);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        drawable.setBounds(0, 0, metrics.widthPixels, bitmap.getHeight() * bitmap.getWidth() / metrics.widthPixels);
        overlay.add(drawable);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overlay.clear();
            }
        }, 3000);
    }
}
