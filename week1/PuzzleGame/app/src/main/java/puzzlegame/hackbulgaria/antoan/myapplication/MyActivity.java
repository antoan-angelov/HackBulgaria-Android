package puzzlegame.hackbulgaria.antoan.myapplication;

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
        LinearLayout rowContainer = (LinearLayout) findViewById(R.id.cont);

        for (int i = 0; i < rows; i++) {
            // initialize a new row
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);
            rowContainer.addView(row);

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

    private boolean checkSolved() {
        for(int i=0; i<mSlices.size(); i++) {
            Drawable d = mSlices.get(i);
            int expectedIndex = mHash.get(d);
            if(expectedIndex != i) {
                return false;
            }
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

                // swap items in the randomized List
                int indexUnder = mSlices.indexOf(below.getDrawable());
                int indexAbove = mSlices.indexOf(above.getDrawable());
                mSlices.set(indexUnder, above.getDrawable());
                mSlices.set(indexAbove, below.getDrawable());

                // swap border colors
                int col1 = ((ColorDrawable) below.getBackground()).getColor();
                int col2 = ((ColorDrawable) above.getBackground()).getColor();
                below.setBackgroundColor(col2);
                above.setBackgroundColor(col1);

                // swap drawables
                Drawable d1 = below.getDrawable();
                Drawable d2 = above.getDrawable();
                above.setImageDrawable(d1);
                below.setImageDrawable(d2);

                // check if puzzle is solved
                if(checkSolved()) {
                    Toast.makeText(MyActivity.this, "You have solved the puzzle!", Toast.LENGTH_LONG).show();
                }

                break;
        }

        return true;
    }
}
