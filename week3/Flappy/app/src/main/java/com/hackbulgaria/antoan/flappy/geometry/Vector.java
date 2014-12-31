package com.hackbulgaria.antoan.flappy.geometry;

/**
 * Created by Antoan on 09-Nov-14.
 */
public class Vector {

    public float x, y;

    public Vector(float x, float y) {
        set(x, y);
    }

    public Vector() {
        this(0, 0);
    }

    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
