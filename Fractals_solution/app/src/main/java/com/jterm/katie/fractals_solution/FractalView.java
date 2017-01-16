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
package com.jterm.katie.fractals_solution;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class FractalView extends View {
    private Paint mBackgroundPaint;
    private Paint mPaint;
    private Paint mLeafPaint;
    private int mMaxDepth = -1;
    private Random mRandom;

    public FractalView(Context context) {
        super(context);
        init();
    }

    public FractalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FractalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FractalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(getResources().getColor(R.color.background_color));

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.branch_color));
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mLeafPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeafPaint.setColor(getResources().getColor(R.color.leaf_color));

        mRandom = new Random();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                myCurrentX = motionEvent.getX();
                motionEvent.getY();
                motionEvent.getActionMasked();
                return false;
            }
        });
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
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
        if (mMaxDepth < 0) {
            return;
        }
        // Start the tree in the middle of the bottom of the screen, with angle PI (straight up).
        drawFractalTree(canvas, getWidth() / 2, getHeight(), Math.PI, 0);
    }

    /**
     *
     * @param canvas The canvas to use for drawing
     * @param x The X position of the base of this tree
     * @param y The Y position of the base of this tree
     * @param angle The angle to sprout from the base of this tree
     * @param depth The depth the tree should be.
     */
    private void drawFractalTree(Canvas canvas, float x, float y, double angle, int depth) {
        // Base case
        if (depth >= mMaxDepth) {
            canvas.drawCircle(x, y, getWidth() / 50, mLeafPaint);
            return;
        }

        float branchLength = getBranchLength(depth);
        mPaint.setStrokeWidth(getBranchWidth(depth));
        // Setting the color would be pretty cool.

        // Use sin and cos to find the end point of this branch.
        float xNew = (float) (x + Math.sin(angle) * branchLength);
        float yNew = (float) (y + Math.cos(angle) * branchLength);

        canvas.drawLine(x, y, xNew, yNew, mPaint);

        // Draw two branches recursively
        double angleNewFirst = angle - mRandom.nextInt(10) / 20.0;
        double angleNewSecond = angle + mRandom.nextInt(10) / 20.0;
        drawFractalTree(canvas, xNew, yNew, angleNewFirst, depth + 1);
        drawFractalTree(canvas, xNew, yNew, angleNewSecond, depth + 1);
    }

    private float getBranchLength(int depth) {
        // Add some randomness to the length
        float baseLength = getWidth() / 6 * (1 - mRandom.nextInt(10) / 30.0f);

        // Scale it based on the depth
        return baseLength * (mMaxDepth - depth) * 1.0f / mMaxDepth;
    }

    private float getBranchWidth(int depth) {
        // A non-linear scale would be nice here, to get much thinner trunks and thicker branches
        return (float) (30 * Math.pow((mMaxDepth - depth), 2) / (mMaxDepth * mMaxDepth));
    }
}
