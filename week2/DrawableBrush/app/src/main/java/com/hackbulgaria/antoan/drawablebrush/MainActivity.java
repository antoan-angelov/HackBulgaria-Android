package com.hackbulgaria.antoan.drawablebrush;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {

    private CustomView mCustomView;
    private BrushImageButton mStarBrush;
    private BrushImageButton mPresentBrush;
    private BrushImageButton mPlusBrush;
    private BrushImageButton mSelectedBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomView = (CustomView) findViewById(R.id.custom_view);

        mStarBrush = (BrushImageButton) findViewById(R.id.star);
        mPresentBrush = (BrushImageButton) findViewById(R.id.present);
        mPlusBrush = (BrushImageButton) findViewById(R.id.plus);

        mStarBrush.setOnClickListener(this);
        mPresentBrush.setOnClickListener(this);
        mPlusBrush.setOnClickListener(this);

        selectBrush(mPlusBrush);
    }

    private void selectBrush(BrushImageButton brush) {
        brush.setBrushSelected(true);
        if(mSelectedBrush != null) {
            mSelectedBrush.setBrushSelected(false);
        }
        mSelectedBrush = brush;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.star:
                mCustomView.setBrush(CustomView.BRUSH_STAR);
                selectBrush(mStarBrush);
                break;
            case R.id.present:
                mCustomView.setBrush(CustomView.BRUSH_PRESENT);
                selectBrush(mPresentBrush);
                break;
            case R.id.plus:
                mCustomView.setBrush(CustomView.BRUSH_PLUS);
                selectBrush(mPlusBrush);
                break;
        }
    }
}
