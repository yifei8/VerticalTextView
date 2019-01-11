package com.sjtu.yifei.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class VerticalTextView extends View {

    private static final String TAG = "VerticalTextView";
    private String text;
    private int textColor;
    private int textSize;
    private int rowSpacing;
    private int columnSpacing;
    private int columnLength;
    private int maxColumns;


    private TextPaint textPaint;
    private int width;
    private int height;
    private List<String> columnTexts;

    private boolean atMostHeight = true; //是否使用包裹字体的高度，减少底部可能出现的空白区域

    public VerticalTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalTextView, defStyle, 0);

        text = a.getString(R.styleable.VerticalTextView_text);
        textColor = a.getColor(R.styleable.VerticalTextView_textColor, Color.BLACK);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        textSize = a.getDimensionPixelSize(R.styleable.VerticalTextView_textSize, textSize);
        rowSpacing = a.getDimensionPixelSize(R.styleable.VerticalTextView_rowSpacing, rowSpacing);
        columnSpacing = a.getDimensionPixelSize(R.styleable.VerticalTextView_columnSpacing, columnSpacing);
        columnLength = a.getInteger(R.styleable.VerticalTextView_columnLength, -1);
        maxColumns = a.getInteger(R.styleable.VerticalTextView_maxColumns, -1);
        atMostHeight = a.getBoolean(R.styleable.VerticalTextView_atMostHeight, true);

        a.recycle();

        // Set up a default TextPaint object
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private int charWidth;
    private int charHeight;
    private int textCountSize;
    private Paint.FontMetrics fontMetrics;

    private void invalidateTextPaintAndMeasurements() {
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
//        textPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = textPaint.getFontMetrics();
        charHeight = (int) (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent) + Math.abs(fontMetrics.leading));
        char[] chars = text.toCharArray();
        textCountSize = chars.length;
        for (char aChar : chars) {
            float tempWidth = textPaint.measureText(aChar + "");
            if (charWidth < tempWidth) {
                charWidth = (int) tempWidth;
            }
        }
        columnTexts = new ArrayList<>();
    }

    private void updateColumnTexts(int count) {
        columnTexts.clear();
        int i = count;
        for (; i < text.length(); i = i + count) {
            columnTexts.add(text.substring(i - count, i));
        }
        if (i - count < text.length()) {
            columnTexts.add(text.substring(i - count));
        }
        Log.e(TAG, "text:" + columnTexts.toString());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize - getPaddingTop() - getPaddingBottom();
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = heightSize - getPaddingTop() - getPaddingBottom();
            if (!TextUtils.isEmpty(text)) {
                height = Math.min(height, charHeight * text.length());
            }
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize - getPaddingLeft() - getPaddingRight();
        } else {
            int columnCount = (height - charHeight) / (charHeight + rowSpacing) + 1;//一列的字符个数
            if (columnLength > 0) {
                columnCount = columnLength;
                atMostHeight = true;
            }
            if (atMostHeight) {
                height = (charHeight + rowSpacing) * (columnCount - 1) + charHeight + (int) (Math.abs(fontMetrics.descent));
            }
            int column = textCountSize / columnCount + (textCountSize % columnCount > 0 ? 1 : 0);
            width = (charWidth + columnSpacing) * (column - 1) + charWidth;
            width = Math.min(width, widthSize - getPaddingLeft() - getPaddingRight());
            updateColumnTexts(columnCount);
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int x = 0;
        int y = 0;
        for (int i = 0; i < columnTexts.size(); i++) { //按列画
            x = i == 0 ? paddingLeft : x + charWidth + columnSpacing;
            char[] chars = columnTexts.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                y = j == 0 ? paddingTop + charHeight : y + charHeight + rowSpacing;
                canvas.drawText(chars[j] + "", x, y, textPaint);
            }
        }


    }

    /**
     * Gets the  string attribute value.
     *
     * @return The string attribute value.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the view's  string attribute value. In the  view, this string
     * is the text to draw.
     *
     * @param text The string attribute value to use.
     */
    public void setText(String text) {
        this.text = text;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Sets the view's  color attribute value. In the  view, this color
     * is the font color.
     *
     * @param textColor The color attribute value to use.
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidateTextPaintAndMeasurements();
    }

    private int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
