package com.sarmad.stickerview.collageviews;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.sarmad.stickerview.util.Sticker;
import com.sarmad.stickerview.util.StickerOperationListener;


public class MultiTouchListener implements OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
//    public boolean isRotateEnabled = true;
    public boolean isTranslateEnabled = true;
//    public boolean isScaleEnabled = true;
//    public float minimumScale = 0.5f;
//    public float maximumScale = 10.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX;
    private float mPrevY;
//    private ScaleGestureDetector mScaleGestureDetector;
    private final StickerOperationListener stickerOperationListener;
    private GestureDetector gestureDetector;
    public MultiTouchListener(StickerOperationListener evnt, Context context) {
//        mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        stickerOperationListener = evnt;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

//    private static float adjustAngle(float degrees) {
//        if (degrees > 180.0f) {
//            degrees -= 360.0f;
//        } else if (degrees < -180.0f) {
//            degrees += 360.0f;
//        }
//
//        return degrees;
//    }

//    private static void move(View view, TransformInfo info) {
//        computeRenderOffset(view, info.pivotX, info.pivotY);
//        adjustTranslation(view, info.deltaX, info.deltaY);
//
//        // Assume that scaling still maintains aspect ratio.
//        float scale = view.getScaleX() * info.deltaScale;
//        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
//        view.setScaleX(scale);
//        view.setScaleY(scale);
//
//        float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
//        view.setRotation(rotation);
//    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        if (!isTranslateEnabled) {
            return true;
        }

        int action = event.getAction();
        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mPrevX = event.getX();
                mPrevY = event.getY();
                stickerOperationListener.onStickerSelected((Sticker)view);
                stickerOperationListener.onStickerDragged((Sticker)view,StickerOperationListener.IS_DRAGGING);
                // Save the ID of this pointer.
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position.
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a
                    // gesture.
//                    if (!mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY);
//                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_UP:
                stickerOperationListener.onStickerDragged((Sticker)view,StickerOperationListener.DRAGGING_STOPPED);
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor.
                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mPrevX = event.getX(newPointerIndex);
                    mPrevY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }

                break;
            }
        }

        return true;
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            stickerOperationListener.onStickerDoubleTapped();
            return true;
        }


    }

}