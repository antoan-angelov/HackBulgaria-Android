package com.hackbulgaria.antoan.flappy.managers;

import android.content.Context;
import android.media.MediaPlayer;

import com.hackbulgaria.antoan.flappy.R;

/**
 * Created by Antoan on 11-Nov-14.
 */
public class AudioManager {
    private MediaPlayer mLoop;
    private MediaPlayer mDeath;
    private MediaPlayer mCoin;

    public AudioManager(Context context) {
        mLoop = MediaPlayer.create(context, R.raw.loop);
        mLoop.setLooping(true);
        mLoop.setVolume(0.2f, 0.2f);
        mLoop.start();

        mDeath = MediaPlayer.create(context, R.raw.die);
        mCoin = MediaPlayer.create(context, R.raw.coin);
    }

    public void playDeath() {
        mDeath.start();
    }

    public void playCoin() {
        mCoin.start();
    }

    public void dispose() {
        mLoop.release();
        mLoop = null;

        mDeath.release();
        mDeath = null;

        mCoin.release();;
        mCoin = null;
    }
}
