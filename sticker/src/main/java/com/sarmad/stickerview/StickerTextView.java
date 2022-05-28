package com.sarmad.stickerview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sarmad.stickerview.collageviews.MultiTouchListener;
import com.sarmad.stickerview.util.MagicTextView;
import com.sarmad.stickerview.util.Sticker;
import com.sarmad.stickerview.util.StickerOperationListener;

/**
 * Created by Sarmad on 6/15/15.
 */
public class StickerTextView extends Sticker {
    public static final Paint.Align CENTER = Paint.Align.CENTER;
    public static final Paint.Align LEFT = Paint.Align.LEFT;
    public static final Paint.Align RIGHT = Paint.Align.RIGHT;

    private final View selectionWraperLayout;
    int currentFontStyle = Typeface.NORMAL;
    private final MagicTextView tv_main;
    private Rotate3dAnimation rotate3dAnimation;
    private int xRotate, yRotate, zRotate;
    private StickerOperationListener stickerOperationListener;
    private boolean isLocked = false;

    public StickerTextView(Context context) {
        super(context);
        selectionWraperLayout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.selection_wrraper, this, false);
        setClipChildren(false);
        tv_main = new MagicTextView(context);

        tv_main.setBlurMaskFilter(BlurMaskFilter.Blur.NORMAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout) selectionWraperLayout.findViewById(R.id.inner_layout)).addView(tv_main, params);
        selectionWraperLayout.findViewById(R.id.button_remove).setOnClickListener(view -> {
            ((ViewGroup) StickerTextView.this.getParent()).removeView(StickerTextView.this);
            stickerOperationListener.onStickerClosed(StickerTextView.this);
        });
        selectionWraperLayout.findViewById(R.id.button_front).setOnClickListener(view -> StickerTextView.this.bringToFront());
        this.addView(selectionWraperLayout);

        selectionWraperLayout.findViewById(R.id.button_scale).setOnTouchListener(new TextScaleTouchListener());
        tv_main.setTextSize(50);

    }

    public StickerTextView(MagicTextView slogan) {
        super(slogan.getContext());

        selectionWraperLayout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.selection_wrraper, this, false);
        setClipChildren(false);


        tv_main = new MagicTextView(slogan.getContext());
        tv_main.setTextSize(slogan.getTextSize());
        tv_main.setText(slogan.getText());
        tv_main.setTypeFace(slogan.getTypeFace());
        tv_main.setStroke(slogan.getStrokeWidth());
        tv_main.setStrokeColor(slogan.getStrokeColor());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout) selectionWraperLayout.findViewById(R.id.inner_layout)).addView(tv_main, params);
        selectionWraperLayout.findViewById(R.id.button_remove).setOnClickListener(view -> {
            ((ViewGroup) StickerTextView.this.getParent()).removeView(StickerTextView.this);
            stickerOperationListener.onStickerClosed(StickerTextView.this);
        });
        selectionWraperLayout.findViewById(R.id.button_front).setOnClickListener(view -> StickerTextView.this.bringToFront());
        this.addView(selectionWraperLayout);


    }

    public void setTextAlignment(Paint.Align alignment) {
        tv_main.setTextAlignment(alignment);
    }


    public Typeface getTypeFace() {
        return tv_main.getTypeFace();
    }


    public float getTextSize() {
        return tv_main.getTextSize();
    }

    public void setTextSize(float size) {
        tv_main.setTextSize(size);
    }

    public void setMask(Bitmap bitmap, int density) {
        tv_main.setMask(bitmap, density);
    }

    public void setBackgroundColor(int color) {
        selectionWraperLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void clearMask() {

        tv_main.setMask(null, 0);
        tv_main.requestLayout();
        tv_main.invalidate();

    }

    /**
     * lock view and remove all touch and click events
     */
    @Override
    public void lock() {
        setControlsVisibility(false);
        isLocked = true;
    }

    /**
     * unlock the view and enable all events
     */
    @Override
    public void unlock() {
        isLocked = false;
        setControlsVisibility(true);
    }


    /**
     * return true if view is lock and false if not
     *
     * @return boolean
     */
    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void setStickerOperationListener(StickerOperationListener stickerOperationListener) {
        this.stickerOperationListener = stickerOperationListener;
        if (stickerOperationListener == null) {
            setOnTouchListener(null);
        } else
            setOnTouchListener(new MultiTouchListener(stickerOperationListener));
    }

    @Override
    public int getXRotate() {
        return xRotate;
    }

    @Override
    public View getMainView() {
        return tv_main;
    }

    @Override
    public void setXRotate(int xRotate) {

        rotate3dAnimation = new Rotate3dAnimation(this.xRotate, xRotate, yRotate, yRotate, zRotate, zRotate);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(1);
        tv_main.startAnimation(rotate3dAnimation);
        this.xRotate = xRotate;
    }

    @Override
    public int getYRotate() {
        return yRotate;
    }

    @Override
    public void setYRotate(int yRotate) {

        rotate3dAnimation = new Rotate3dAnimation(this.xRotate, xRotate, this.yRotate, yRotate, zRotate, zRotate);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(1);
        tv_main.startAnimation(rotate3dAnimation);
        this.yRotate = yRotate;
    }

    @Override
    public int getZRotate() {
        return zRotate;
    }

    @Override
    public void setZRotate(int zRotate) {

        rotate3dAnimation = new Rotate3dAnimation(this.xRotate, xRotate, yRotate, yRotate, this.zRotate, zRotate);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(1);
        tv_main.startAnimation(rotate3dAnimation);
        this.zRotate = zRotate;
    }

    @Override
    public int getShadowRadius() {
        return tv_main.getShadowRadius();
    }

    @Override
    public void setShadowRadius(int radius) {
        tv_main.setShadowRadius(radius);

    }

    @Override
    public int getShadowColor() {
        return tv_main.getShadowColor();
    }

    @Override
    public void setShadowColor(int color) {
        tv_main.setShadowColor(color);
    }

    @Override
    public void setControlsVisibility(boolean isVisible) {
        if (!isVisible) {

            selectionWraperLayout.findViewById(R.id.main_sticker_container).setBackground(new ColorDrawable(Color.TRANSPARENT));
            selectionWraperLayout.findViewById(R.id.button_remove).setVisibility(View.INVISIBLE);
            selectionWraperLayout.findViewById(R.id.button_front).setVisibility(INVISIBLE);
        } else {
            selectionWraperLayout.findViewById(R.id.main_sticker_container).setBackgroundResource(R.drawable.border_sticker);
            selectionWraperLayout.findViewById(R.id.button_remove).setVisibility(View.VISIBLE);
            selectionWraperLayout.findViewById(R.id.button_front).setVisibility(View.VISIBLE);

        }

    }

    @Override
    public float getAlpha() {
        return tv_main.getAlpha();
    }

    @Override
    public void setAlpha(float alpha) {
        tv_main.setAlpha(alpha);
    }

    @Override
    public int getOffset() {
//        return tv_main.getOffset();
        return 0;
    }

    public void setTypeface(Typeface typeface) {
        if (currentFontStyle != Typeface.NORMAL)
            tv_main.setTypeFace(typeface);
        else
            tv_main.setTypeFace(typeface);

    }

    @Override
    public void setArchAngle(int radius) {
        tv_main.setArchRadius(radius - 360);
        tv_main.enableArch();
        if (tv_main.getArchRadius() <= 0 && tv_main.getArchRadius() >= -8) {
            tv_main.setArchRadius(-8);
            tv_main.disableArch();
        }

    }

    @Override
    public void setShadowStyle(BlurMaskFilter.Blur style) {
        tv_main.setBlurMaskFilter(style);
    }

    public String getText() {
        return tv_main.getText();
    }

    public void setText(String text) {
        tv_main.setText(text);
//        tv_main.setMyText(text);

    }

    public void setTextColor(int color) {
        tv_main.setMask(null, -1);
        tv_main.setTextColor(color);
    }


    @Override
    public void setStrokeMask(Bitmap item, int density) {
        tv_main.setStrokeMask(item, density);
    }

    @Override
    public void setStrokeColor(int color) {
        tv_main.setStrokeColor(color);
    }

    @Override
    public int getStrokeWidth() {
        return tv_main.getStrokeWidth();
    }

    @Override
    public void setStrokeWidth(int width) {
        tv_main.setStroke(width);
    }

    @Override
    public void setTextSpacing(float spacing) {
        tv_main.setLetterSpacing(spacing);
    }

    @Override
    public float getTextSpacingValue() {
        return tv_main.getLetterSpacing();
    }

    @Override
    public int getShadowStrokeWidth() {
        return tv_main.getShadowStrokeWidth();
    }

    @Override
    public void setShadowStrokeWidth(int width) {
        tv_main.setShadowStrokeWidth(width);
    }

    public float getTextWidth() {
        return tv_main.getTextWidth();
    }


    private class TextScaleTouchListener implements OnTouchListener {
        float orgX = 0.0f;
        float orgY = 0.0f;
        float scaleFactor = 0.0f;
        Rect textBounds = new Rect();
        int widthPixels = 0;
        int rndWidth = 0;
        int rndHeight = 0;
        View inLayout;

        public TextScaleTouchListener() {

            widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.orgX = motionEvent.getX();
                    this.orgY = motionEvent.getY();
                    scaleFactor = tv_main.getScaleX();
                    inLayout = selectionWraperLayout.findViewById(R.id.main_sticker_container);
                    break;
                case MotionEvent.ACTION_MOVE & MotionEvent.ACTION_MASK:

//                    if (motionEvent.getX() >= orgX && tv_main.getWidth() < (widthPixels-200)) {
//                        scaleFactor = motionEvent.getX();
//                        Log.e("INCREASE", "Original X = " + orgX + "   Event X = " + motionEvent.getX());
//                        orgX = motionEvent.getX();
//                        tv_main.setTotalScale(scaleFactor);
//
//                    }
//                    else if (motionEvent.getX() <= orgX+100 && tv_main.getWidth() > (widthPixels / 3)) {
//                        orgX = motionEvent.getX();
//                        tv_main.setTotalScale(scaleFactor);
//
//                    }
                    if ((tv_main.getWidth() + motionEvent.getX() <= widthPixels - 200) &&
                            (tv_main.getWidth() + motionEvent.getX() >= (float) (widthPixels / 3))) {
                        scaleFactor = motionEvent.getX();
                        tv_main.setTotalScale(scaleFactor);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (tv_main.getWidth() >= widthPixels - 200)

                        break;


            }

            return true;
        }
    }

}
