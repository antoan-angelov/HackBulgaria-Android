package gestureimageview.hackbulgaria.antoan.gestureimageview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity implements View.OnTouchListener, View.OnClickListener {


    private class Point {
        public float x, y;

        Point(float x, float y) {
            set(x, y);
        }

        Point() { set(0, 0); }

        public Point set(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Point set(Point p) {
            this.x = p.x;
            this.y = p.y;
            return this;
        }

        public Point substract(Point p) {
            this.x -= p.x;
            this.y -= p.y;
            return this;
        }

        public Point add(Point p) {
            this.x += p.x;
            this.y += p.y;
            return this;
        }

        public Point multiply(double by) {
            this.x *= by;
            this.y *= by;
            return this;
        }

        public Point clone() {
            return new Point(this.x, this.y);
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public double length() {
            return Math.sqrt(this.x * this.x + this.y * this.y);
        }
    }

    class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mImage.setTranslationX(0);
            mImage.setTranslationY(0);
            mImage.setRotation(0);
            mImage.setScaleX(1);
            mImage.setScaleY(1);
            init();
            return super.onDoubleTap(e);
        }
    }

    private ImageView mImage;
    private float scale;
    private Point point1, point2;
    private float rotation;
    private double initialRotation;
    private double initialDistance;
    private final Point startVector = new Point();
    private final Point endVector = new Point();
    private final Point zeroVector = new Point();
    private final Point initialTranslateVector = new Point();
    private final Point translateVector = new Point();
    private int initialX, initialY;
    private GestureDetector mDetector;
    private Button mSaveFrameButton;
    private Button mPlayButton;
    private List<Animator> mStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mSaveFrameButton = (Button) findViewById(R.id.save_frame);
        mPlayButton = (Button) findViewById(R.id.play);
        mStates = new ArrayList<Animator>();

        mSaveFrameButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);

        init();

        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnTouchListener(this);
        mDetector = new GestureDetector(this, new DoubleTapGestureListener());
    }

    private void init() {
        scale = 1f;
        point1 = null;
        point2 = null;
        rotation = 0;
        initialRotation = 0;
        initialDistance = 0;
        startVector.set(0, 0);
        endVector.set(0, 0);
        zeroVector.set(0, 0);
        initialTranslateVector.set(0, 0);
        translateVector.set(0, 0);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.save_frame:

                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, mImage.getTranslationX());
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, mImage.getTranslationY());
                PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofFloat(View.ROTATION, mImage.getRotation());
                PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, mImage.getScaleX());
                PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, mImage.getScaleY());
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mImage, pvhX, pvhY, pvhRotation, pvhScaleX, pvhScaleY);
                mStates.add(animator);

                break;
            case R.id.play:
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(mStates);
                animatorSet.start();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int[] img_coordinates = new int[2];
        mImage.getLocationOnScreen(img_coordinates);
        initialX = img_coordinates[0];
        initialY = img_coordinates[1];
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        this.mDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        int index = event.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(index == 0) {
                    if(point1 != null && point2 == null)
                        point2 = point1;

                    point1 = new Point();
                    localToGlobal(point1, event.getX(), event.getY(), mImage);

                    translateVector.set(mImage.getTranslationX(), mImage.getTranslationY());
                    rotation = mImage.getRotation();
                    scale = mImage.getScaleX();

                    if(point2 == null) {
                        initialTranslateVector.set(point1).substract(translateVector);
                    }
                    else {
                        setInitValues();
                    }
                }
                else if (index == 1) {
                    point2 = new Point();
                    localToGlobal(point2, event.getX(1), event.getY(1), mImage);
                    setInitValues();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(index == 0) {
                    if(point2 != null) {
                        initialTranslateVector.set(point2).substract(translateVector);
                        point1 = point2;
                        point2 = null;
                    }
                    else {
                        point1 = null;
                    }
                }
                else if (index == 1 && point2 != null) {
                    if(point1 != null) {
                        initialTranslateVector.set(point1).substract(translateVector);
                    }
                    else {
                        point1 = point2;
                    }

                    point2 = null;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                localToGlobal(point1, event.getX(0), event.getY(0), mImage);
                translateVector.set(point1).substract(initialTranslateVector);

                if (point2 != null) {
                    localToGlobal(point2, event.getX(1), event.getY(1), mImage);
                    translateVector.set(point1).add(point2).multiply(0.5).substract(initialTranslateVector);
                    endVector.set(point2).substract(point1);

                    // rotation
                    double ang = findAngle(startVector, zeroVector, endVector);
                    if (!Double.isNaN(ang)) {
                        ang = ang * 180 / Math.PI;
                        rotation = (float) (initialRotation + ang);
                        // wrap if needed
                        if (rotation > 180)
                            rotation -= 360;
                        else if (rotation < -180)
                            rotation += 360;
                    }

                    mImage.setRotation(rotation);

                    // scaling
                    double d = endVector.length();
                    scale = (float) (d / initialDistance);
                    scale = Math.max(0.3f, Math.min(scale, 10.0f));

                    mImage.setScaleX(scale);
                    mImage.setScaleY(scale);
                }

                mImage.setTranslationX(translateVector.x);
                mImage.setTranslationY(translateVector.y);
                break;
        }

        return true;
    }

    private void setInitValues() {
        startVector.set(point2).substract(point1);
        double d = startVector.length();
        initialDistance = d / scale;
        initialRotation = rotation;
        initialTranslateVector.set(point1).add(point2).multiply(0.5).substract(translateVector);
    }

    private void localToGlobal(Point p, float x, float y, ImageView mImage) {
        double width = mImage.getMeasuredWidth(), height = mImage.getMeasuredHeight();
        double rotation = mImage.getRotation() * Math.PI / 180;
        double tx = mImage.getTranslationX(), ty = mImage.getTranslationY();
        double globalCenterX = tx+initialX+width*0.5, globalCenterY = ty+initialY+height*0.5;
        double rx = x - width * 0.5, ry = y - height * 0.5;
        double cos = Math.cos(rotation), sin = Math.sin(rotation);
        double newX = rx * cos - ry * sin, newY = rx * sin + ry * cos;

        p.set((float) (globalCenterX + newX * mImage.getScaleX()), (float) (globalCenterY + newY * mImage.getScaleY()));
    }

    // Law of Cosines + 3x3 matrix determinant to determine the sign
    double findAngle(Point A, Point B, Point C) {
        float det = A.x * B.y + B.x * C.y + C.x * A.y - A.x * C.y - B.x * A.y - C.x * B.y;
        double AB = Math.sqrt(Math.pow(B.x - A.x, 2) + Math.pow(B.y - A.y, 2));
        double BC = Math.sqrt(Math.pow(B.x - C.x, 2) + Math.pow(B.y - C.y, 2));
        double AC = Math.sqrt(Math.pow(C.x - A.x, 2) + Math.pow(C.y - A.y, 2));
        return (det < 0 ? 1 : -1) * Math.acos((BC * BC + AB * AB - AC * AC) / (2 * BC * AB));
    }
}
