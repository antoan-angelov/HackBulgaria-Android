package com.hackbulgaria.antoan.splashscreen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        final ImageView image = (ImageView) findViewById(R.id.splash);
        final Handler handler = new Handler(Looper.getMainLooper());
        final ViewGroupOverlay overlay = root.getOverlay();

        // Splash screen doesn't show if I show it directly in onCreate.
        // Couldn't find the right event method from which to show it,
        // so I just made the splashscreen show 500ms after launch.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overlay.add(image);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        overlay.clear();
                    }
                }, 3000);
            }
        }, 500);
    }
}
