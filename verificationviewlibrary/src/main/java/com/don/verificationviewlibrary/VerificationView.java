package com.don.verificationviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * <p>
 * Description：验证码控件
 * </p>
 *
 * @author tangzhijie
 */
public class VerificationView extends View {

    /**
     * 使用wrap_content时默认的尺寸
     */
    private static final int DEFAULT_WIDTH = 240;
    private static final int DEFAULT_HEIGHT = 120;

    /**
     * 背景颜色
     */
    private static final int BG_COLOR = Color.WHITE;

    /**
     * 圆点颜色(如果与背景色设置一样的颜色，相当于不现实干扰圆点)
     */
    private static final int CIRCLE_COLOR = Color.RED;

    /**
     * 干扰线的颜色
     */
    private static final int LINE_COLOR = Color.RED;

    /**
     * 文本的颜色
     */
    private static final int TEXT_COLOR = Color.RED;

    /**
     * 随机圆点的数量
     */
    private static final int POINT_NUM = 50;

    /**
     * 随机干扰线的数量
     */
    private static final int LINE_NUM = 4;

    /**
     * 随机干扰线的宽度
     */
    private static final int LINE_WIDTH = 5;

    /**
     * 干扰线是否使用随机色
     */
    private static final boolean IS_LINECOLOR_RANDOM = true;

    /**
     * 文字是否使用随机色
     */
    private static final boolean IS_TEXTCOLOR_RANDOM = true;

    /**
     * 测量文本大小
     */
    private Rect textBounds = new Rect();

    /**
     * 文本
     */
    private String[] texts = new String[4];
    private String textString;

    private Paint mPaint;

    private Random random = new Random();

    /**
     * 绘制验证码的画布
     */
    private Bitmap mBitmap;
    private Canvas mCanvas;

    public VerificationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationView(Context context) {
        super(context);
        init();
    }

    public VerificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //文本的大小
        int textSize = sp2px(getContext(), 24);
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        randomText();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == MeasureSpec.AT_MOST
                && measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (measureWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, measureHeightSize);
        } else if (measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidthSize, DEFAULT_HEIGHT);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        drawCode();
    }

    /**
     * 绘制验证码
     */
    private void drawCode() {
        //绘制背景
        mCanvas.drawColor(BG_COLOR);

        mPaint.setColor(CIRCLE_COLOR);
        //绘制随机圆点
        for (int i = 0; i < POINT_NUM; i++) {
            int radius = random.nextInt(8);
            int[] points = randomPoints();
            mCanvas.drawCircle(points[0], points[1], radius, mPaint);
        }

        //绘制随机线
        mPaint.setStrokeWidth(LINE_WIDTH);
        mPaint.setColor(LINE_COLOR);
        for (int i = 0; i < LINE_NUM; i++) {
            //判断是否设置随机颜色
            if (IS_LINECOLOR_RANDOM) {
                mPaint.setColor(randomColor());
            }
            int[] lines = randomLines();
            mCanvas.drawLine(lines[0], lines[1], lines[2], lines[3], mPaint);
        }

        //数字之间的间隙
        int textGap = (getMeasuredWidth() - textBounds.width()) / 5;
        mPaint.setColor(TEXT_COLOR);
        //绘制文本
        for (int i = 0; i < 4; i++) {
            //判断是否设置随机颜色
            if (IS_TEXTCOLOR_RANDOM) {
                mPaint.setColor(randomColor());
            }
            int x = textGap * (i + 1) + textBounds.width() / 4 * i;
            mCanvas.drawText(texts[i], x, randomY(), mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 随机生成数字的Y坐标
     */
    private int randomY() {
        int y = (int) ((getMeasuredHeight() / 2 + textBounds.height()) * random.nextFloat());
        if (y < textBounds.height()) {
            y = textBounds.height();
        }
        return y;
    }

    /**
     * 生成随机的圆心
     */
    private int[] randomPoints() {
        int[] points = new int[2];
        points[0] = (int) (random.nextFloat() * getMeasuredWidth());
        points[1] = (int) (random.nextFloat() * getMeasuredHeight());
        return points;
    }

    /**
     * 生成随机的线条
     */
    private int[] randomLines() {
        int[] lines = new int[4];
        lines[0] = (int) (random.nextFloat() * getMeasuredWidth());
        lines[1] = (int) (random.nextFloat() * getMeasuredHeight());
        lines[2] = (int) (random.nextFloat() * getMeasuredWidth());
        lines[3] = (int) (random.nextFloat() * getMeasuredHeight());
        return lines;
    }

    /**
     * 生成随机数字
     */
    private void randomText() {
        textString = "";
        for (int i = 0; i < 4; i++) {
            texts[i] = random.nextInt(10) + "";
            textString = textString + texts[i];
        }
        //测量数字的宽高
        mPaint.getTextBounds(textString, 0, textString.length(), textBounds);
    }

    /**
     * 生成随机颜色
     */
    private int randomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }

    /**
     * sp转化为px
     */
    private static int sp2px(Context context, float spValue) {
        final float fontsScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontsScale + 0.5f);
    }

    /**
     * 重新生成验证码
     */
    public void reset() {
        randomText();
        drawCode();
        invalidate();
    }

    /**
     * 获取当前验证码文本
     */
    public String getText() {
        return textString;
    }


}
