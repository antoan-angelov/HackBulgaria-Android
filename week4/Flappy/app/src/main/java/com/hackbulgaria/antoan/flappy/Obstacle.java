package com.hackbulgaria.antoan.flappy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Antoan on 10-Nov-14.
 */
public class Obstacle extends GameObject {

    private final static Stack<Obstacle> sPool = new Stack<Obstacle>();
    public static final int PIPE_MIN_HEIGHT = 100;
    public static final float PIPE_HEIGHT_COEFF = 0.3f;
    public static final int PIPE_SPEED = 14;

    public static Obstacle getInstance(DrawingView context, Bitmap texture, float x, float canvasHeight) {
        double random = Math.random();
        int pipeHeight = (int) (texture.getHeight() * PIPE_HEIGHT_COEFF * random + PIPE_MIN_HEIGHT);
        float y = (random > 0.5 ? 0 : canvasHeight - pipeHeight);

        Obstacle obstacle;

        if(sPool.size() == 0) {
            obstacle = new Obstacle(context, texture, x, y, pipeHeight);
        }
        else {
            obstacle = sPool.pop();
            obstacle.setPosition(x, y);
            obstacle.setHeight(pipeHeight);
        }

        return obstacle;
    }

    private DrawingView mContext;

    private Obstacle(DrawingView context, Bitmap bitmap, float initialX, float initialY, int pipeHeight) {
        super(bitmap, initialX, initialY, bitmap.getWidth(), pipeHeight);
        mContext = context;
    }

    @Override
    public void draw(Canvas canvas) {

        final int saveCount = canvas.save();
        try {
            if(mPosition.y <= 0) {
                canvas.translate(0, getHeight());
                canvas.scale(1, -1);
            }
            canvas.drawBitmap(mBitmap, mPosition.x, mPosition.y, null);
        } finally {
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        mPosition.x -= PIPE_SPEED;

        if(mPosition.x + getWidth() < 0) {
            release(this);
            mContext.unsubscribe(this);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        sPool.clear();
        mContext.unsubscribe(this);
    }

    public static void release(Obstacle obstacle) {
        sPool.add(obstacle);
    }

    @Override
    public boolean canPause() {
        return true;
    }
}
