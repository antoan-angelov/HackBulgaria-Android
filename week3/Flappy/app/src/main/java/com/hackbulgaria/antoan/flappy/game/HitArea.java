package com.hackbulgaria.antoan.flappy.game;

import com.hackbulgaria.antoan.flappy.geometry.Rect;
import com.hackbulgaria.antoan.flappy.listeners.OnClickListener;

/**
 * Created by Antoan on 29-Dec-14.
 */
public class HitArea {
    private int mId;
    private Rect mRect;
    private OnClickListener mClickListener;

    public HitArea(int id, Rect rect, OnClickListener listener) {
        mId = id;
        mRect = rect;
        mClickListener = listener;
    }

    public boolean test(int x, int y) {
        boolean result = mRect.isInRect(x, y);

        if(result) {
            mClickListener.OnClick(mId);
        }

        return result;
    }

    public boolean isEnabled() {
        return true;
    }
}
