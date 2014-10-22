package puzzlegame.hackbulgaria.antoan.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MyActivity extends Activity implements View.OnDragListener {

    private List<Drawable> mSlices;
    private HashMap<Drawable, Integer> mHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mHash = new HashMap<Drawable, Integer>();
        mSlices = new ArrayList<Drawable>();

        TypedArray arr = getResources().obtainTypedArray(R.array.slices);
        int len = arr.length(), rows = 1 + (len - 1) / 4, columns = (len > 4 ? 4 : len);

        // initially, the slices are in solved order
        // associating each Drawable with its corresponding index
        for (int i = 0; i < len; i++) {
            Drawable d = arr.getDrawable(i);
            mSlices.add(d);
            mHash.put(d, i);
        }

        arr.recycle();

        // and then shuffle the slices
       Collections.shuffle(mSlices);

        int counter = 0;
        Random rnd = new Random();
        GridLayout row = (GridLayout) findViewById(R.id.cont);

        for (int i = 0; i < rows; i++) {
            // initialize a new row

            for (int j = 0; j < columns; j++) {
                Drawable d = mSlices.get(counter++);
                // pick random border color
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                final ImageView iv = new ImageView(this);

                iv.setAdjustViewBounds(true);
                iv.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
                iv.setPadding(5, 5, 5, 5);
                iv.setBackgroundColor(color);
                iv.setTag(counter);
                iv.setImageDrawable(d);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setOnDragListener(MyActivity.this);

                final int finalJ = j;
                final int finalI = i;

                Coord coord = new Coord(finalJ, finalI, mHash.get(d));
                iv.setTag(coord);
                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // Instantiates the drag shadow builder.
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(iv);

                        // Starts the drag
                        iv.startDrag(null, myShadow, iv, 0);

                        return false;
                    }
                });

                row.addView(iv);
            }

        }
    }

    private class Coord {
        public int x, y;
        public int index;

        public Coord(int x, int y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }
    }

    private boolean checkSolved() {

        GridLayout row = (GridLayout) findViewById(R.id.cont);
        int lastCoord = -1;
        for(int i=0; i < row.getChildCount(); i++) {
            ImageView iv = (ImageView) row.getChildAt(i);
            Coord coord = (Coord) iv.getTag();

            if(coord.index <= lastCoord)
                return false;
            lastCoord = coord.index;
        }

        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        final int action = dragEvent.getAction();

        switch(action) {
            case DragEvent.ACTION_DROP:
                ImageView below = (ImageView) view;
                ImageView above = (ImageView) dragEvent.getLocalState();

                Coord coordAbove = (Coord) above.getTag();
                Coord coordBelow = (Coord) below.getTag();
                above.setTag(coordBelow);
                below.setTag(coordAbove);

                ObjectAnimator animX1 = ObjectAnimator.ofFloat(below, "x", coordAbove.x * below.getMeasuredHeight());
                ObjectAnimator animY1 = ObjectAnimator.ofFloat(below, "y", coordAbove.y * below.getMeasuredHeight());
                ObjectAnimator animX2 = ObjectAnimator.ofFloat(above, "x", coordBelow.x * below.getMeasuredHeight());
                ObjectAnimator animY2 = ObjectAnimator.ofFloat(above, "y", coordBelow.y * below.getMeasuredHeight());
                AnimatorSet animSetXY = new AnimatorSet();
                animSetXY.playTogether(animX1, animY1, animX2, animY2);
                animSetXY.start();

                if(checkSolved()) {
                    Toast.makeText(MyActivity.this, "You have solved the puzzle!", Toast.LENGTH_LONG).show();
                }

                break;
        }

        return true;
    }
}
