package com.hackbulgaria.antoan.flappy.game;

import android.os.Handler;

import com.hackbulgaria.antoan.flappy.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class GameClock {

    private List<GameClockListener> clockListeners = new ArrayList<GameClockListener>();
    private GameClockListener mMainListener;
    private boolean mPaused = false;
    private boolean mForcePause = false;

    public void pause(boolean force) {
        mPaused = true;
        mForcePause = force;
    }

    public void resume() {
        if(mForcePause) {
            handler.post(new ClockRunnable());
        }

        mPaused = false;
        mForcePause = false;
    }

    public boolean isPaused() {
        return mPaused;
    }

    public void clearSubscriptions() {
        clockListeners.clear();
        mMainListener = null;
    }

    public void subscribeMain(GameClockListener listener) {
        mMainListener = listener;
    }

    public static interface GameClockListener {
        public void onGameEvent(GameEvent gameEvent);
        public boolean canPause();
    }

    private Handler handler = new Handler();

    public GameClock() {
        mPaused = true;
        mForcePause = false;
        handler.post(new ClockRunnable());
    }

    public void subscribe(GameClockListener listener) {
        clockListeners.add(listener);
    }

    public void unsubscribe(GameClockListener listener) {
        clockListeners.remove(listener);
    }

    private class ClockRunnable implements Runnable {
        @Override
        public void run() {
            if(!mForcePause) {
                onTimerTick();
                handler.postDelayed(this, Constants.FRAMERATE_CONSTANT);
            }
        }

        private void onTimerTick() {

            if(!mForcePause) {
                for (GameClockListener listener : clockListeners) {
                    if(!mPaused || (mPaused && !listener.canPause())) {
                        listener.onGameEvent(new GameEvent());
                    }
                }

                if(mMainListener != null && (!mPaused || (mPaused && !mMainListener.canPause()))) {
                    mMainListener.onGameEvent(new GameEvent());
                }
            }
        }
    }
}
