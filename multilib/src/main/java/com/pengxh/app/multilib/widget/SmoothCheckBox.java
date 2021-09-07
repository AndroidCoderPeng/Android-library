package com.pengxh.app.multilib.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.pengxh.app.multilib.R;
import com.pengxh.app.multilib.utils.SizeUtil;

/**
 * @description: 自定义多选框
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2019/12/14 19:01
 */
public class SmoothCheckBox extends View implements Checkable, View.OnClickListener {

    private final static float BOUNCE_VALUE = 0.2f;

    private Drawable checkDrawable;

    private Paint backgroundPaint;
    private Paint backgroundEraser;
    private Paint checkEraser;
    private Paint borderPaint;

    private Bitmap drawBitmap;
    private Bitmap checkBitmap;
    private Canvas bitmapCanvas;
    private Canvas checkCanvas;

    private float progress;
    private ObjectAnimator checkAnim;

    private boolean attachedToWindow;
    private boolean isChecked;

    private int checkBoxSize = SizeUtil.dp2px(getContext(), 30);
    private int bitmapColor;
    private int borderColor;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);//颜色混合效果，简单解释就是动态渐变颜色

    public SmoothCheckBox(Context context) {
        this(context, null);
    }

    public SmoothCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        setOnClickListener(this);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SmoothCheckBox);
        bitmapColor = typedArray.getColor(R.styleable.SmoothCheckBox_background_color, 0xFF09BA07);
        borderColor = typedArray.getColor(R.styleable.SmoothCheckBox_stroke_color, 0xFF707070);

        //画笔效果
        int paintFlag = Paint.ANTI_ALIAS_FLAG;//抗锯齿，画出来的线条更柔和
        backgroundPaint = new Paint(paintFlag);
        backgroundEraser = new Paint(paintFlag);
        backgroundEraser.setColor(0);
        backgroundEraser.setXfermode(xfermode);
        checkEraser = new Paint(paintFlag);
        checkEraser.setColor(0);
        checkEraser.setStyle(Paint.Style.STROKE);
        checkEraser.setXfermode(xfermode);
        borderPaint = new Paint(paintFlag);
        borderPaint.setStyle(Paint.Style.STROKE);//空心圆
        borderPaint.setStrokeWidth(5);
        checkDrawable = context.getResources().getDrawable(R.drawable.check);
        setVisibility(VISIBLE);
        typedArray.recycle();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE && drawBitmap == null) {
            int reallySize = SizeUtil.dp2px(getContext(), checkBoxSize);
            //长宽相等
            drawBitmap = Bitmap.createBitmap(reallySize, reallySize, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(drawBitmap);
            checkBitmap = Bitmap.createBitmap(reallySize, reallySize, Bitmap.Config.ARGB_8888);
            checkCanvas = new Canvas(checkBitmap);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = checkBoxSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = checkBoxSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility() != VISIBLE) {
            return;
        }
        checkEraser.setStrokeWidth(checkBoxSize);
        drawBitmap.eraseColor(0);
        //位运算，右移相当于 ÷2
        float radius = getMeasuredWidth() >> 1;//半径

        float bitmapProgress = progress >= 0.5f ? 1.0f : progress / 0.5f;
        float checkProgress = progress < 0.5f ? 0.0f : (progress - 0.5f) / 0.5f;

        float p = isChecked ? progress : (1.0f - progress);
        if (p < BOUNCE_VALUE) {
            //动态改变半径大小
            radius -= SizeUtil.dp2px(getContext(), 2) * p;
        } else if (p < BOUNCE_VALUE * 2) {
            radius -= (1 - p) * SizeUtil.dp2px(getContext(), 2);
        }

        float centerX = getMeasuredWidth() >> 1;//圆心X坐标
        float centerY = getMeasuredHeight() >> 1;//圆心Y坐标

        borderPaint.setColor(borderColor);
        canvas.drawCircle(centerX, centerY, radius - SizeUtil.dp2px(getContext(), 1), borderPaint);

        backgroundPaint.setColor(bitmapColor);

        bitmapCanvas.drawCircle(centerX, centerY, radius, backgroundPaint);
        bitmapCanvas.drawCircle(centerX, centerY, radius * (1 - bitmapProgress), backgroundEraser);
        canvas.drawBitmap(drawBitmap, 0, 0, null);

        checkBitmap.eraseColor(0);
        //设置图标大小
        //得到的非图片固有属性，而是与设备相关的值。在适配材料充足的情况用这个较合适，但是如果没做适配的情况下，建议宽高写死
//        int w = checkDrawable.getIntrinsicWidth();
//        int h = checkDrawable.getIntrinsicHeight();

        int w = 72;
        int h = 72;
        int x = (getMeasuredWidth() - w) / 2;
        int y = (getMeasuredHeight() - h) / 2;

        checkDrawable.setBounds(x, y, x + w, y + h);
        checkDrawable.draw(checkCanvas);
        checkCanvas.drawCircle(centerX, centerY, radius * (1 - checkProgress), checkEraser);

        canvas.drawBitmap(checkBitmap, 0, 0, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
    }


    public void setProgress(float value) {
        if (progress == value) {
            return;
        }
        progress = value;
        invalidate();
    }

    public void setSize(int size) {
        this.checkBoxSize = size;
    }

    public float getProgress() {
        return progress;
    }

    public void setCheckedColor(int value) {
        bitmapColor = value;
    }

    public void setBorderColor(int value) {
        borderColor = value;
        borderPaint.setColor(borderColor);
    }

    private void cancelAnim() {
        if (checkAnim != null) {
            checkAnim.cancel();
        }
    }

    private void addAnim(boolean isChecked) {
        checkAnim = ObjectAnimator.ofFloat(this, "progress", isChecked ? 1.0f : 0.0f);
        checkAnim.setDuration(300);
        checkAnim.start();
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checked == isChecked) {
            return;
        }
        isChecked = checked;
        if (attachedToWindow && animated) {
            addAnim(checked);
        } else {
            cancelAnim();
            setProgress(checked ? 1.0f : 0.0f);
        }
    }


    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public void setChecked(boolean b) {
        setChecked(b, true);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void onClick(View view) {
        toggle();
    }
}