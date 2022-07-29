package com.sarmad.stickerview.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
        throw new UnsupportedOperationException();

    }

    public  void setArchAngle(int angelValue){
        Log.e("STICKER","Method only supported on stickerTextView");
        throw new UnsupportedOperationException();

    }

    public  void setShadowStyle(BlurMaskFilter.Blur style){
        Log.e("STICKER","only supported for sticker text view");
        throw new UnsupportedOperationException();


    }

    /**
     * @param color color for the text
     * */
    public void setStrokeColor(int color) {
        Log.e("STICKER","setStrokeColor only supported for Sticker TextView");
        throw new UnsupportedOperationException();

    }

    public void setStrokeWidth(int strokeWidth) {
        Log.e("STICKER","setStrokeWidth only supported for StickerTextView");
        throw new UnsupportedOperationException();

    }

    public  int getStrokeWidth(){
        Log.e("STICKER","setStrokeWidth only supported for StickerTextView");
        throw new UnsupportedOperationException();
    }

    public void setStrokeMask(Bitmap item,int density) {
        Log.e("STICKER","setStrokeMask only supported for StickerTextView");
        throw new UnsupportedOperationException();
    }

    public void setTextSpacing(float spacing) {
        Log.e("STICKER","setTextSpacing only supported for StickerTextView");
        throw new UnsupportedOperationException();
    }

    public float getTextSpacingValue() {
        Log.e("STICKER","getSpacingValue only supported for StickerTextView");
        throw new UnsupportedOperationException();

    }

    public int getShadowStrokeWidth() {
        Log.e("STICKER","getStrokeWidth only supported for textview");
        throw new UnsupportedOperationException();

    }
    public void editText(){
        Log.e("STICKER","Method only supported for Text Sticker");
        throw new UnsupportedOperationException();
    }


    public static float convertDpToPixel(float dp, Resources r){

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics());
    }

}
