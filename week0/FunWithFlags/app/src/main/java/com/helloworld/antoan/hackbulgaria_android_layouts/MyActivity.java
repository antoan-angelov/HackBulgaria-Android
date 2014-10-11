package com.helloworld.antoan.hackbulgaria_android_layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MyActivity extends Activity {

    public static final int MODE_EASY = 1;
    public static final int MODE_EASY_HORIZONTAL = 2;
    public static final int MODE_MEDIUM = 3;
    public static final int MODE_HARD = 4;

    private final static int[] mFlagsEasy = {R.array.Bulgaria, R.array.Germany, R.array.Lithuania, R.array.Netherlands, R.array.Russia};
    private final static int[] mFlagsEasyHorizontal = {R.array.Romania, R.array.Belgium, R.array.Italy};
    private final static int[] mFlagsMedium = {R.array.Bulgaria, R.array.Germany, R.array.Lithuania, R.array.Netherlands, R.array.Russia, R.array.Romania, R.array.Belgium, R.array.Italy};
    private int mCurrentFlag;

    private View mColor1;
    private View mColor2;
    private View mColor3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(MODE_MEDIUM);
    }

    private void init(final int mode) {

        if(mode == MODE_EASY || mode == MODE_EASY_HORIZONTAL || mode == MODE_MEDIUM) {

            int layoutId = 0;
            int[] flagsArray = null;

            if(mode == MODE_EASY) {
                layoutId = R.layout.flags_easy;
                flagsArray = mFlagsEasy;
            }
            else if(mode == MODE_EASY_HORIZONTAL) {
                layoutId = R.layout.flags_easy_horizontal;
                flagsArray = mFlagsEasyHorizontal;
            }
            else if(mode == MODE_MEDIUM) {
                layoutId = R.layout.flags_medium;
                flagsArray = mFlagsMedium;
            }

            setContentView(layoutId);

            View flagCont = findViewById(R.id.flag_cont);

            mColor1 = flagCont.findViewById(R.id.color1);
            mColor2 = flagCont.findViewById(R.id.color2);
            mColor3 = flagCont.findViewById(R.id.color3);

            final int[] finalFlagsArray = flagsArray;
            flagCont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadNextFlag(finalFlagsArray);
                }
            });

            mCurrentFlag = -1;
            loadNextFlag(flagsArray);
        }
        else if(mode == MODE_HARD) {
            setContentView(R.layout.flags_hard);
        }
    }

    private void loadNextFlag(int[] flagsArray) {

        if(mCurrentFlag < flagsArray.length - 1)
            mCurrentFlag ++;
        else
            mCurrentFlag = 0;

        int[] colors = getResources().getIntArray(flagsArray[mCurrentFlag]);
        mColor1.setBackgroundColor(colors[0]);
        mColor2.setBackgroundColor(colors[1]);
        mColor3.setBackgroundColor(colors[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
