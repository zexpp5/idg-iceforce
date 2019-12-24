package com.utils;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;



/**
 * @author zjh
 * Created by root on 15-7-28.
 */
public class AnimUtils {
    /**
     * 获取点赞动画
     * @return
     */
    public static Animation getPraiseAnimation(){
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new  ScaleAnimation(1f,1.5f,1f,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f,1.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(300);
        return animationSet;
    }

    /**
     * 获取欢迎页的next动画
     * @return
     */
    public static Animation getWelcomNextAnimation(Context context){
//        TranslateAnimation animation = new TranslateAnimation(0, DensityUtil.dip2px(context,20),0,0);
//        animation.setDuration(800);
//        animation.setRepeatCount(Integer.MAX_VALUE);
//        return animation;
        return null;
    }

    public static Animation getScaleAnimation(int durationMillis) {
		ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setDuration(durationMillis);
        return animation;
	}

}
