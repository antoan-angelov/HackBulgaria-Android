package com.hackbulgaria.antoan.drawablebrush;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Antoan on 08-Nov-14.
 */
public class BrushImageButton extends ImageButton {

    private static final int[] STATE_BRUSH_SELECTED = {R.attr.state_brush_selected};

    private boolean mBrushSelected;

    public BrushImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (mBrushSelected) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

            mergeDrawableStates(drawableState, STATE_BRUSH_SELECTED);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    public void setBrushSelected(boolean brushSelected) {
        if (this.mBrushSelected != brushSelected) {
            this.mBrushSelected = brushSelected;

            refreshDrawableState();
        }
    }

}
