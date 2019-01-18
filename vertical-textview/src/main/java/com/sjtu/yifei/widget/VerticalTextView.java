package com.sjtu.yifei.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 垂直文字显示的TextView
 */
public class VerticalTextView extends View {

    private static final String TAG = "VerticalTextView";
    private String text;
    private int textColor;
    private int textSize;
    private int rowSpacing;
    private int columnSpacing;
    private int columnLength;
    private int maxColumns;
    private int textStyle;
    private boolean isCharCenter = false; //字符是否居中展示
    private boolean atMostHeight = true; //是否使用包裹字体的高度，减少底部可能出现的空白区域

    private Paint ellipsisPaint;
    private TextPaint textPaint;
    private int myMeasureWidth;
    private int myMeasureHeight;
    private List<String> columnTexts;

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

    private void init() {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        textColor = 0xff000000;
        textSize = sp2px(getContext(), 14);
        columnSpacing = dp2px(getContext(), 4);
        textStyle = 0;
    }

    private void init(AttributeSet attrs, int defStyle) {
        init();
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
        isCharCenter = a.getBoolean(R.styleable.VerticalTextView_isCharCenter, true);
        textStyle = a.getInt(R.styleable.VerticalTextView_textStyle, textStyle);
        a.recycle();

        // Set up a default TextPaint object
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private boolean isShowEllipsis;
    private int charWidth;
    private int charHeight;
    private Paint.FontMetrics fontMetrics;

    private void invalidateTextPaintAndMeasurements() {
        lastShowColumnIndex = -1;
        isShowEllipsis = false;
        if (columnTexts != null) {
            columnTexts.clear();
        }
        fontMetrics = null;
        invalidateTextPaint();
        invalidateMeasurements();
    }

    private void invalidateTextPaint() {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(isCharCenter ? Paint.Align.CENTER : Paint.Align.LEFT);
        textPaint.setFakeBoldText((textStyle & Typeface.BOLD) != 0);
        textPaint.setTextSkewX((textStyle & Typeface.ITALIC) != 0 ? -0.25f : 0);
        fontMetrics = textPaint.getFontMetrics();
        charHeight = (int) (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent) + Math.abs(fontMetrics.leading));

        if (maxColumns > 0) {
            if (ellipsisPaint == null) {
                ellipsisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/verticalEllipsis.TTF");
                ellipsisPaint.setTypeface(typeface);
                ellipsisPaint.setFakeBoldText((textStyle & Typeface.BOLD) != 0);
                ellipsisPaint.setTextSkewX((textStyle & Typeface.ITALIC) != 0 ? -0.25f : 0);
            }
            ellipsisPaint.setTextSize(textSize);
            ellipsisPaint.setColor(textColor);
            ellipsisPaint.setTextAlign(isCharCenter ? Paint.Align.CENTER : Paint.Align.LEFT);
        }
    }

    private void invalidateMeasurements() {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            float tempWidth = textPaint.measureText(aChar + "");
            if (charWidth < tempWidth) {
                charWidth = (int) tempWidth;
            }
        }
        if (columnTexts == null) {
            columnTexts = new ArrayList<>();
        }
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
    }

    private int lastShowColumnIndex = -1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            myMeasureHeight = heightSize - getPaddingTop() - getPaddingBottom();
        } else {
            if (TextUtils.isEmpty(text)) {
                myMeasureHeight = 0;
            } else {
                myMeasureHeight = heightSize - getPaddingTop() - getPaddingBottom();
                /*
                 * bug fix 当parent是RelativeLayout时，RelativeLayout onMeasure会测量两次，
                 * 当自定义view宽或高设置为wrap_content时，会出现计算出错，显示异常。这是由于
                 * 第一次调用时宽高mode默认是wrap_content类型，size会是parent size。这将导致
                 * 自定义view第一次计算出的size不是我们需要的值，影响第二次正常计算。
                 */
                if (getLayoutParams() != null && getLayoutParams().height > 0) {
                    myMeasureHeight = getLayoutParams().height;
                }
                if (columnLength > 0) {
                    myMeasureHeight = Integer.MIN_VALUE;
                    updateColumnTexts(columnLength);
                    for (int i = 0; i < columnTexts.size(); i++) {
                        myMeasureHeight = Math.max(myMeasureHeight, charHeight * columnTexts.get(i).length());
                    }
                } else {
                    myMeasureHeight = Math.min(myMeasureHeight, charHeight * text.length());
                }
            }
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            myMeasureWidth = widthSize - getPaddingLeft() - getPaddingRight();
            if (charHeight > 0) {
                int columnCount = (myMeasureHeight - charHeight) / (charHeight + rowSpacing) + 1;//一列的字符个数
                updateColumnTexts(columnCount);
            }
        } else {
            if (TextUtils.isEmpty(text)) {
                myMeasureWidth = 0;
            } else {
                if (charHeight > 0) {
                    int columnCount = 1;
                    if (columnLength > 0) {
                        columnCount = columnLength;
                        atMostHeight = true;
                    } else if (myMeasureHeight > 0) {
                        columnCount = (myMeasureHeight - charHeight) / (charHeight + rowSpacing) + 1;//一列的字符个数
                    }
                    updateColumnTexts(columnCount);
                    if (atMostHeight) {
                        myMeasureHeight = (charHeight + rowSpacing) * (columnCount - 1) + charHeight + (int) (Math.abs(fontMetrics.descent));
                    }
                    int column = columnTexts.size();
                    if (maxColumns > 0) {
                        if (column > maxColumns) {
                            isShowEllipsis = true;
                            column = maxColumns;
                            lastShowColumnIndex = maxColumns;
                        } else {
                            lastShowColumnIndex = column;
                        }
                    }
                    if (lastShowColumnIndex > 0) {
                        myMeasureWidth = (charWidth + columnSpacing) * (lastShowColumnIndex - 1) + charWidth;
                    } else {
                        myMeasureWidth = (charWidth + columnSpacing) * (column - 1) + charWidth;
                    }
                } else {
                    myMeasureWidth = getSuggestedMinimumWidth();
                }
            }
        }

        setMeasuredDimension(myMeasureWidth, myMeasureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int x = 0;
        int y = 0;
        if (columnTexts == null) {
            return;
        }
        for (int i = 0; i < columnTexts.size(); i++) { //按列画
            x = i == 0 ? paddingLeft : x + charWidth + columnSpacing;
            char[] chars = columnTexts.get(i).toCharArray();
            boolean isLastColumn = i == lastShowColumnIndex - 1;
            for (int j = 0; j < chars.length; j++) {
                y = j == 0 ? paddingTop + (int) Math.abs(fontMetrics.ascent) : y + charHeight + rowSpacing;
                if (lastShowColumnIndex == maxColumns && isShowEllipsis && j == chars.length - 1 && isLastColumn) {
                    canvas.drawText("\uE606", isCharCenter ? (x + charWidth / 2 + 1) : x, y, ellipsisPaint);
                    return;
                } else {
                    canvas.drawText(chars[j] + "", isCharCenter ? (x + charWidth / 2 + 1) : x, y, textPaint);
                }
            }
        }
    }

    public void setText(String text) {
        this.text = text;
        invalidateTextPaintAndMeasurements();
        requestLayout();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidateTextPaintAndMeasurements();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
        invalidateTextPaintAndMeasurements();
    }

    public void setColumnSpacing(int columnSpacing) {
        this.columnSpacing = columnSpacing;
        invalidateTextPaintAndMeasurements();
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
    }

    public void setCharCenter(boolean charCenter) {
        isCharCenter = charCenter;
    }

    public void setTypeface(Typeface tf, int style) {
        if (style > 0) {
            if (tf == null) {
                return;
            }
            tf = Typeface.create(tf, style);
            setTypeface(tf);
            int typefaceStyle = tf != null ? tf.getStyle() : 0;
            int need = style & ~typefaceStyle;
            textPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
            textPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
        } else {
            textPaint.setFakeBoldText(false);
            textPaint.setTextSkewX(0);
            setTypeface(tf);
        }
    }

    public void setTypeface(Typeface typeface) {
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        if (textPaint.getTypeface() != typeface) {
            textPaint.setTypeface(typeface);
        }
    }

    public int getVWidth() {
        return myMeasureWidth;
    }

    public int getVHeight() {
        return myMeasureHeight;
    }

    public String getText() {
        return text;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getRowSpacing() {
        return rowSpacing;
    }

    public int getColumnSpacing() {
        return columnSpacing;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public boolean isCharCenter() {
        return isCharCenter;
    }

    public boolean isAtMostHeight() {
        return atMostHeight;
    }

    public boolean isShowEllipsis() {
        return isShowEllipsis;
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
