package com.sarmad.stickerview.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by tough on 9/5/17.
 */

public abstract class Sticker extends FrameLayout {
    public Sticker(Context context) {
        super(context);
    }

    /**
     * @param xRotate take rotation parameter for X-axis and rotate the view on X-axis
     * */
    public abstract void setXRotate(int xRotate);

    public abstract void setYRotate(int yRotate);

    public abstract void setZRotate(int zRotate);
    public abstract void setStickerOperationListener(StickerOperationListener stickerOperationListener);
    public abstract int getXRotate();
    public abstract View getMainView();
    public abstract int getYRotate();
    public abstract int getOffset();
    public abstract int getZRotate();
    public abstract int getShadowRadius();
    public abstract int getShadowColor();
    public abstract void setControlsVisibility(boolean isVisible);
    public abstract float getAlpha();
    public abstract void setAlpha(float alpha);
    public abstract void setShadowRadius(int radius);
    public abstract void setShadowColor(int color);
    public abstract void clearMask();

    public abstract void lock();
    public abstract void unlock();
    public abstract boolean isLocked();

    public  void setShadowStrokeWidth(int width){
        Log.e("STICKER","Method only supported on stickerTextView");
    }

    public  void setArchAngle(int angelValue){
        Log.e("STICKER","Method only supported on stickerTextView");
    }

    public  void setShadowStyle(BlurMaskFilter.Blur style){
        Log.e("STICKER","only supported for sticker text view");
    }

    /**
     * @param color color for the text
     * */
    public void setStrokeColor(int color) {
        Log.e("STICKER","setStrokeColor only supported for Sticker TextView");
    }

    public void setStrokeWidth(int strokeWidth) {
        Log.e("STICKER","setStrokeWidth only supported for StickerTextView");
    }

    public  int getStrokeWidth(){
        Log.e("STICKER","setStrokeWidth only supported for StickerTextView");
        return -1;
    }

    public void setStrokeMask(Bitmap item,int density) {
        Log.e("STICKER","setStrokeMask only supported for StickerTextView");

    }

    public void setTextSpacing(float spacing) {
        Log.e("STICKER","setTextSpacing only supported for StickerTextView");

    }

    public float getTextSpacingValue() {
        Log.e("STICKER","getSpacingValue only supported for StickerTextView");
        return -1.0f;

    }

    public int getShadowStrokeWidth() {
        Log.e("STICKER","getStrokeWidth only supported for textview");
        return -1;
    }
    public static float convertDpToPixel(float dp, Resources r){

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics());
    }
}
