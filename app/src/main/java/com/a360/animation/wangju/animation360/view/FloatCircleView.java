package com.a360.animation.wangju.animation360.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.a360.animation.wangju.animation360.R;

/**
 * Created by user on 2016/9/3.
 */
public class FloatCircleView extends View {
    //设置View的宽和高
    private int width = 150;
    private int height = 150;

    //定义画圆和文字的两个画笔
    private Paint circlePaint;
    private Paint textPaint;

    //text文本
    private String text = "50%";
    //是否正在拖动
    private boolean isDragging = false;
    //拖拽后图片变化显示的bitmap
    private Bitmap bitmap;

    public FloatCircleView(Context context) {
        this(context, null);
    }

    public FloatCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        Bitmap src = BitmapFactory.decodeResource(getResources(),
                R.mipmap.wj);
        bitmap = Bitmap.createScaledBitmap(src, width, height, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isDragging) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        } else {
            canvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = (metrics.descent + metrics.ascent) / 2;
            float y = height / 2 - dy;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setDragState(boolean b) {
        isDragging = b;
        invalidate();
    }
}
