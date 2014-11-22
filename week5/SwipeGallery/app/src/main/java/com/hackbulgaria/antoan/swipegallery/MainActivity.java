package com.hackbulgaria.antoan.swipegallery;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.io.File;
import java.io.FileFilter;


public class MainActivity extends FragmentActivity {

    private File[] mPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(picturesDir.isDirectory()) {
            mPictures = picturesDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String fileName = pathname.getName();
                    return fileName.endsWith(".png")
                            || fileName.endsWith(".jpg")
                            || fileName.endsWith(".jpeg");
                }
            });

            if(mPictures != null) {
                ViewPager pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.getInstance(mPictures[position].getAbsolutePath());
        }

        @Override
        public int getCount() {
            return mPictures.length;
        }
    }
}
