package com.hackbulgaria.antoan.flappy.wrappers;

/**
 * Created by Antoan on 30-Dec-14.
 */
public class UserWrapper {
    private int mId;
    private int mScore;
    private String mName;

    public UserWrapper(int id, String name, int score) {
        this(name, score);
        mId = id;
    }

    public UserWrapper(String name, int score) {
        mName = name;
        mScore = score;
    }

    public String getName() {
        return mName;
    }

    public int getScore() {
        return mScore;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
