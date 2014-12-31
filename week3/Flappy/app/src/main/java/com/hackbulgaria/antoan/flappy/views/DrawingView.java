package com.hackbulgaria.antoan.flappy.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.hackbulgaria.antoan.flappy.game.Background;
import com.hackbulgaria.antoan.flappy.game.Bird;
import com.hackbulgaria.antoan.flappy.dialogs.EnterNameDialogFragment;
import com.hackbulgaria.antoan.flappy.game.GameClock;
import com.hackbulgaria.antoan.flappy.game.GameEvent;
import com.hackbulgaria.antoan.flappy.game.GameObject;
import com.hackbulgaria.antoan.flappy.game.HitArea;
import com.hackbulgaria.antoan.flappy.game.Obstacle;
import com.hackbulgaria.antoan.flappy.geometry.Rect;
import com.hackbulgaria.antoan.flappy.listeners.OnClickListener;
import com.hackbulgaria.antoan.flappy.R;
import com.hackbulgaria.antoan.flappy.dialogs.ViewHighscoresDialogFragment;
import com.hackbulgaria.antoan.flappy.managers.AudioManager;
import com.hackbulgaria.antoan.flappy.managers.PatternManager;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View implements GameClock.GameClockListener, OnClickListener {

    public static float SCREEN_DENSITY;
    private static final int DISTANCE_BETWEEN_PIPES = 220;
    private static final int FIRST_PIPE_POS = 400;
    private static final int BIRD_X = 100;
    private static final int EDGE_OFFSET = 15;
    private static final int ID_SCORES_BUTTON = 1;

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
    private final android.graphics.Rect mTempRect = new android.graphics.Rect();
    private int mScore;
    private Paint mGameOverStrokePaint;
    private PatternManager mPatternManager;
    private boolean mTempPause;
    private boolean mHasGameStarted;
    private List<HitArea> mClickListeners;

    private int mDistanceBetweenPipes;
    private int mFirstPipePos;
    private int mBirdX;
    private int mEdgeOffset;
    private HitArea mScoresHitArea;

    private int mGameplayTextSize;
    private int mGameplayVerticalSpacing;
    private int mGameplayStrokeWidth;
    private int mGameplayTextClickPadding;

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
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        SCREEN_DENSITY = metrics.density;

        mDistanceBetweenPipes = (int) (DISTANCE_BETWEEN_PIPES * SCREEN_DENSITY);
        mFirstPipePos = (int) (FIRST_PIPE_POS * SCREEN_DENSITY);
        mBirdX = (int) (BIRD_X * SCREEN_DENSITY);
        mEdgeOffset = (int) (EDGE_OFFSET * SCREEN_DENSITY);

        mPaint = new Paint();

        mObstacles = new ArrayList<>();
        mClickListeners = new ArrayList<>();

        initVariables();

        AssetManager assetManager = getContext().getAssets();
        Typeface plain = Typeface.createFromAsset(assetManager, "font.ttf");

        mGameplayTextSize = getResources().getDimensionPixelSize(R.dimen.gameplay_text_size);
        mGameplayVerticalSpacing = getResources().getDimensionPixelSize(R.dimen.gameplay_text_vertical_spacing);
        mGameplayStrokeWidth = getResources().getDimensionPixelSize(R.dimen.gameplay_text_stroke_width);
        mGameplayTextClickPadding = getResources().getDimensionPixelSize(R.dimen.gameplay_text_click_padding);

        mScorePaint = new Paint();
        mScorePaint.setTypeface(plain);
        mScorePaint.setColor(Color.WHITE);
        mScorePaint.setAlpha(90);
        mScorePaint.setTextSize(mGameplayTextSize);

        mGameOverFillPaint = new Paint();
        mGameOverFillPaint.setTypeface(plain);
        mGameOverFillPaint.setColor(Color.WHITE);
        mGameOverFillPaint.setTextSize(mGameplayTextSize);

        mGameOverStrokePaint = new Paint(mGameOverFillPaint);
        mGameOverStrokePaint.setColor(Color.BLACK);
        mGameOverStrokePaint.setStyle(Paint.Style.STROKE);
        mGameOverStrokePaint.setStrokeWidth(mGameplayStrokeWidth);
    }

    private void initVariables() {
        mGameOver = false;
        mCanTap = true;
        mTempPause = false;
        mNextObstacle = null;
        mScoresHitArea = null;
        mScore = 0;
        mHasGameStarted = false;
        mPatternManager = new PatternManager(new PatternManager.Pattern[] { PatternManager.Pattern.PATTERN_REST, PatternManager.Pattern.PATTERN_MEDIUM });
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
            drawCenteredText("TAP TO RESTART", mGameOverFillPaint, mGameplayTextSize + mGameplayVerticalSpacing, false);
        }
        else if(!mHasGameStarted) {
            drawCenteredText("TAP TO START", mGameOverFillPaint, 0, false);
            drawCenteredText("VIEW HIGHSCORES", mGameOverFillPaint, mGameplayTextSize + mGameplayVerticalSpacing, false, ID_SCORES_BUTTON);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawCenteredText(String text, Paint paint) {
        drawCenteredText(text, paint, 0, true);
    }

    private void drawCenteredText(String text, Paint paint, int yOffset, boolean isScore) {
        drawCenteredText(text, paint, yOffset, isScore, -1);
    }

    private void drawCenteredText(String text, Paint paint, int yOffset, boolean isScore, int id) {
        paint.getTextBounds(text, 0, text.length(), mTempRect);
        int boundWidth = mTempRect.width();
        int boundHeight = mTempRect.height();
        int x = (width - boundWidth) / 2;
        int y = (height + boundHeight) / 2 + yOffset;

        if(id != -1 && mScoresHitArea == null) {
            Rect rect = new Rect(x - mGameplayTextClickPadding, y - boundHeight - mGameplayTextClickPadding,
                    boundWidth + 2 * mGameplayTextClickPadding, boundHeight + 2 * mGameplayTextClickPadding);
            mScoresHitArea = new HitArea(id, rect, this) {
                @Override
                public boolean isEnabled() {
                    return !mHasGameStarted;
                }
            };
            mClickListeners.add(mScoresHitArea);
        }

        if(isScore) {
            final int saveCount = mCanvas.save();
            try {
                mCanvas.scale(5, 5, width * 0.5f, height * 0.5f);
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

                int xpos = (int) event.getX();
                int ypos = (int) event.getY();

                if(ypos > height - mEdgeOffset
                    || ypos < mEdgeOffset
                    || xpos < mEdgeOffset
                    || xpos > width - mEdgeOffset) {

                    return true;
                }

                for(HitArea hitArea : mClickListeners) {
                    if(hitArea.isEnabled() && hitArea.test(xpos, ypos)) {
                        return true;
                    }
                }

                if(!mHasGameStarted) {
                    mHasGameStarted = true;
                }

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
                    mClickListeners.clear();
                    initVariables();
                    mPaused = false;
                    mForcePaused = false;
                    setInitPositions();
                }

                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(mTempPause || w < h || w <= 0 || h <= 0) {
            mTempPause = false;
            return;
        }

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

            background.setWidth(width);
            background.setHeight(height);

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
            bird = new Bird(getContext(), mBirdX, height * 0.5f);
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

        int x = mFirstPipePos;
        while(x < width) {
            Obstacle obstacle = Obstacle.getInstance(this, mPipeTexture, x, height, mPatternManager.getNext());
            addObstacle(obstacle);
            if(mNextObstacle == null) {
                mNextObstacle = obstacle;
            }

            x += mDistanceBetweenPipes;
        }

        bird.setPosition(mBirdX, height * 0.5f);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {

        float lastX = mLastObstacle.getPosition().x;
        if(lastX < width) {
            Obstacle obstacle = Obstacle.getInstance(this, mPipeTexture, lastX + mDistanceBetweenPipes, height, mPatternManager.getNext());
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
            mGameOver = true;
            bird.die();
            pause(false);
            EnterNameDialogFragment newFragment = EnterNameDialogFragment.newInstance(mScore);
            FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
            newFragment.show(ft, "dialog");
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

        mTempPause = true;
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

    @Override
    public void OnClick(int id) {
        switch(id) {
            case ID_SCORES_BUTTON:
                ViewHighscoresDialogFragment newFragment = ViewHighscoresDialogFragment.newInstance();
                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                newFragment.show(ft, "dialog");

                break;
        }
    }

}
