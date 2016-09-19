package com.inter.aktiehq.app.CanvasListe;

/*
    Klasse zum zeichnen der Elemente der Liste

 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.inter.aktiehq.app.R;

public class CanvasListe extends View implements SensorEventListener {

    private static final float TOLERANCE = 5;
    public int width;
    public int height;
    Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private float mX, mY;

    private ImageView image;
    private Drawable mCustomImage;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    public CanvasListe(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

        // image = new ImageView(this);
        image = (ImageView) findViewById(R.id.imageViewCompass);
        mCustomImage = context.getResources().getDrawable(R.drawable.compass);

        //Bitmap bMap = BitmapFactory.decodeFile("drawable/compass.png");
        //image.setImageBitmap(bMap);


    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawRect(15,15,15,15, mPaint);

        Rect imageBounds = canvas.getClipBounds();  // Adjust this for where you want it

        mCanvas.rotate(50);

        mCustomImage.setBounds(imageBounds);
        mCustomImage.draw(canvas);

        // Bitmap bitmap = BitmapFactory.decodeFile(filePath);
    }


    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        //tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
         //mCustomImage.startAnimation(ra);

        mCanvas.rotate(currentDegree);
        //mCustomImage.r
        currentDegree = -degree;

        this.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}