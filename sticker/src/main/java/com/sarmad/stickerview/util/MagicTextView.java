package com.sarmad.stickerview.util;
/**
 * Warning
 */
// every thing in this file is completely nasccersy sooo don't fucking change anything got it

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class MagicTextView extends View {
    private final Paint mPaintText;
    private final Paint strokePaint;
    private final Paint shadowPaint;
    private final Rect textBounds;
    private final Paint boxPaint;
    private final RectF boxRect;
    private final float vOffset = 0f;
    private float textSize = 0.0f;
    private int mRadius = 0;
    private int width = 0;
    private int shadowRadius = 1;
    private int diffLeftCurve = 0;
    private int color = Color.BLACK;
    private int strokeWidth = 0;
    private int shadowColor = Color.WHITE;
    private int strokeColor = Color.WHITE;
    private boolean isArcAngleChanged = false;
    private BlurMaskFilter.Blur blurMaskFilter = BlurMaskFilter.Blur.OUTER;
    private int screenWidth = 0;
    private String message = "Hemant Vitthalbhai Patel Android Developer at Office beacon, Vadodara";
    //        private String message ="Patel Android this is text ";
    private Path textPath;
    private int shadowStrokeWidth = 0;

    public MagicTextView(Context context) {
        super(context);
        textBounds = new Rect();
//        setBackgroundColor(Color.YELLOW);
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint.setColor(Color.TRANSPARENT);
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        boxPaint.setStyle(Paint.Style.FILL);

        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setTextAlign(Paint.Align.CENTER);


        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setTextAlign(Paint.Align.CENTER);

        boxPaint.setAntiAlias(true);
        boxRect = new RectF();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        textPath = new Path();
    }

    public void setMask(Bitmap bitmap, int density) {
        if (bitmap == null) {
            mPaintText.setShader(null);
        } else {
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / density, bitmap.getHeight() / density, false);
            Shader shader = new BitmapShader(scaleBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mPaintText.setShader(shader);
        }
        invalidate();
        requestLayout();

    }

    public void setStrokeMask(Bitmap bitmap, int density) {
        if (bitmap == null) {
            strokePaint.setShader(null);
        } else {
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / density, bitmap.getHeight() / density, false);
            Shader shader = new BitmapShader(scaleBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            strokePaint.setShader(shader);
        }
        invalidate();
        requestLayout();
    }

    public void setBlurMaskFilter(BlurMaskFilter.Blur blurMaskFilter) {
        this.blurMaskFilter = blurMaskFilter;
        shadowPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, blurMaskFilter));
        invalidate();
    }

    public int getArchRadius() {
        return mRadius;
    }

    public void setArchRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float size) {
        this.textSize = size;
        mPaintText.setTextSize(size);
        invalidate();
    }

    public void setTextColor(int color) {
        this.color = color;
        mPaintText.setColor(color);

    }

    public void setTextAlignment(Paint.Align alignment) {
        mPaintText.setTextAlign(alignment);
        strokePaint.setTextAlign(alignment);
        shadowPaint.setTextAlign(alignment);
    }

    public float getTextWidth() {
        return mPaintText.measureText(message);
    }

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        int textHeight = 0;
        int textWidth = 0;
        int offsetX = 0;
        int offsetY = 0;
        float kerning = 1.0f;
        float scale = 1.0f;
        float kerningValue;
        int textOffsetX = offsetX;
        int textOffsetY = offsetY;
        float innerOffsetX;
        float innerOffsetY;

        float totalWidth = ((float) getWidth());


        strokePaint.setColor(strokeColor);
        strokePaint.setTextSize(mPaintText.getTextSize());
        strokePaint.setTypeface(mPaintText.getTypeface());
        strokePaint.setLetterSpacing(mPaintText.getLetterSpacing());
        strokePaint.setStrokeWidth(strokeWidth);

        strokePaint.setStrokeCap(Paint.Cap.SQUARE);


        shadowPaint.setColor(shadowColor);
        shadowPaint.setTextSize(mPaintText.getTextSize());
        shadowPaint.setTypeface(mPaintText.getTypeface());

        shadowPaint.setLetterSpacing(mPaintText.getLetterSpacing());
        shadowPaint.setStrokeWidth(shadowStrokeWidth);

        width = Math.round(strokePaint.measureText(message));


        if (isArcAngleChanged) {

            String t = message.replaceAll("\n", " ");
            int startAngle;
            int aCurvingAngle = mRadius;
            if (mRadius >= 360) {
                aCurvingAngle = 359;
            } else if (mRadius <= -360) {
                aCurvingAngle = -359;
            }

            kerningValue = ((kerning * 20.0f) * scale) / ((float) (t.length() - 1));
            float hOffset = 1;
            float lineWidth = mPaintText.measureText(t) + (((float) (t.length() - 1)) * kerningValue);
//            if (tmpStrokePaint.getStrokeWidth() > 0.0f) {
//                lineWidth += tmpStrokePaint.getStrokeWidth() * 2.0f;
//                hOffset = 0.0f + tmpStrokePaint.getStrokeWidth();
//            }
            float diameter = (float) (((double) ((360.0f * lineWidth) / ((float) Math.abs(aCurvingAngle)))) / Math.PI);

            int baseWidth = 1;


            innerOffsetX = (((float) (getWidth() - baseWidth)) * 0.5f) * scale;
            innerOffsetY = 0;

            textOffsetX += (int) innerOffsetX;
            textOffsetY += (int) innerOffsetY;

            float left = ((float) textOffsetX) + (((totalWidth - (2.0f * innerOffsetX)) - diameter) / 2.0f);
            float top = (float) textOffsetY;
            if (aCurvingAngle > 0) {
                startAngle = 270;
                top+=30;
                diffLeftCurve = 48;

            } else {
                diffLeftCurve = 0;
                top += (((float) getHeight()) - innerOffsetY) - diameter; // left curve ok
                if (mPaintText.getStrokeWidth() > 0.0f) {
                    top -= mPaintText.getStrokeWidth();
                }

                startAngle = 90;
            }
            this.textPath.reset();



            this.textPath.addArc(left, top, left + diameter, top + diameter, (startAngle - (aCurvingAngle / 2f)), (float) aCurvingAngle);
            textPath.computeBounds(boxRect, true);


            canvas.drawRect(boxRect, boxPaint);

            if (strokeWidth >= 2)
                canvas.drawTextOnPath(t, this.textPath, hOffset, 0.0f, strokePaint);
            if (shadowStrokeWidth >= 2)
                canvas.drawTextOnPath(t, this.textPath, hOffset, 0.0f, shadowPaint);

            canvas.drawTextOnPath(t, this.textPath, hOffset, 0.0f, mPaintText);

            textHeight = (int) boxRect.height()+diffLeftCurve;
            textWidth = (int) Math.abs(boxRect.width()) + 10;


        } else {

            float y = 0;
            if (message.contains("\n")) { // if there are multiple lines then do not draw first line in center becuase if first lines is drawn in center other lines will go below cause one line blank space at the top so draw from start
                y = mPaintText.getTextSize();
            } else { // else if it is single line then draw it in center
                y = (int) ((getHeight() / 2) - ((mPaintText.descent() + mPaintText.ascent()) / 2));
            }
            float x = getTextXCoordinates(mPaintText.getTextAlign()); //get text X coordinates based on text alignment

            String[] msg = message.split("\n");
            int lastTextWidth = 0;
            Paint.FontMetrics mtr = mPaintText.getFontMetrics();
            for (String text : msg) {
                if (strokeWidth >= 2)
                    canvas.drawText(text, x, y, strokePaint);
                if (shadowStrokeWidth >= 2)
                    canvas.drawText(text, x, y, shadowPaint);

                canvas.drawText(text, x, y, mPaintText);
                mPaintText.getTextBounds(text, 0, text.length(), textBounds);
                textHeight += Math.abs(mtr.descent - mtr.ascent); // save the text hight w.r.t lines and height of font to change the view height accourdingly
                textWidth = textBounds.width();
                y += (mPaintText.descent() - mPaintText.ascent()); // text size +textbounds height
                if (textWidth < lastTextWidth) { // set the text width accorduing to the largest line in paragraph
                    textWidth = lastTextWidth;
                }
                lastTextWidth = textWidth;


            }
            if(textWidth+110 >= screenWidth) {
                setTextSize(getTextSize()-10);
            }

        }

        getLayoutParams().width = textWidth + 5;
        getLayoutParams().height = textHeight;
        requestLayout();

    }

    /**
     * @param alignment current text alignment
     * @return X coordinates for text according to alignment
     */
    private float getTextXCoordinates(Paint.Align alignment) {
        return alignment == Paint.Align.CENTER ? getWidth() / 2f : alignment == Paint.Align.RIGHT ? getWidth() : 0;
    }

    private int calculateArea(Region region) {

        RegionIterator regionIterator = new RegionIterator(region);

        int size = 0; // amount of Rects
        float area = 0; // units of area

        Rect tmpRect = new Rect();

        while (regionIterator.next(tmpRect)) {
            size++;
            area = tmpRect.height();
        }
        Log.e("Rect amount=", +size + "");
        Log.d("units of area=", +area + "");
        return size;

    }

    public void setStroke(int strokeWidth) {
        if (strokeWidth >= 1) {
            this.strokeWidth = strokeWidth;
            invalidate();
        }
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(int radius) {
        if (radius >= 1) {
            this.shadowRadius = radius;
            shadowPaint.setMaskFilter(new BlurMaskFilter(radius, blurMaskFilter));
            invalidate();
        }


    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int color) {
        this.shadowColor = color;
        invalidate();
    }

    public String getText() {
        return message;
    }

    public void setText(String text) {
        this.message = text;
        invalidate();
    }

    public Typeface getTypeFace() {
        return mPaintText.getTypeface();
    }

    public void setTypeFace(Typeface tv) {

        mPaintText.setTypeface(tv);
        invalidate();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public float getLetterSpacing() {
        return mPaintText.getLetterSpacing();
    }

    public void setLetterSpacing(float spacing) {
        mPaintText.setLetterSpacing(spacing);
        invalidate();
    }

    public int getShadowStrokeWidth() {
        return shadowStrokeWidth;
    }

    public void setShadowStrokeWidth(int shadowStrokeWidth) {
        this.shadowStrokeWidth = shadowStrokeWidth;
        invalidate();
    }

    public int getStrokeColor() {
        return strokePaint.getColor();
    }

    public void setStrokeColor(int color) {
        setStrokeMask(null, -1);
        this.strokeColor = color;

        invalidate();

    }

    public void enableArch() {
        isArcAngleChanged = true;
    }

    public void disableArch() {
        isArcAngleChanged = false;
    }
}
