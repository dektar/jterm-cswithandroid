/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.continentaldivide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;


public class ContinentMap extends View {

    private final ContinentMapController mController;
    private final Paint mSquarePaint;
    private final Paint mTextPaint;

    public ContinentMap(Context context) {
        super(context);
        mController = new ContinentMapController();
        mSquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(getResources().getColor(R.color.label_color));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(getResources().getDimension(R.dimen.label_size));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    /**
     * Resets the continental divide info.
     */
    public void clearContinentalDivide() {
        mController.clearContinentalDivide();
        invalidate();
    }

    /**
     * Draws the current state of the Continent
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // EXAMPLE SOLUTION

        // Get the pixel width of the square.
        int squareWidth = getWidth() / mController.getBoardSize();
        float textOffset = mTextPaint.getTextSize() / 2;

        // For each cell, paint it the appropriate color and label it by height.
        ContinentMapController.Cell cell;
        for (int x = 0; x < mController.getBoardSize(); x++) {
            for (int y = 0; y < mController.getBoardSize(); y++) {
                cell = mController.getCell(x, y);
                mSquarePaint.setColor(getCellColor(cell));
                canvas.drawRect(squareWidth * x, squareWidth * y, squareWidth * (x + 1),
                        squareWidth * (y + 1), mSquarePaint);
                canvas.drawText("" + cell.height, squareWidth * (2 * x + 1) / 2,
                        squareWidth * (2 * y + 1) / 2 + textOffset,  mTextPaint);
            }
        }

        // END EXAMPLE SOLUTION
    }

    private int getCellColor(ContinentMapController.Cell cell) {
        // Linearly scale the color between white and medium grey, based on the min/max of the
        // current board.
        float ratio = 1 - (cell.height - mController.getMinHeight()) * 1.0f /
                (mController.getMaxHeight() - mController.getMinHeight());
        float red = 127 + ratio * 128;
        float green = 127 + ratio * 128;
        float blue = 127 + ratio * 128;

        if (cell.flowsNW && cell.flowsSE) {
            // This cell flows into both oceans -> Turn it reddish
            green -= 50;
            blue -= 50;
        } else if (cell.flowsNW) {
            // Green ocean -> Tint it greenish
            red -= 50;
            blue -= 50;
        } else if (cell.flowsSE) {
            // Blue ocean -> Tint it blueish
            red -= 50;
            green -= 50;
        } else if (cell.basin) {
            // Basin -> Tint it yellowish
            blue -= 100;
        }
        return Color.rgb((int) red, (int) green, (int) blue);
    }

    public void buildUpContinentalDivide(boolean oneStep) {
        mController.buildUpContinentalDivide(oneStep);
        invalidate();
    }

    public void buildDownContinentalDivide(boolean oneStep) {
        mController.buildDownContinentalDivide(oneStep);
        invalidate();
    }

    public void generateTerrain(int detail) {
        mController.generateTerrain(detail);
        invalidate();
    }
}
