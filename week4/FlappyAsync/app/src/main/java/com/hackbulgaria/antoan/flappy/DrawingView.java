package com.hackbulgaria.antoan.flappy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View implements GameClock.GameClockListener {

    public static final int DISTANCE_BETWEEN_PIPES = 400;
    public static final int FIRST_PIPE_POS = 400;
    public static final int BIRD_X = 100;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private GameClock mGameClock;
    private List<Obstacle> mObstacles;
    private int width, height;
    private Background background;
    private Bird bird;
    private boolean mPaused;
    private boolean mForcePaused;
    private boolean mGameOver;
    private Bitmap mPipeTexture;
    private Obstacle mLastObstacle;
    private boolean mCanTap;
    private AudioManager mAudioManager;
    private Obstacle mNextObstacle;
    private Paint mScorePaint;
    private Paint mGameOverFillPaint;
    private final Rect mTempRect = new Rect();
    private int mScore;
    private Paint mGameOverStrokePaint;
    private GameFragment.OnGameOverListener mListener;

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        mPaint = new Paint();

        mObstacles = new ArrayList<Obstacle>();
        initVariables();

        AssetManager assetManager = getContext().getAssets();
        Typeface plain = Typeface.createFromAsset(assetManager, "font.ttf");

        mScorePaint = new Paint();
        mScorePaint.setTypeface(plain);
        mScorePaint.setColor(Color.WHITE);
        mScorePaint.setAlpha(90);
        mScorePaint.setTextSize(100);

        mGameOverFillPaint = new Paint();
        mGameOverFillPaint.setTypeface(plain);
        mGameOverFillPaint.setColor(Color.WHITE);
        mGameOverFillPaint.setTextSize(72);

        mGameOverStrokePaint = new Paint(mGameOverFillPaint);
        mGameOverStrokePaint.setColor(Color.BLACK);
        mGameOverStrokePaint.setStyle(Paint.Style.STROKE);
        mGameOverStrokePaint.setStrokeWidth(12);
    }

    private void initVariables() {
        mGameOver = false;
        mCanTap = true;
        mNextObstacle = null;
        mScore = 0;
    }

    private void addObstacle(final Obstacle obstacle) {
        mObstacles.add(obstacle);
        post(new Runnable() {
            @Override
            public void run() {
                mGameClock.subscribe(obstacle);
            }
        });

        mLastObstacle = obstacle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mForcePaused)
            return;

        mCanvas.drawRect(0, 0, width, height, mPaint);

        background.draw(mCanvas);

        drawCenteredText(String.valueOf(mScore), mScorePaint);

        for(GameObject gameObject : mObstacles) {
            gameObject.draw(mCanvas);
        }

        bird.draw(mCanvas);

        if(mGameOver) {
            drawCenteredText("GAME OVER", mGameOverFillPaint, 0, false);
            drawCenteredText("TAP TO RESTART", mGameOverFillPaint, 80, false);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawCenteredText(String text, Paint paint) {
        drawCenteredText(text, paint, 0, true);
    }

    private void drawCenteredText(String text, Paint paint, int yOffset, boolean isScore) {
        paint.getTextBounds(text, 0, text.length(), mTempRect);
        int x = (width - mTempRect.width()) / 2;
        int y = (height + mTempRect.height()) / 2 + yOffset;

        if(isScore) {
            final int saveCount = mCanvas.save();
            try {
                mCanvas.scale(4, 4, width * 0.5f, height * 0.5f);
                mCanvas.drawText(text, x, y, mScorePaint);
            } finally {
                mCanvas.restoreToCount(saveCount);
            }
        }
        else {
            mCanvas.drawText(text, x, y, mGameOverStrokePaint);
            mCanvas.drawText(text, x, y, mGameOverFillPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mCanTap) {
                    if (mGameClock.isPaused()) {
                        mGameClock.resume();
                    }
                    bird.flap();
                }
                else if(mGameOver) {
                    for(Obstacle obstacle : mObstacles) {
                        Obstacle.release(obstacle);
                    }
                    mObstacles.clear();
                    initVariables();
                    mPaused = false;
                    mForcePaused = false;
                    setInitPositions();
                    mGameClock.resume();
                }
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(w < h || w <= 0 || h <= 0)
            return;

        width = w;
        height = h;

        boolean resize = (!mPaused && oldw * oldh > 0);

        mPaused = false;
        mForcePaused = false;

        initialize(resize);
    }

    private void initialize(boolean resize) {
        if(mBitmap != null) {
            mBitmap.recycle();
        }

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        if(resize) {
            for(Obstacle obstacle : mObstacles) {
                if(obstacle.getPosition().y > 0) {
                    obstacle.setPosition(obstacle.getPosition().x, height - obstacle.getHeight());
                }
            }
            return;
        }

        if(mGameClock == null) {
            mGameClock = new GameClock();
        }
        else {
            mGameClock.resume();
            mGameClock.pause(false);
        }

        if(background == null) {
            background = new Background(getContext(), width, height);
        }

        if(bird == null) {
            bird = new Bird(getContext(), BIRD_X, height * 0.5f);
        }

        if(mPipeTexture == null) {
            mPipeTexture = BitmapFactory.decodeResource(getResources(), R.drawable.pipe);
        }

        setInitPositions();
    }

    private void setInitPositions() {

        mGameClock.clearSubscriptions();
        mGameClock.subscribeMain(this);
        mGameClock.subscribe(background);
        mGameClock.subscribe(bird);

        int x = FIRST_PIPE_POS;
        while(x < width) {
            Obstacle obstacle = Obstacle.getInstance(this, mPipeTexture, x, height);
            addObstacle(obstacle);
            if(mNextObstacle == null) {
                mNextObstacle = obstacle;
            }

            x += DISTANCE_BETWEEN_PIPES;
        }

        bird.setPosition(BIRD_X, height * 0.5f);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        float lastX = mLastObstacle.getPosition().x;
        if(lastX < width) {
            Obstacle obstacle = Obstacle.getInstance(this, mPipeTexture, lastX + DISTANCE_BETWEEN_PIPES, height);
            addObstacle(obstacle);
        }

        if(mCanTap) {
            boolean collision = false;

            for (Obstacle obstacle : mObstacles) {
                if (bird.collidesWith(obstacle)) {
                    collision = true;
                    break;
                }
            }

            if (collision || bird.getPosition().y < 0 || bird.getPosition().y + bird.getHeight() > height) {
                mAudioManager.playDeath();
                mCanTap = false;

                if(bird.getPosition().y + bird.getHeight() > height) {
                    bird.setPosition(bird.getPosition().x, height - bird.getHeight() * 0.5f);
                }

                if (bird.getPosition().y >= 0) {
                    bird.flap();
                }
            }

            if(bird.getPosition().x > mNextObstacle.getPosition().x) {
                int index = mObstacles.indexOf(mNextObstacle);
                mNextObstacle = mObstacles.get(index + 1);
                increaseScore();
            }
        }

        if(!mGameOver && (bird.getPosition().y >  height )) {
            if(mListener != null) {
                mListener.onGameOver(mScore);
            }
            mGameOver = true;
            bird.die();
            pause(false);
        }

        invalidate();
    }

    private void increaseScore() {
        mAudioManager.playCoin();
        mScore ++;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    public void pause(boolean force) {
        mPaused = true;
        mForcePaused = force;
        if(mGameClock != null) {
            mGameClock.pause(force);
        }

        if(force) {

            if(mGameClock != null)
                mGameClock.clearSubscriptions();

            if (bird != null) {
                bird.dispose();
                bird = null;
            }

            if (background != null) {
                background.dispose();
                background = null;
            }

            for (GameObject obstacle : mObstacles) {
                obstacle.dispose();
            }
            mObstacles.clear();
            mObstacles = null;

            mNextObstacle = null;

            if(mPipeTexture != null) {
                mPipeTexture.recycle();
                mPipeTexture = null;
            }

            if(mBitmap != null) {
                mBitmap.recycle();
                mBitmap = null;
            }
        }
    }

    public void resume() {
        init();
    }

    public void unsubscribe(final Obstacle obstacle) {
        post(new Runnable() {
            @Override
            public void run() {
                if(obstacle != null) {
                    mGameClock.unsubscribe(obstacle);
                    if(mObstacles != null)
                        mObstacles.remove(obstacle);
                }
            }
        });
    }

    public void setAudioManager(AudioManager audioManager) {
        this.mAudioManager = audioManager;
    }

    public void setOnGameOverListener(GameFragment.OnGameOverListener listener) {
        mListener = listener;
    }
}
