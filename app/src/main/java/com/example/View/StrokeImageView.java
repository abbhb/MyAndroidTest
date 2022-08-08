package com.example.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.myapplicationtest.R;

import java.util.Arrays;

public class StrokeImageView extends AppCompatImageView {

    public static final int SHAPE_MODE_ROUND_RECT = 1;
    public static final int SHAPE_MODE_CIRCLE = 0;

    private int mShapeMode = 0; // 描边的形状
    private float mRadius = 0;
    private int mStrokeColor = 0x000000;
    private float mStrokeWidth = 0;
    private boolean mShapeChanged;

    private Shape mMainShape;// 内部的圆形
    private Shape mCircleShape;// 描边的圆形
    private Paint mMainPaint, mStrokePaint;
    private Bitmap mStrokeBitmap;

    public StrokeImageView(Context context) {
        this(context, null);
    }

    public StrokeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setLayerType(LAYER_TYPE_HARDWARE, null);// 硬件加速建议关闭
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StrokeImageView);
            mShapeMode = a.getInt(R.styleable.StrokeImageView_shapeMode, 0);
            mRadius = a.getDimension(R.styleable.StrokeImageView_roundRadius, 0);

            mStrokeWidth = a.getDimension(R.styleable.StrokeImageView_strokeWidth, 0);
            mStrokeColor = a.getColor(R.styleable.StrokeImageView_strokeColor, mStrokeColor);
            a.recycle();
        }

        mMainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMainPaint.setFilterBitmap(true);
        mMainPaint.setColor(Color.GREEN);
        mMainPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));// PorterDuff.Mode.DST_IN 显示相交的 DST部分，将多余的边框裁剪掉

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setFilterBitmap(true);
        mStrokePaint.setColor(Color.RED);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || mShapeChanged) {
            mShapeChanged = false;

            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            switch (mShapeMode) {
                case SHAPE_MODE_ROUND_RECT:
                    break;
                case SHAPE_MODE_CIRCLE:
                    int min = Math.min(width, height);
                    mRadius = (float) min / 2;
                    break;
            }

            if (mMainShape == null || mRadius != 0) {
                float[] radius = new float[8];
                Arrays.fill(radius, mRadius);
                mMainShape = new RoundRectShape(radius, null, null);
                mCircleShape = new RoundRectShape(radius, null, null);
            }
            mMainShape.resize(width, height);
            mCircleShape.resize(width - mStrokeWidth * 2, height - mStrokeWidth * 2);

            createStrokeBitmap();

        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mStrokeWidth > 0 && mCircleShape != null && mStrokeBitmap != null) {
            int i = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
            mStrokePaint.setXfermode(null);
            canvas.drawBitmap(mStrokeBitmap, 0, 0, mStrokePaint);
            canvas.translate(mStrokeWidth, mStrokeWidth);
            mStrokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));// 以显示的中心圆向外画出要描边的矩形边框
            mCircleShape.draw(canvas, mStrokePaint);
            canvas.restoreToCount(i);
        }

        switch (mShapeMode) {
            case SHAPE_MODE_ROUND_RECT:
            case SHAPE_MODE_CIRCLE:
                if (mMainShape != null) {
                    mMainShape.draw(canvas, mMainPaint);
                }
                break;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mStrokeBitmap == null) createStrokeBitmap();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseStrokeBitmap();
    }

    private Bitmap createStrokeBitmap() {
        if (mStrokeWidth <= 0) return null;

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        if (w == 0 || h == 0) return null;

        releaseStrokeBitmap();

        mStrokeBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(mStrokeBitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(mStrokeColor);
        c.drawRect(new RectF(0, 0, w, h), p);
        return mStrokeBitmap;
    }

    private void releaseStrokeBitmap() {
        if (mStrokeBitmap != null) {
            mStrokeBitmap.recycle();
            mStrokeBitmap = null;
        }
    }


    public void setStroke(int strokeColor, float strokeWidth) {
        if (mStrokeWidth <= 0) return;

        if (mStrokeWidth != strokeWidth) {
            mStrokeWidth = strokeWidth;

            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            if(mCircleShape != null) {
                mCircleShape.resize(width - mStrokeWidth * 2, height - mStrokeWidth * 2);
            }
            postInvalidate();
        }

        if (mStrokeColor != strokeColor) {
            mStrokeColor = strokeColor;

            createStrokeBitmap();
            postInvalidate();
        }
    }

    public void setStrokeColor(int strokeColor) {
        setStroke(strokeColor, mStrokeWidth);
    }

    public void setStrokeWidth(float strokeWidth) {
        setStroke(mStrokeColor, strokeWidth);
    }

    public void setShape(int shapeMode, float radius) {
        mShapeChanged = mShapeMode != shapeMode || mRadius != radius;

        if (mShapeChanged) {
            mShapeMode = shapeMode;
            mRadius = radius;

            mMainShape = null;
            mCircleShape = null;
            requestLayout();
        }
    }

    public void setShapeMode(int shapeMode) {
        setShape(shapeMode, mRadius);
    }

    public void setShapeRadius(float radius) {
        setShape(mShapeMode, radius);
    }

}