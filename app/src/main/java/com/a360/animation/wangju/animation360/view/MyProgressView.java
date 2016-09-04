package com.a360.animation.wangju.animation360.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by user on 2016/9/4.
 */
public class MyProgressView extends View {

    private int width = 200;
    private int height = 200;

    private Paint circlePaint;
    private Paint progressPaint;
    private Paint textPaint;

    private Bitmap bitmap;
    private Path path;
    private Canvas bitmapCanvas;

    private int progress = 50;
    private int max = 100;
    private int currentProgress = 0;

    private boolean isSingleTap = false;
    private DoubleTapRunnable doubleTapRunnable = new DoubleTapRunnable();
    private SingleTapRunnable singleTapRunnable = new SingleTapRunnable();

    private int count = 50;

    private Handler mUpdateProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public MyProgressView(Context context) {
        this(context, null);
    }

    public MyProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.argb(0xff, 0x3a, 0x8c, 0x6c));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.argb(0xff, 0x4e, 0xc9, 0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        path = new Path();

        final GestureDetector detector = new GestureDetector(new MyGestureDetectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmapCanvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
        float y = (1- (float)currentProgress / max) * height;
        path.reset();
        path.moveTo(width, y);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(0, y);
        if (!isSingleTap) {
            float d = (1- (float)currentProgress / progress) * 10;
            for (int i = 0; i < 5; i++) {
                path.rQuadTo(10, -d, 20, 0);
                path.rQuadTo(10, d, 20 , 0);
            }
        } else {
            //振幅的计算
            float d = (float)count / 50 * 10;
            if (count % 2 == 0) {
                for (int i = 0; i < 5; i++){
                    path.rQuadTo(20, -d, 40, 0);
                    path.rQuadTo(20, d, 40, 0);
                }
            } else {
                for (int i = 0; i < 5; i++){
                    path.rQuadTo(20, d, 40, 0);
                    path.rQuadTo(20, -d, 40, 0);
                }
            }
        }

        path.close();

        bitmapCanvas.drawPath(path, progressPaint);
        String text = (int)((float)(currentProgress / max) * 100) + "%";
        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float baseLine = (height - metrics.ascent - metrics.descent) / 2;
        bitmapCanvas.drawText(text, width / 2 - textWidth / 2, baseLine, textPaint);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(getContext(), "双击啦", Toast.LENGTH_SHORT).show();
            startDoubleTapAnimation();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(getContext(), "单击啦", Toast.LENGTH_SHORT).show();
            isSingleTap = true;
            currentProgress = progress;
            startSingleTapAnimation();
            return super.onSingleTapConfirmed(e);
        }

    }

    private void startDoubleTapAnimation() {
        mUpdateProgressHandler.postDelayed(doubleTapRunnable, 50);
    }

    private void startSingleTapAnimation() {
        mUpdateProgressHandler.postDelayed(singleTapRunnable, 200);
    }

    class DoubleTapRunnable implements Runnable {

        @Override
        public void run() {
            currentProgress++;
            if (currentProgress <= progress) {
                //子线程中使用postInvalidate()方法，在runnable中使用invalidate()方法
                invalidate();
                mUpdateProgressHandler.postDelayed(doubleTapRunnable, 50);
            } else {
                mUpdateProgressHandler.removeCallbacks(doubleTapRunnable);
                currentProgress = 0;
            }
        }
    }

    class SingleTapRunnable implements Runnable {

        @Override
        public void run() {
            count--;
            if (count >= 0) {
                invalidate();
                mUpdateProgressHandler.postDelayed(singleTapRunnable, 200);
            } else {
                mUpdateProgressHandler.removeCallbacks(singleTapRunnable);
                count = 50;
            }
        }
    }
}
