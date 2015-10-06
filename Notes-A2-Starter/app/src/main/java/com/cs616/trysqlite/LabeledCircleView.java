package com.cs616.trysqlite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ian on 15-09-13.
 */
public class LabeledCircleView extends View {

    private static int LIGHTEN_BASE = 75;
    private static float LABEL_HEIGHT = 60.0f;

    // The letter to display in the center of the circle
    private char label;

    // The paint for the circle and the text
    private Paint paint;
    private Paint textPaint;

    // The position of the circle
    private RectF position;

    // The bounding box of the rendered label
    private Rect textBounds;

    public LabeledCircleView(Context context) {
        super(context);
        init();
    }

    public LabeledCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialized the view with default colors.
     */
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(LABEL_HEIGHT);

        setLabel('A');
        setColor(220, 150, 86);
    }

    /**
     * Get the circle's color
     * @return The circle's color
     */
    public int getColor() {
        return paint.getColor();
    }

    /**
     * Set the circle's color using RGB
     * @param red The red component (0-255)
     * @param green The green component (0-255)
     * @param blue The blue component (0-255)
     */
    public void setColor(int red, int green, int blue) {
        paint.setColor(Color.rgb(red, green, blue));

        float redF = (float) red / 255f;
        float greenF = (float) green / 255f;
        float blueF = (float) blue / 255f;

        int rest = 255 - LIGHTEN_BASE;
        textPaint.setColor(Color.rgb(
                (int) (redF * LIGHTEN_BASE + rest),
                (int) (greenF * LIGHTEN_BASE + rest),
                (int) (blueF * LIGHTEN_BASE + rest)
        ));

        // force a redraw
        invalidate();
    }

    /**
     * Get the circle's label
     * @return The label
     */
    public char getLabel() {
        return label;
    }

    /**
     * Set the circle's label
     * @param label The circle's label
     */
    public void setLabel(char label) {
        this.label = label;

        // recalculate the text bounds since this will now be different
        textBounds = new Rect();
        textPaint.getTextBounds(Character.toString(label), 0, 1, textBounds);

        // force a redraw
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(position, paint);
        canvas.drawText(Character.toString(label),
                        (position.left + position.right) / 2.0f,
                        (position.top + position.bottom) / 2.0f - textBounds.exactCenterY(),
                        textPaint);
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        float xPadding = (float) (getPaddingLeft() + getPaddingRight());
        float yPadding = (float) (getPaddingTop() + getPaddingBottom());
        float actualWidth = (float) width - xPadding;
        float actualHeight = (float) height - yPadding;

        float diameter = Math.min(actualWidth, actualHeight);

        // Fix the position of the circle offset by top/left padding
        position = new RectF(0.0f, 0.0f, diameter, diameter);
        position.offset(getPaddingLeft(), getPaddingTop());
    }

}
