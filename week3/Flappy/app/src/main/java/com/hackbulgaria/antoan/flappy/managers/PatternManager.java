package com.hackbulgaria.antoan.flappy.managers;

import android.util.Log;

/**
 * Created by Antoan on 25-Dec-14.
 */
public class PatternManager {

    public static class Pattern {

        public final static float INVALID_PATTERN = 2;

        public static final Pattern PATTERN_REST = new Pattern(new float[] {0.3f, 0.4f, - 0.4f, 0.3f, 0.5f});
        public static final Pattern PATTERN_MEDIUM = new Pattern(new float[] {-0.65f, -0.6f, 0.5f, -0.4f, 0.5f, -0.65f, 0.6f, -0.4f});

        private final float[] mPattern;
        private int mIndex;

        private Pattern(float[] pattern) {
            mPattern = pattern;
            mIndex = 0;
        }

        public float getNext() {
            if(mIndex >= mPattern.length) {
                return INVALID_PATTERN;
            }

            return mPattern[mIndex++];
        }

        public void reset() {
            mIndex = 0;
        }
    }

    private Pattern mCurrentPattern;
    private Pattern[] mSequence;
    private int mSequenceIndex;

    public PatternManager(Pattern[] sequence) {
        mSequence = sequence;
        mSequenceIndex = 0;
        mCurrentPattern = mSequence[mSequenceIndex];
        mCurrentPattern.reset();
    }

    public float getNext() {

        float res = mCurrentPattern.getNext();

        if(res != Pattern.INVALID_PATTERN) {
            return res;
        }

        if(mSequenceIndex + 1 >= mSequence.length) {
            mSequenceIndex = 0;
        }

        mCurrentPattern = mSequence[++ mSequenceIndex];
        mCurrentPattern.reset();

        return mCurrentPattern.getNext();
    }

}
