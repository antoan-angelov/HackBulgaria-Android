package gestureimageview.hackbulgaria.antoan.gestureimageview;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;


public class MyActivity extends Activity {

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

    private ImageView mImage;
    private float scale = 1f;
    private Point point1 = null, point2 = null;
    private float rotation = 0;
    private double initialRotation = 0;
    private double initialDistance = 0;
    private Point startVector = new Point();
    private Point endVector = new Point();
    private Point zeroVector = new Point();
    private Point initialTranslateVector = new Point();
    private Point translateVector = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mImage = (ImageView) findViewById(R.id.image);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getActionIndex() == 0) {
                    if(point1 != null && point2 == null)
                        point2 = point1;

                    point1 = new Point(event.getX(), event.getY());

                    if(point2 == null) {
                        initialTranslateVector.set(point1).substract(translateVector);
                    }
                    else {
                        initialTranslateVector.set(point1).add(point2).multiply(0.5).substract(translateVector);
                    }
                }
                else if (event.getActionIndex() == 1) {
                    point2 = new Point(event.getX(1), event.getY(1));
                    startVector.set(point2).substract(point1);
                    double d = startVector.length();
                    initialDistance = d / scale;
                    initialRotation = rotation;
                    initialTranslateVector.set(point1).add(point2).multiply(0.5).substract(translateVector);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                int index = event.getActionIndex();
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
                point1.set(event.getX(0), event.getY(0));
                translateVector.set(point1).substract(initialTranslateVector);

                if (point2 != null) {
                    point2.set(event.getX(1), event.getY(1));
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

    // Law of Cosines + 3x3 matrix determinant to determine the sign
    double findAngle(Point A, Point B, Point C) {
        float det = A.x * B.y + B.x * C.y + C.x * A.y - A.x * C.y - B.x * A.y - C.x * B.y;
        double AB = Math.sqrt(Math.pow(B.x - A.x, 2) + Math.pow(B.y - A.y, 2));
        double BC = Math.sqrt(Math.pow(B.x - C.x, 2) + Math.pow(B.y - C.y, 2));
        double AC = Math.sqrt(Math.pow(C.x - A.x, 2) + Math.pow(C.y - A.y, 2));
        return (det < 0 ? 1 : -1) * Math.acos((BC * BC + AB * AB - AC * AC) / (2 * BC * AB));
    }
}
