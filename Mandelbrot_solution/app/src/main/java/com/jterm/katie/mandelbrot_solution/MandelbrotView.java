/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jterm.katie.mandelbrot_solution;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MandelbrotView extends View {

    private static final double X_MAX = 1;
    private static final double X_MIN = -2.5;
    private static final double Y_MAX = 1.75;
    private static final double Y_MIN = -1.75;

    private double mXMin = X_MIN;
    private double mXMax = X_MAX;
    private double mYMin = Y_MIN;
    private double mYMax = Y_MAX;

    private Paint mBackgroundPaint;
    private Paint mPaint;
    private int mMaxDepth;
    private int mNumPoints = 100;

    private int[] mColors;

    public MandelbrotView(Context context) {
        super(context);
        init();
    }

    public MandelbrotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MandelbrotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MandelbrotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(getResources().getColor(R.color.background_color));

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec == 0 || heightMeasureSpec == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        int size = Math.max(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(size, size);
    }

    public void reset(int depth) {
        mMaxDepth = depth;
        mXMin = X_MIN;
        mXMax = X_MAX;
        mYMin = Y_MIN;
        mYMax = Y_MAX;
        initializeColors();
        postInvalidate();
    }

    /**
     * Define the colors to use, based on the depth.
     * This will mean less calculation during onDraw!
     */
    private void initializeColors() {
        mColors = new int[mMaxDepth + 1];
        for (int i = 0; i < mMaxDepth; i++) {
            int r = (int) (255 * (i * 1.0 / mMaxDepth));
            int g = (int) (255 * (1 - i * 1.0 / mMaxDepth));
            mColors[i] = Color.rgb(r, g, 255);
        }
        mColors[mMaxDepth] = Color.BLACK;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);

        if (mColors == null || mColors.length == 0) {
            return;
        }

        int dotSize = getWidth() / mNumPoints;
        for (int i = 0; i < mNumPoints; i++) {

            // map i from 0 to numPoints into mandelbrot coordinates, X_MIN to X_MAX
            double mX = (i * 1.0) / mNumPoints * (mXMax - mXMin) + mXMin;

            // map i from 0 to numPoints into canvas coordinates, 0 to getWidth
            float x = (i * 1.0f) / mNumPoints * getWidth();

            for (int j = 0; j < mNumPoints; j++) {
                double mY = (j * 1.0) / mNumPoints * (mYMax - mYMin) + mYMin;
                mPaint.setColor(getMandelbrotColor(mX, mY));

                // map j from 0 to numPoints into canvas coordinates, 0 to getHeight
                float y = (j * 1.0f) / mNumPoints * getHeight();

                // Draw a point at this location.
                canvas.drawCircle(x, y, dotSize, mPaint);
            }
        }
    }

    /**
     * Gets the color for a pixel in mandelbrot X, Y coordinates
     *
     * Pseudocode:
     * x = 0.0
     * y = 0.0
     * iteration = 0
     * max_iteration = 1000
     * while (x*x + y*y < 2*2  AND  iteration < max_iteration) {
     *   xtemp = x*x - y*y + x0
     *   y = 2*x*y + y0
     *   x = xtemp
     *   iteration = iteration + 1
     * }
     * return colors[iteration]
     */
    private int getMandelbrotColor(double mX, double mY) {
        double x = 0;
        double y = 0;
        int iter = 0;
        while (x * x + y * y < 4 && iter < mMaxDepth) {
            double temp = x * x - y * y + mX;
            y = 2 * x * y + mY;
            x = temp;
            iter++;
        }
        return mColors[iter];
    }
}
