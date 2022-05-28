package com.sarmad.stickerview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class StickerImageView extends StickerView implements StickerView.ScaleCallBack {
    private String owner_id;
    private ImageView iv_main;
    Bitmap originalBitmap;
    int imgResourcesID = 0;
    Bitmap maskBitmap;
    private Rotate3dAnimation rotate3dAnimation;
    private int xRotate, yRotate, zRotate;

    public StickerImageView(Context context) {
        super(context);
        setClipChildren(false);
        setScaleCallBackListener(this);



    }
    public void setMask(Bitmap maskBitmap){
        this.iv_main.setColorFilter(0); // if color filter is applied mask will not work removing color filter here if any applied
        if(imgResourcesID == 0 && this.originalBitmap == null)
            return;
        Bitmap orginalCopy = null;
        if(this.originalBitmap == null){
            orginalCopy = BitmapFactory.decodeResource(getResources(),imgResourcesID);
        }
        else{
            orginalCopy = Bitmap.createBitmap(originalBitmap);
        }

        Bitmap mask = Bitmap.createBitmap(orginalCopy.getWidth(),orginalCopy.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        //mask paint
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setShader(new BitmapShader(maskBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
        // image paint
        Paint imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        imagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawRect(0,0,orginalCopy.getWidth(),orginalCopy.getHeight(),maskPaint);
        canvas.drawBitmap(orginalCopy,0f,0f,imagePaint);


        this.iv_main.setImageBitmap(mask);

    }

    @Override
    public void setXRotate(int xRotate) {

        rotate3dAnimation = new Rotate3dAnimation(this.xRotate, xRotate, yRotate, yRotate, zRotate, zRotate);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(1);
        iv_main.startAnimation(rotate3dAnimation);
        this.xRotate = xRotate;
    }
    @Override
    public void setYRotate(int yRotate) {

        rotate3dAnimation = new Rotate3dAnimation(this.xRotate, xRotate, this.yRotate, yRotate, zRotate, zRotate);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(1);
        iv_main.startAnimation(rotate3dAnimation);
        this.yRotate = yRotate;
    }
    @Override
    public void setZRotate(int zRotate) {

        rotate3dAnimation = new Rotate3dAnimation(this.xRotate, xRotate, yRotate, yRotate, this.zRotate, zRotate);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setDuration(1);
        iv_main.startAnimation(rotate3dAnimation);
        this.zRotate = zRotate;
    }
    @Override
    public int getXRotate() {
        return xRotate;
    }
    @Override
    public int getYRotate() {
        return yRotate;
    }

    @Override
    public int getOffset() {
        return 0;
    }




    @Override
    public void clearMask(){
        Log.e("ORG","MASK CLEAR");
        setImageBitmap(originalBitmap);

    }


    @Override
    public int getZRotate() {
        return zRotate;
    }



    @Override
    public int getShadowRadius() {
        throw new UnsupportedOperationException("Method Not Supported For Images");
    }

    @Override
    public int getShadowColor() {
        throw new UnsupportedOperationException("Method Not Supported For Images");
    }

    @Override
    public float getAlpha() {
        return iv_main.getAlpha();
    }

    @Override
    public void setAlpha(float alpha) {
        iv_main.setAlpha(alpha);
    }



    @Override
    public void setShadowRadius(int radius) {
        throw new UnsupportedOperationException("Method Not Supported For Images");
    }

    @Override
    public void setShadowColor(int color) {
        throw new UnsupportedOperationException("Method Not Supported For Images");
    }


    public void setOwnerId(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwnerId() {
        return this.owner_id;
    }

    @Override
    public View getMainView() {
        if (this.iv_main == null) {
            this.iv_main = new ImageView(getContext());

        }

        return iv_main;
    }

    public void setBorder(int borderColor) {


        if (imgResourcesID == 0 && this.originalBitmap == null)// if image view is empty then don't set mask
            return;

        Bitmap org;
        if (this.originalBitmap == null)// if setImageResources
            org = BitmapFactory.decodeResource(getResources(), imgResourcesID); // get the orginal image as a bitmap
        else  // if setImageBitmap
            org = Bitmap.createBitmap(originalBitmap);

        int strokeWidth = 13;
        try {
            Bitmap newStrokedBitmap = Bitmap.createBitmap(org.getWidth() + strokeWidth + 2, org.getHeight() + strokeWidth + 2, org.getConfig());
            Canvas canvas = new Canvas(newStrokedBitmap);
            float scaleX = (org.getWidth() + 2.0f * strokeWidth) / org.getWidth();
            float scaleY = (org.getHeight() + 2.0f * strokeWidth) / org.getHeight();
            Matrix matrix = new Matrix();
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            matrix.setScale(scaleX, scaleY);
            canvas.drawBitmap(org, matrix, paint);
            canvas.drawColor(borderColor, PorterDuff.Mode.SRC_ATOP); //Color.WHITE is stroke color
            canvas.drawBitmap(org, strokeWidth, strokeWidth, paint);
            this.iv_main.setImageBitmap(newStrokedBitmap);
            this.maskBitmap = newStrokedBitmap;
        }catch (Exception exception){
            Toast.makeText(getContext(),"Something Went Wrong :(",Toast.LENGTH_LONG).show();
        }

    }


    public void setImageBitmap(Bitmap bmp) {
        this.originalBitmap = bmp;

        this.iv_main.setImageBitmap(bmp);
    }

    public void setImageResource(int res_id) {
        imgResourcesID = res_id;
        this.iv_main.setImageResource(res_id);
    }

    public void setImageDrawable(Drawable drawable) {
        this.iv_main.setImageDrawable(drawable);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) this.iv_main.getDrawable()).getBitmap();
    }
    public void setColorFilter(int color){
        iv_main.setColorFilter(color);
    }

    @Override
    public View getImageView() {
        return iv_main;
    }


    @Override
    public void onScale(int factor) {
        // restore animation on scale
        Rotate3dAnimation animation = new Rotate3dAnimation(xRotate, xRotate, yRotate, yRotate, zRotate, zRotate);
        animation.setFillAfter(true);
        animation.setDuration(1);
        iv_main.startAnimation(animation);
        // adjust mask on scale up and down

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Rotate3dAnimation animation = new Rotate3dAnimation(xRotate, xRotate, yRotate, yRotate, zRotate, zRotate);
        animation.setFillAfter(true);
        animation.setDuration(1);
        iv_main.startAnimation(animation);
    }

    public void setBorder(int blue, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(originalBitmap.getWidth() + borderSize * 2, originalBitmap.getHeight() + borderSize * 2,originalBitmap.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(blue);
        canvas.drawBitmap(originalBitmap, borderSize, borderSize, null);
        setImageBitmap(bmpWithBorder);
    }
}
