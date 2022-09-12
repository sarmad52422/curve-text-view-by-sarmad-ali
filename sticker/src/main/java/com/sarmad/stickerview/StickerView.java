package com.sarmad.stickerview;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.sarmad.stickerview.util.Sticker;
import com.sarmad.stickerview.util.StickerOperationListener;


public abstract class StickerView extends Sticker {


    public static final String TAG = "my view sticerk view";
    private final static int BUTTON_SIZE_DP = 20;
    public final static int SELF_SIZE_DP = 100;
    private ScaleCallBack scaleCallBack;
    private GestureDetector gestureDetector;

    private ImageView iv_scale;
    private ImageView iv_delete;
    private ImageView iv_flip;
    private ImageView iv_switch;
    private View innerViewContainer = null;
    private boolean isLocked = false;
    private StickerOperationListener stickerOperationListener;
    // For scalling
    private float this_orgX = -1, this_orgY = -1;
    private float scale_orgX = -1, scale_orgY = -1;
    private double scale_orgWidth = -1, scale_orgHeight = -1;
    // For rotating
    private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;
    // For moving
    private float move_orgX = -1, move_orgY = -1;
    private double centerX, centerY;
    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
            if (view.getTag().equals("DraggableViewGroup")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getMainView().getParent().requestDisallowInterceptTouchEvent(true);

                        Log.v(TAG, "sticker view action down");
                        move_orgX = event.getRawX();
                        move_orgY = event.getRawY();
                        if (stickerOperationListener != null) {
                            stickerOperationListener.onStickerSelected(StickerView.this);
                            stickerOperationListener.onStickerDragged((Sticker)view,StickerOperationListener.IS_DRAGGING);

                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        getMainView().getParent().requestDisallowInterceptTouchEvent(true);

                        Log.v(TAG, "sticker view action move");
                        float offsetX = event.getRawX() - move_orgX;
                        float offsetY = event.getRawY() - move_orgY;
                        StickerView.this.setX(StickerView.this.getX() + offsetX);
                        StickerView.this.setY(StickerView.this.getY() + offsetY);
                        move_orgX = event.getRawX();
                        move_orgY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if(stickerOperationListener != null)
                            stickerOperationListener.onStickerDragged((Sticker)view,StickerOperationListener.DRAGGING_STOPPED);

                        Log.v(TAG, "sticker view action up");
                        break;
                }
            } else if (view.getTag().equals("iv_scale")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "iv_scale action down");


                        this_orgX = innerViewContainer.getX();
                        this_orgY = innerViewContainer.getY();

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();
                        scale_orgWidth = innerViewContainer.getLayoutParams().width;
                        scale_orgHeight = innerViewContainer.getLayoutParams().height;

                        rotate_orgX = event.getRawX();
                        rotate_orgY = event.getRawY();

                        centerX =innerViewContainer.getX() +
                                ((View) innerViewContainer.getParent()).getX() +
                                (float) innerViewContainer.getWidth() / 2;


                        //double statusBarHeight = Math.ceil(25 * getContext().getResources().getDisplayMetrics().density);
                        int result = 0;
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            result = getResources().getDimensionPixelSize(resourceId);
                        }
                        double statusBarHeight = result;
                        centerY = innerViewContainer.getY() +
                                ((View) innerViewContainer.getParent()).getY() +
                                statusBarHeight +
                                (float) innerViewContainer.getHeight() / 2;

                        break;
                    case MotionEvent.ACTION_MOVE:


                        rotate_newX = event.getRawX();
                        rotate_newY = event.getRawY();

                        double angle_diff = Math.abs(
                                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                                        - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

                        Log.v(TAG, "angle_diff: " + angle_diff);

                        double length1 = getLength(centerX, centerY, scale_orgX, scale_orgY);
                        double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

                        int size = convertDpToPixel(SELF_SIZE_DP, getContext());
                        if (length2 > length1
                                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                                ) {
                            //scale up
                            Log.e("StickerView","Scaling Up");
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            innerViewContainer.getLayoutParams().width += offset;
                            innerViewContainer.getLayoutParams().height += offset;
                            getImageView().getLayoutParams().width +=offset;
                            getImageView().getLayoutParams().height +=offset;
                            onScaling(true);
                            Log .e("STICHER OF","oofff set = "+innerViewContainer.getLayoutParams().width);


                            int factor = Math.max(innerViewContainer.getLayoutParams().width, innerViewContainer.getLayoutParams().height);

                            scaleCallBack.onScale(factor/500);


                            //DraggableViewGroup.this.setX((float) (getX() - offset / 2));
                            //DraggableViewGroup.this.setY((float) (getY() - offset / 2));
                        } else if (length2 < length1
                                && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)
                                && innerViewContainer.getLayoutParams().width > size / 2
                                && innerViewContainer.getLayoutParams().height > size / 2) {
                            //scale down
                            double offsetX = Math.abs(event.getRawX() - scale_orgX);
                            double offsetY = Math.abs(event.getRawY() - scale_orgY);
                            double offset = Math.max(offsetX, offsetY);
                            offset = Math.round(offset);
                            innerViewContainer.getLayoutParams().width -= offset;
                            innerViewContainer.getLayoutParams().height -= offset;
                            getImageView().getLayoutParams().width -=offset;
                            getImageView().getLayoutParams().height -=offset;

                            int factor = Math.max(innerViewContainer.getLayoutParams().width, innerViewContainer.getLayoutParams().height);

                            scaleCallBack.onScale(factor/500);
                            onScaling(false);
                        }

                        //rotate

//                        double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
//                        Log.v(TAG, "log angle: " + angle);

                        //setRotation((float) angle - 45);
//                        setRotation((float) angle - 45);
//                        Log.v(TAG, "getRotation(): " + getRotation());

//                        onRotating();

                        rotate_orgX = rotate_newX;
                        rotate_orgY = rotate_newY;

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();

                        innerViewContainer.postInvalidate();
                        innerViewContainer.requestLayout();

                        break;
                    case MotionEvent.ACTION_UP:
                        getLayoutParams().width = innerViewContainer.getLayoutParams().width;
                        getLayoutParams().height = innerViewContainer.getLayoutParams().height;
                        requestLayout();

                        break;
                }
            }
            return true;
        }
    };
    public void setStickerSize(int width,int height){
        innerViewContainer.getLayoutParams().width = width;
        innerViewContainer.getLayoutParams().height = height;
        innerViewContainer.requestLayout();

    }
    public StickerView(Context context) {
        super(context);
        setClipChildren(false);
        init(context);
        gestureDetector = new GestureDetector(context,new GestureListener());


    }



    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
    @Override
    public void setStickerOperationListener(StickerOperationListener stickerOperationListener) {
        this.stickerOperationListener = stickerOperationListener;
    }

    public void setScaleCallBackListener(ScaleCallBack scaleCallBack) {
        this.scaleCallBack = scaleCallBack;
    }
    @Override
    public void lock(){
//       setStickerOperationListener(null);
       setOnTouchListener(null);
       setControlsVisibility(false);
       isLocked = true;

    }
    @Override
    public boolean isLocked(){
        return isLocked;
    }

    @Override
    public void unlock(){
        setOnTouchListener(mTouchListener);
        isLocked = false;
        setControlsVisibility(true);
    }

    private void init(Context context) {
        View mainContainer = LayoutInflater.from(context).inflate(R.layout.selection_wrraper,this,false);
        this.innerViewContainer = mainContainer;
        this.iv_scale = mainContainer.findViewById(R.id.button_scale);
        this.iv_delete = mainContainer.findViewById(R.id.button_remove);
        this.iv_flip = new ImageView(context);
        this.iv_switch = mainContainer.findViewById(R.id.button_front);
        this.iv_flip.setImageResource(R.drawable.flip);
        this.setTag("DraggableViewGroup");
        this.iv_scale.setTag("iv_scale");
        this.iv_delete.setTag("iv_delete");
        this.iv_flip.setTag("iv_flip");
        this.iv_switch.setTag("tv_switch");

        int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;
        int size = convertDpToPixel(SELF_SIZE_DP, getContext());

        FrameLayout.LayoutParams this_params =
                new FrameLayout.LayoutParams(
                        size,
                        size
                );
        this_params.gravity = Gravity.CENTER;

        FrameLayout.LayoutParams iv_main_params =
                new FrameLayout.LayoutParams(
                        size,
                        size
                );
//        iv_main_params.setMargins(margin, margin, margin, margin);
//
//        FrameLayout.LayoutParams iv_border_params =
//                new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                );
//        iv_border_params.setMargins(margin, margin, margin, margin);
//
        FrameLayout.LayoutParams iv_scale_params =
                new FrameLayout.LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
//        iv_scale_params.gravity = Gravity.BOTTOM | Gravity.END;
//
//        FrameLayout.LayoutParams iv_delete_params =
//                new FrameLayout.LayoutParams(
//                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
//                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
//                );
//        iv_delete_params.gravity = Gravity.TOP | Gravity.END;
//
        FrameLayout.LayoutParams iv_flip_params =
                new FrameLayout.LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_flip_params.gravity = Gravity.BOTTOM | Gravity.START;
//        FrameLayout.LayoutParams iv_switch_params = new FrameLayout.LayoutParams(
//                convertDpToPixel(BUTTON_SIZE_DP, getContext()),
//                convertDpToPixel(BUTTON_SIZE_DP, getContext())
//        );
//        iv_switch_params.gravity = Gravity.BOTTOM | Gravity.START;

//        this.setLayoutParams(this_params);
        View mainView = getMainView();
        if(mainView != null){
            ((ViewGroup)mainContainer.findViewById(R.id.inner_layout)).addView(mainView,iv_main_params);
            ((ViewGroup)mainContainer).addView(iv_flip,iv_flip_params);
        }
        this.addView(mainContainer,this_params);
//        if(mainView != null) {
//            mainView.setPadding(20, 20, 20, 20);
//            this.addView(mainView, iv_main_params);
//        }
//        this.addView(iv_border, iv_border_params);
//        this.addView(iv_scale, iv_scale_params);
//        this.addView(iv_delete, iv_delete_params);
//        this.addView(iv_flip, iv_flip_params);
//        this.addView(iv_switch, iv_switch_params);
        this.setOnTouchListener(mTouchListener);
        this.iv_scale.setOnTouchListener(mTouchListener);
        this.iv_switch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StickerView.this.bringToFront();
            }
        });
        this.iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StickerView.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) StickerView.this.getParent());
                    myCanvas.removeView(StickerView.this);
                    stickerOperationListener.onStickerRemoved(StickerView.this);
                }
            }
        });
        this.iv_flip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.v(TAG, "flip the view");

                View mainView = getMainView();
                mainView.setRotationY(mainView.getRotationY() == -180f ? 0f : -180f);
                mainView.invalidate();
                requestLayout();
            }
        });
    }

    protected abstract View getImageView();

    public boolean isFlip() {
        return getMainView().getRotationY() == -180f;
    }

    public abstract View getMainView();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    private float[] getRelativePos(float absX, float absY) {
        Log.v("ken", "getRelativePos getX:" + ((View) this.getParent()).getX());
        Log.v("ken", "getRelativePos getY:" + ((View) this.getParent()).getY());
        float[] pos = new float[]{
                absX - ((View) this.getParent()).getX(),
                absY - ((View) this.getParent()).getY()
        };
        Log.v(TAG, "getRelativePos absY:" + absY);
        Log.v(TAG, "getRelativePos relativeY:" + pos[1]);
        return pos;
    }


    protected View getImageViewFlip() {
        return iv_flip;
    }

    protected void onScaling(boolean scaleUp) {
    }

    protected void onRotating() {
    }

    @Override
    public void setControlsVisibility(boolean isVisible) {
        if (!isVisible) {
            innerViewContainer.findViewById(R.id.main_sticker_container).setBackground(new ColorDrawable(Color.TRANSPARENT));
            innerViewContainer.findViewById(R.id.button_remove).setVisibility(View.INVISIBLE);
            innerViewContainer.findViewById(R.id.button_front).setVisibility(INVISIBLE);
            innerViewContainer.findViewById(R.id.button_scale).setVisibility(INVISIBLE);
            this.iv_flip.setVisibility(INVISIBLE);
        } else {
            innerViewContainer.findViewById(R.id.main_sticker_container).setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.selection_color,null));
            innerViewContainer.findViewById(R.id.button_remove).setVisibility(View.VISIBLE);
            innerViewContainer.findViewById(R.id.button_front).setVisibility(View.VISIBLE);
            innerViewContainer.findViewById(R.id.button_scale).setVisibility(View.VISIBLE);
            this.iv_flip.setVisibility(View.VISIBLE);
        }

    }

    public interface ScaleCallBack {
        void onScale(int factor);
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
           stickerOperationListener.onStickerDoubleTapped();
            return true;
        }


    }




}