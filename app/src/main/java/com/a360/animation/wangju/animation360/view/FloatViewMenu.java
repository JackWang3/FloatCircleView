package com.a360.animation.wangju.animation360.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.a360.animation.wangju.animation360.R;

/**
 * Created by user on 2016/9/4.
 */
public class FloatViewMenu extends RelativeLayout{

    private LinearLayout ll;
    private TranslateAnimation animation;

    public FloatViewMenu(Context context) {
        super(context);
        View root = View.inflate(getContext(), R.layout.float_menu_view, null);
        ll = (LinearLayout) root.findViewById(R.id.ll);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ll.setAnimation(animation);
        addView(root);
    }

    public void startAnimation() {
        animation.start();
    }
}
