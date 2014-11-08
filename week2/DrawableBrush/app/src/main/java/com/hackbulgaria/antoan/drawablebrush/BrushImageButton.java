package com.hackbulgaria.antoan.drawablebrush;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Antoan on 08-Nov-14.
 */
public class BrushImageButton extends ImageButton {

    private static final int[] STATE_MESSAGE_UNREAD = {R.attr.state_brush_selected};

    private boolean brushSelected;

    public BrushImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Constructors, view loading etc...
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        // If the message is unread then we merge our custom message unread state into
        // the existing drawable state before returning it.
        if (brushSelected) {
            // We are going to add 1 extra state.
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

            mergeDrawableStates(drawableState, STATE_MESSAGE_UNREAD);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    public void setBrushSelected(boolean brushSelected) {
        if (this.brushSelected != brushSelected) {
            this.brushSelected = brushSelected;

            // Refresh the drawable state so that it includes the message unread
            // state if required.
            refreshDrawableState();
        }
    }

}
