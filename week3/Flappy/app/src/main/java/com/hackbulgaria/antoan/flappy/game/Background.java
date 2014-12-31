package com.hackbulgaria.antoan.flappy.game;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.hackbulgaria.antoan.flappy.R;

/**
 * Created by Antoan on 09-Nov-14.
 */
public class Background extends GameObject {

    private BitmapShader mShader;
    private Paint mPaint;

    public Background(Context context, int width, int height) {
        super(context, R.drawable.clouds, 0, 0, width, height);

        mShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mPaint = new Paint();
        mPaint.setShader(mShader);
    }

    @Override
    public void draw(Canvas canvas) {
        final int saveCount = canvas.save();
        try {
            canvas.translate(mPosition.x, 0);
            canvas.drawRect(-mPosition.x, 0, getWidth()- mPosition.x, getHeight(), mPaint);
        } finally {
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        mPosition.x -= 20;
    }

    @Override
    public boolean canPause() {
        return false;
    }

}
