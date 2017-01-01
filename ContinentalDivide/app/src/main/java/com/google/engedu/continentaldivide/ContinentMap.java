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

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class ContinentMap extends View {

    private final ContinentMapController mController;

    public ContinentMap(Context context) {
        super(context);
        mController = new ContinentMapController();
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
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
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
