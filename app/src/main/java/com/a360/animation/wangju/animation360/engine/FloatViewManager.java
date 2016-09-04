package com.a360.animation.wangju.animation360.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.a360.animation.wangju.animation360.view.FloatCircleView;
import com.a360.animation.wangju.animation360.view.FloatViewMenu;

import java.lang.reflect.Field;

/**
 * Created by user on 2016/9/3.
 */
public class FloatViewManager implements View.OnTouchListener{

    private static FloatViewManager mInstance;
    private Context context;

    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private FloatCircleView circleView;
    private FloatViewMenu floatViewMenu;

    private float startX;
    private float startY;

    //初始按下点的位置，解决拖动过程与点击事件的冲突
    private float x0;
    private float y0;

    private FloatViewManager(final Context context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatCircleView(context);
        circleView.setOnTouchListener(this);
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show();
                //隐藏circleView 显示菜单栏 开启动画
                wm.removeView(circleView);
                showFloatMenuView();
                floatViewMenu.startAnimation();
            }
        });
        floatViewMenu = new FloatViewMenu(context);
    }

    public static FloatViewManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FloatViewManager.class) {
                if (mInstance == null) {
                    mInstance = new FloatViewManager(context);
                }
            }
        }

        return mInstance;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                x0 = event.getRawX();
                y0 = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                float y = event.getRawY();
                float dx = x - startX;
                float dy = y - startY;
                params.x += dx;
                params.y += dy;
                circleView.setDragState(true);
                wm.updateViewLayout(circleView, params);
                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_UP:
                float x1 = event.getRawX();
                if (x1 > getScreenWidth() / 2) {
                    //需要再理解，下边这句代码
                    params.x = getScreenWidth() - circleView.getWidth();
                } else {
                    params.x = 0;
                }
                circleView.setDragState(false);
                wm.updateViewLayout(circleView, params);
                if (Math.abs(x1 - x0) > 6) {
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    /**
     * 获取屏幕的宽度
     * @return
     */
    public int getScreenWidth() {
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     * @return
     */
    public int getScreenHeight() {
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取状态栏的高,通过反射
     */
    public int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R&dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(0);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 显示浮窗小球到窗口上
     */
    public void showFloatCircleView() {
        params = new WindowManager.LayoutParams();
        params.width = circleView.getWidth();
        params.height = circleView.getHeight();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        wm.addView(circleView, params);
    }

    public void showFloatMenuView() {
        params = new WindowManager.LayoutParams();
        params.width = getScreenWidth();
        params.height = getScreenHeight() - getStatusHeight();
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        wm.addView(floatViewMenu, params);
    }
}
